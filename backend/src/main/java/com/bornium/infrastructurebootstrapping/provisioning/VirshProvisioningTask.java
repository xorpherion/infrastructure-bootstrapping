package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.base.access.Vnc;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.regex.Pattern;

public class VirshProvisioningTask extends ProvisioningTask {

    public VirshProvisioningTask(Hypervisor hypervisor, Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec) {
        super(hypervisor, loginCredentials, virtualMachine, operatingSystem, machineSpec);
    }

    @Override
    protected void installVm()  throws Exception{
        getSsh().execSudoPrint("mkdir -p " + Ssh.quote(vmPath() + "/helper"));
        getOperatingSystem().createInstallHelperFiles(this);

        getSsh().execSudoPrint("mkisofs -o " + Ssh.quote(vmPath() + "/helper.iso") +" " + Ssh.quote(vmPath() + "/helper"));

        getSsh().execSudoPrint("rm " + Ssh.quote(vmPath() + "/vm.xml"));
        getSsh().execSudoPrint("bash -c \"echo '" + createVmXml(getVirtualMachine()) + "' > " + Ssh.quote(vmPath() + "/vm.xml") + "\"");
        getSsh().execSudoPrint("virsh define " + Ssh.quote(vmPath() + "/vm.xml"));
        getSsh().execSudoPrint("virsh autostart " +Ssh.quote(getVirtualMachine().getId()));
        getSsh().execSudoPrint("virsh start " + Ssh.quote(getVirtualMachine().getId()));

        System.out.println("Waiting 60 seconds for vm startup");
        //TODO actually look at the images received to see if terminal is ready instead of waiting 60 secs
        Thread.sleep(60000);

        Vnc vnc = new Vnc(getHypervisor().getHost());
        vnc.exec(getOperatingSystem().getVncCommandForInstallAndShutdown(this, "/dev/sr1"));
        vnc.close();

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
                .replaceAll(Pattern.quote("${bootimg}"), "/home/" + getHypervisor().getUsername() + "/" + getImagePath())
                .replaceAll(Pattern.quote("${helperimg}"), "/home/" + getHypervisor().getUsername() + "/" + vmPath() + "/helper.iso")
                .replaceAll(Pattern.quote("${mac}"), vm.getMac())
                .replace("\"", "\\\"");
        return xml;
    }
}
