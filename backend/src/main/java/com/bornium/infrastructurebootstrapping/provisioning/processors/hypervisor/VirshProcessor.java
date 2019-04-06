//package com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor;
//
//import com.bornium.infrastructurebootstrapping.base.access.Vnc;
//import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Virsh;
//import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
//import org.springframework.util.StreamUtils;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.UUID;
//import java.util.regex.Pattern;
//
//public class VirshProcessor extends HypervisorProcessor<Virsh> {
//
//    public VirshProcessor(Virsh hypervisor) {
//        super(hypervisor);
//    }
//
//    @Override
//    protected void installVm(VirtualMachine vm) throws Exception {
//        ssh.execSudoPrint("mkdir -p " + vmPath(vm) + "/helper");
//        vm.getOperatingSystem().createInstallHelperFiles(this, vm);
//
//        ssh.execSudoPrint("mkisofs -o " + vmPath(vm) + "/helper.iso " + vmPath(vm) + "/helper");
//
//        ssh.execSudoPrint("rm " + vmPath(vm) + "/vm.xml");
//        ssh.execSudoPrint("bash -c \"echo '" + createVmXml(vm) + "' > " + vmPath(vm) + "/vm.xml\"");
//        ssh.execSudoPrint("virsh define " + vmPath(vm) + "/vm.xml");
//        ssh.execSudoPrint("virsh autostart testvm");
//        ssh.execSudoPrint("virsh start testvm");
//
//        //TODO actually look at the images received to see if terminal is ready instead of waiting 60 secs
//        Thread.sleep(60000);
//
//        Vnc vnc = new Vnc(hypervisor.getHost());
//        //vnc.exec(vm.getOperatingSystem().getVncCommandForInstallAndShutdown(vm, "/dev/sr1"));
//        vnc.close();
//
//        System.out.println("Waiting for install");
//        while (true) {
//            String vmState = ssh.execSudo("virsh domstate " + vm.getId());
//            Thread.sleep(1000);
//            if (vmState.contains("shut off"))
//                break;
//        }
//        System.out.println("Install done");
//
//        ssh.execSudoPrint("virsh start " + vm.getId());
//    }
//
//
//    private String createVmXml(VirtualMachine vm) throws IOException {
//        String xml = StreamUtils.copyToString(this.getClass().getResourceAsStream("/hypervisor/virsh/vm-template.xml"), Charset.defaultCharset());
//
//        xml = xml
//                .replaceAll(Pattern.quote("${name}"), vm.getId().getId())
//                .replaceAll(Pattern.quote("${uuid}"), UUID.randomUUID().toString())
//                .replaceAll(Pattern.quote("${memory}"), String.valueOf(vm.getMachineSpec().getRam().bytes()))
//                .replaceAll(Pattern.quote("${cpus}"), String.valueOf(vm.getMachineSpec().getCpus()))
//                .replaceAll(Pattern.quote("${baseimg}"), "/home/" + hypervisor.getUsername() + "/" + baseImagePath(vm))
//                .replaceAll(Pattern.quote("${bootimg}"), "/home/" + hypervisor.getUsername() + "/" + getImagePath(vm))
//                .replaceAll(Pattern.quote("${helperimg}"), "/home/" + hypervisor.getUsername() + "/" + vmPath(vm) + "/helper.iso")
//                .replaceAll(Pattern.quote("${mac}"), vm.getMac())
//                .replace("\"", "\\\"");
//        return xml;
//    }
//
//    @Override
//    void createDisks(VirtualMachine vm) {
//        vm.getMachineSpec().getDisks().forEach(disk -> {
//            ssh.execSudoPrint("qemu-img create -f qcow2 " + baseImagePath(vm) + " " + disk.getSize().bytes() + "B");
//        });
//
//    }
//
//    @Override
//    public void delete(VirtualMachine vm) {
//        ssh.execSudoPrint("virsh destroy " + vm.getId());
//        ssh.execSudoPrint("virsh undefine " + vm.getId());
//        ssh.execSudoPrint("rm -r " + vmPath(vm));
//    }
//
//
//}
