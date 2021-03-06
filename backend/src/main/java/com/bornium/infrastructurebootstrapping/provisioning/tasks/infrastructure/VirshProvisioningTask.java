package com.bornium.infrastructurebootstrapping.provisioning.tasks.infrastructure;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.base.access.Vnc;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Volume;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.DiskPassthrough;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.FileSystem;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.PciPassthrough;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.services.AuthenticationsService;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class VirshProvisioningTask extends ProvisioningTask {

    public VirshProvisioningTask(Hypervisor hypervisor, Credentials hypervisorCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec, Credentials vmCredentials, AuthenticationsService authenticationsService) {
        super(hypervisor, hypervisorCredentials, virtualMachine, operatingSystem, machineSpec,vmCredentials, authenticationsService);
    }

    @Override
    protected void postProcessVm() {
        getVirtualMachine().getFileSystems().stream().forEach(fs -> {
//            getVmSsh().execSudoPrint("mkdir -p " + fs.getVmPath());
//            getVmSsh().execSudoPrint("mount -t 9p -o rw,trans=virtio,version=9p2000.L "+fs.getTarget()+ " " + fs.getVmPath());
//            getVmSsh().execSudoPrint("echo '"+fs.getTarget()+"                                  "+fs.getVmPath()+"     9p         rw,trans=virtio,version=9p2000.L  0       0' >> /etc/fstab");
        });
    }

    @Override
    protected void installVmAndReboot()  throws Exception{
        //getSsh().execSudoPrint("mkdir -p " + Ssh.quote(vmPath() + "/helper"));
        getOperatingSystem().createInstallHelperFiles(this);

        //getSsh().execSudoPrint("mkisofs -o " + Ssh.quote(vmPath() + "/helper.iso") +" " + Ssh.quote(vmPath() + "/helper"));

        getHypervisorSsh().execSudoPrint("rm " + Ssh.quote(vmPath() + "/vm.xml"));
        getHypervisorSsh().execSudoPrint("bash -c \"echo '" + createVmXml(getVirtualMachine(), "vm-template.xml") + "' > " + Ssh.quote(vmPath() + "/vm.xml") + "\"");
        getHypervisorSsh().execSudoPrint("virsh define " + Ssh.quote(vmPath() + "/vm.xml"));
        getHypervisorSsh().execSudoPrint("virsh autostart " +Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("virsh start " + Ssh.quote(getVirtualMachine().getId()));

        String vncPortStr = getHypervisorSsh().execSudoPrint("virsh vncdisplay " + Ssh.quote(getVirtualMachine().getId())).split(":")[1].substring(0,1);
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
            String vmState = getHypervisorSsh().execSudo("virsh domstate " + Ssh.quote(getVirtualMachine().getId()));
            Thread.sleep(1000);
            if (vmState.contains("shut off"))
                break;
        }
        System.out.println("Install done");


        getHypervisorSsh().execSudoPrint("virsh destroy " +Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("virsh undefine " +Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("bash -c \"echo '" + createVmXml(getVirtualMachine(), "vm-run-template.xml") + "' > " + Ssh.quote(vmPath() + "/prod.xml") + "\"");
        getHypervisorSsh().execSudoPrint("virsh define " + Ssh.quote(vmPath() + "/prod.xml"));
        getHypervisorSsh().execSudoPrint("virsh autostart " +Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("virsh start " + Ssh.quote(getVirtualMachine().getId()));
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
            getHypervisorSsh().execSudoPrint("qemu-img create -f qcow2 " + Ssh.quote(baseImagePath()) + " " + disk.getSize().bytes() + "B");
        });
        this.getVirtualMachine().getVolumes().stream().forEach(volume -> {
            getHypervisorSsh().execSudoPrint("qemu-img create -f qcow2 " + Ssh.quote(volume.getHostBaseDirectory() + "/" + getVirtualMachine().getId() + "-" + volume.getId() + ".qcow2") + " " + volume.getSize().bytes() + "B");
        });
    }

    @Override
    protected void delete()  throws Exception{
        getHypervisorSsh().execSudoPrint("virsh destroy " + Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("virsh undefine " + Ssh.quote(getVirtualMachine().getId()));
        getHypervisorSsh().execSudoPrint("rm -r " + Ssh.quote(vmPath()));
    }

    private String createVmXml(VirtualMachine vm, String xmlFile) throws IOException {
        String xml = StreamUtils.copyToString(this.getClass().getResourceAsStream("/hypervisor/virsh/" + xmlFile), Charset.defaultCharset());

        xml = xml
                .replaceAll(Pattern.quote("${name}"), vm.getId())
                .replaceAll(Pattern.quote("${uuid}"), UUID.randomUUID().toString())
                .replaceAll(Pattern.quote("${memory}"), String.valueOf(getMachineSpec().getRam().bytes()))
                .replaceAll(Pattern.quote("${cpus}"), String.valueOf(getMachineSpec().getCpus()))
                .replaceAll(Pattern.quote("${baseimg}"), "/home/" + getHypervisor().getUsername() + "/" + baseImagePath())
                .replaceAll(Pattern.quote("${bootimg}"), "/home/" + getHypervisor().getUsername() + "/ib/images/coreos_production_iso_image.iso")
                .replaceAll(Pattern.quote("${helperimg}"), "/home/" + getHypervisor().getUsername() + "/" + vmPath() + "/helper.iso")
                .replaceAll(Pattern.quote("${mac}"), vm.getMac())
                .replaceAll(Pattern.quote("${vmdir}"), "/home/"+getHypervisor().getUsername()+"/"+vmPath())
                .replaceAll(Pattern.quote("${devices}"), createDevices(vm))
                .replace("\"", "\\\"");
        return xml;
    }

    private String createDevices(VirtualMachine vm) {
        String result = IntStream.range(0,vm.getDiskPassthroughs().size()).mapToObj(i -> toDisk(vm.getDiskPassthroughs().get(i),i)).collect(Collectors.joining());
        result += vm.getPciPassthroughs().stream().map(this::toPci).collect(Collectors.joining());
        result += vm.getFileSystems().stream().map(this::toFileSystem).collect(Collectors.joining());
        result += vm.getVolumes().stream().map(this::toVolume).collect(Collectors.joining());

        if(result.contains("device=\"lun\""))
            result += "<controller type=\"scsi\" index=\"0\" model=\"virtio-scsi\"/>";

        return result;
    }

    private String toFileSystem(FileSystem fileSystem) {
        return "<filesystem type=\"mount\" accessmode=\"mapped\">\n" +
                "            <source dir=\""+fileSystem.getSource()+"\"/>\n" +
                "            <target dir=\""+ fileSystem.getTarget() +"\"/>\n" +
                "        </filesystem>";
    }

    private String toDisk(DiskPassthrough diskPassthrough, int i) {
        return "<disk type=\"block\" device=\""+diskPassthrough.getType().toString().toLowerCase()+"\">\n" +
                "    <driver name=\"qemu\" type=\"raw\" cache=\"none\"/>\n" +
                "    <source dev=\""+ diskPassthrough.getHostSource() +"\"/>\n" +
                "    <target dev=\""+ getDeviceName(diskPassthrough,i) +"\" bus=\""+ getBus(diskPassthrough)+"\"/>\n" +
                "    <alias name=\"ua-"+diskPassthrough.getId()+
                "\"/>" +
                "  </disk>\n";
    }

    private String getDeviceName(DiskPassthrough diskPassthrough, int i) {
        switch (diskPassthrough.getType()){
            case LUN: return "sd" + Character.toString(((char) 65 + i)).toLowerCase();
            case DISK: return "vd" + Character.toString(((char) 65 + i)).toLowerCase();
        }
        throw new RuntimeException("NYI");
    }

    private String getBus(DiskPassthrough diskPassthrough) {
        switch (diskPassthrough.getType()){
            case LUN: return "scsi";
            case DISK: return "virtio";
        }
        throw new RuntimeException("NYI");
    }

    private String toPci(PciPassthrough pciPassthrough) {
        return "<hostdev mode=\"subsystem\" type=\"pci\" managed=\"yes\">\n" +
                "  <source>\n" +
                "    <address domain=\"0x"+pciPassthrough.getDomain()+"\" bus=\"0x"+pciPassthrough.getBus()+"\" slot=\"0x"+pciPassthrough.getSlot()+"\" function=\"0x"+pciPassthrough.getFunction()+"\" />" +
                "  </source>\n" +
                "</hostdev>\n";
    }

    private String toVolume(Volume volume){
        return "<disk type=\"file\" device=\"disk\">\n" +
                "            <driver name=\"qemu\" type=\"qcow2\"/>\n" +
                "            <source file=\""+volume.getHostBaseDirectory()+ "/"+getVirtualMachine().getId() +"-"+volume.getId()+".qcow2"+"\"/>\n" +
                "            <target dev=\""+volume.getGuestDevice() +"\" bus=\"ide\"/>\n" +
                "        </disk>";
    }
}
