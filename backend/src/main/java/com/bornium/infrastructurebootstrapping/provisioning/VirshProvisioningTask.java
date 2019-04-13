package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.base.access.Vnc;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import org.springframework.util.StreamUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class VirshProvisioningTask extends ProvisioningTask {

    public VirshProvisioningTask(Hypervisor hypervisor, Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec) {
        super(hypervisor, loginCredentials, virtualMachine, operatingSystem, machineSpec);
    }

    @Override
    protected void postProcessVm() {
        Ssh vmSsh = null;
        vmSsh.execSudoPrint("mkdir /ib/");
        vmSsh.execSudoPrint("mount -t 9p -o rw,trans=virtio,version=9p2000.L mounted /ib/");
    }

    @Override
    protected void installVmAndReboot()  throws Exception{
        //getSsh().execSudoPrint("mkdir -p " + Ssh.quote(vmPath() + "/helper"));
        getOperatingSystem().createInstallHelperFiles(this);

        //getSsh().execSudoPrint("mkisofs -o " + Ssh.quote(vmPath() + "/helper.iso") +" " + Ssh.quote(vmPath() + "/helper"));

        getSsh().execSudoPrint("rm " + Ssh.quote(vmPath() + "/vm.xml"));
        getSsh().execSudoPrint("bash -c \"echo '" + createVmXml(getVirtualMachine()) + "' > " + Ssh.quote(vmPath() + "/vm.xml") + "\"");
        getSsh().execSudoPrint("virsh define " + Ssh.quote(vmPath() + "/vm.xml"));
        getSsh().execSudoPrint("virsh autostart " +Ssh.quote(getVirtualMachine().getId()));
        getSsh().execSudoPrint("virsh start " + Ssh.quote(getVirtualMachine().getId()));

        String vncPortStr = getSsh().execSudoPrint("virsh vncdisplay " + Ssh.quote(getVirtualMachine().getId())).split(":")[1].substring(0,1);
        int vncPort = Integer.parseInt(vncPortStr);

        System.out.println("Waiting for vm startup");

        CountDownLatch cdl = new CountDownLatch(1);

        Vnc vnc = new Vnc(getHypervisor().getHost(), 5900 + vncPort, blockUntilImageStabilizes(cdl));

        cdl.await();

        vnc.exec("sudo mkdir /ib/");
        vnc.exec("sudo mount -t 9p -o rw,trans=virtio,version=9p2000.L mounted /ib/");
        vnc.close();

        Vnc vnc1 = new Vnc(getHypervisor().getHost(),5900 + vncPort,null);

        Arrays.asList(getOperatingSystem().getVncCommandForInstallAndShutdown(this)).forEach(cmd -> vnc1.exec(cmd));
        vnc1.close();

        System.out.println("Waiting for install");
        while (true) {
            String vmState = getSsh().execSudo("virsh domstate " + Ssh.quote(getVirtualMachine().getId()));
            Thread.sleep(1000);
            if (vmState.contains("shut off"))
                break;
        }
        System.out.println("Install done");

        getSsh().execSudoPrint("virsh start " + Ssh.quote(getVirtualMachine().getId()));
    }

    private Consumer<Image> blockUntilImageStabilizes(CountDownLatch cdl) {
        return new Consumer<Image>() {

            int counter = -1;
            final int numImages = 5;
            BufferedImage[] lastImages = new BufferedImage[numImages];
            long[] lastDiffs = new long[numImages];
            long lastAcc = 0;
            int accsSet = 0;
            boolean done = false;

            @Override
            public void accept(Image vncImage) {
                if(done)
                    return;
                counter++;
                BufferedImage orig = (BufferedImage) vncImage;
                BufferedImage bwImage = new BufferedImage(orig.getWidth(),orig.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D graphic = bwImage.createGraphics();
                graphic.drawImage(vncImage, 0, 0, Color.WHITE, null);
                graphic.dispose();

                lastImages[counter%numImages] = bwImage;

                if(counter < numImages)
                    return;

                int[] changeImage = new int[((DataBufferByte)bwImage.getRaster().getDataBuffer()).getData().length];

                Stream.of(lastImages).forEach(image -> {
                    byte[] imgData = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
                    for(int i = 0; i < imgData.length; i++)
                        changeImage[i] += imgData[i];
                });

                byte[] finalData = new byte[((DataBufferByte)bwImage.getRaster().getDataBuffer()).getData().length];
                for(int i = 0; i < changeImage.length; i++)
                    finalData[i] = (byte)(changeImage[i] / 5);

                long acc = 0;

                for(int i = 0; i < finalData.length; i++)
                    acc += finalData[i];

                lastDiffs[counter%numImages] = lastAcc - acc;
                lastAcc = acc;
                accsSet++;

                if(accsSet < 5)
                    return;

                if(!(LongStream.of(lastDiffs).map(operand -> Math.abs(operand)).sum() < 5000))
                    return;

                cdl.countDown();
                done = true;

                /*
                //dev only
                temporarilyWriteToFile(finalImage,finalData);
                */
            }

            private void temporarilyWriteToFile(BufferedImage bwImage, byte[] finalData) throws IOException {
                BufferedImage finalImage = new BufferedImage(bwImage.getWidth(),bwImage.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
                final byte[] a = ( (DataBufferByte) finalImage.getRaster().getDataBuffer() ).getData();
                System.arraycopy(finalData, 0, a, 0, finalData.length);

                Path path = Paths.get("tempImages/");
                if(!path.toFile().exists())
                    Files.createDirectories(path);
                ImageIO.write(bwImage,"jpg",new File("tempImages/image" + counter + ".jpg"));
            }
        };
    }

    @Override
    protected void createDisks()  throws Exception{
        getMachineSpec().getDisks().forEach(disk -> {
            getSsh().execSudoPrint("qemu-img create -f qcow2 " + Ssh.quote(baseImagePath()) + " " + disk.getSize().bytes() + "B");
        });
    }

    @Override
    protected void delete()  throws Exception{
        getSsh().execSudoPrint("virsh destroy " + Ssh.quote(getVirtualMachine().getId()));
        getSsh().execSudoPrint("virsh undefine " + Ssh.quote(getVirtualMachine().getId()));
        getSsh().execSudoPrint("rm -r " + Ssh.quote(vmPath()));
    }

    private String createVmXml(VirtualMachine vm) throws IOException {
        String xml = StreamUtils.copyToString(this.getClass().getResourceAsStream("/hypervisor/virsh/vm-template.xml"), Charset.defaultCharset());

        xml = xml
                .replaceAll(Pattern.quote("${name}"), vm.getId())
                .replaceAll(Pattern.quote("${uuid}"), UUID.randomUUID().toString())
                .replaceAll(Pattern.quote("${memory}"), String.valueOf(getMachineSpec().getRam().bytes()))
                .replaceAll(Pattern.quote("${cpus}"), String.valueOf(getMachineSpec().getCpus()))
                .replaceAll(Pattern.quote("${baseimg}"), "/home/" + getHypervisor().getUsername() + "/" + baseImagePath())
                .replaceAll(Pattern.quote("${bootimg}"), "/home/" + getHypervisor().getUsername() + "/" + "/ib/images/coreos_production_iso_image.iso")
                .replaceAll(Pattern.quote("${helperimg}"), "/home/" + getHypervisor().getUsername() + "/" + vmPath() + "/helper.iso")
                .replaceAll(Pattern.quote("${mac}"), vm.getMac())
                .replaceAll(Pattern.quote("${vmdir}"), "/home/"+getHypervisor().getUsername()+"/"+vmPath())
                .replace("\"", "\\\"");
        return xml;
    }
}
