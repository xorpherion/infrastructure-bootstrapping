package com.bornium.boostrappingascode.processors.hypervisor;

import com.bornium.boostrappingascode.entities.hypervisor.Virsh;
import com.bornium.boostrappingascode.entities.machine.VirtualMachine;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.regex.Pattern;

public class VirshProcessor extends HypervisorProcessor<Virsh> {

    public VirshProcessor(Virsh hypervisor) {
        super(hypervisor);
    }

    @Override
    protected void cleanupCreate(VirtualMachine vm) {
    }

    @Override
    protected void mountInstallFiles() {
        //ssh.execSudo("virsh pool-create-as --name testpool --type dir --target ~/ib/images");
        //ssh.execSudo("virsh pool-start testpool");
    }

    @Override
    protected void initVm(VirtualMachine vm) throws Exception {
        ssh.execSudoPrint("mkdir -p " + vmPath(vm) + "/helper");
        ssh.execSudoPrint("touch " + vmPath(vm) + "/helper/helper.txt");
        ssh.execSudoPrint("bash -c 'echo helper > " + vmPath(vm) + "/helper/helper.txt'");

        ssh.execSudoPrint("mkisofs -o " + vmPath(vm) + "/helper.iso " + vmPath(vm) + "/helper");

        ssh.execSudoPrint("rm " + vmPath(vm) + "/vm.xml");
        ssh.execSudoPrint("bash -c \"echo '" + createVmXml(vm) + "' > " + vmPath(vm) + "/vm.xml\"");
        ssh.execSudoPrint("virsh define " + vmPath(vm) + "/vm.xml");
        ssh.execSudoPrint("virsh autostart testvm");
        ssh.execSudoPrint("virsh start testvm");
    }

    private String createVmXml(VirtualMachine vm) throws IOException {
        String xml = StreamUtils.copyToString(this.getClass().getResourceAsStream("/virsh/vm-template.xml"), Charset.defaultCharset());

        xml = xml
                .replaceAll(Pattern.quote("${name}"), vm.getBaseId().getId())
                .replaceAll(Pattern.quote("${uuid}"), UUID.randomUUID().toString())
                .replaceAll(Pattern.quote("${memory}"), String.valueOf(vm.getRam().bytes()))
                .replaceAll(Pattern.quote("${cpus}"), String.valueOf(vm.getCpus()))
                .replaceAll(Pattern.quote("${baseimg}"), "/home/" + hypervisor.getUser().getName() + "/" + baseImagePath(vm))
                .replaceAll(Pattern.quote("${bootimg}"), "/home/" + hypervisor.getUser().getName() + "/" + getImagePath(vm))
                .replaceAll(Pattern.quote("${helperimg}"), "/home/" + hypervisor.getUser().getName() + "/" + vmPath(vm) + "/helper.iso")
                .replaceAll(Pattern.quote("${mac}"), vm.getMac())
                .replace("\"", "\\\"");
        return xml;
    }

    @Override
    void createDisk(VirtualMachine vm) {
        ssh.execSudoPrint("qemu-img create -f qcow2 " + baseImagePath(vm) + " " + vm.getDisk().getSize().bytes() + "B");
    }

    @Override
    public void delete(VirtualMachine vm) {
        ssh.execSudoPrint("virsh destroy " + vm.getBaseId().getId());
        ssh.execSudoPrint("virsh undefine " + vm.getBaseId().getId());
        ssh.execSudoPrint("rm -r " + vmPath(vm));
    }


}
