//package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;
//
//import com.bornium.infrastructurebootstrapping.base.access.Ssh;
//import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
//
//import javax.persistence.Entity;
//
//@Entity
//public class Qemu extends Hypervisor {
//
//    public Qemu() {
//        this(null,null);
//    }
//
//    public Qemu(Ssh ssh, String id){
//        super(ssh,id,"qemu");
//    }
//
//    @Override
//    public void create(VirtualMachine vm) {
//        createVMDirectory(vm);
//        createDisk(vm);
//        downloadImage(vm);
//        installVm(vm);
//        getSsh().exec("whoami");
//    }
//
//    private void installVm(VirtualMachine vm) {
//        ssh.execSudo("qemu-system-x86_64 -vnc :0 -daemonize -enable-kvm -hda " + baseImagePath(vm)+ " -cdrom " + getImagePath(vm) + " -m " + vm.getRam().megabytes() + " -name " + vm.getBaseId().getId());
//    }
//
//    private void downloadImage(VirtualMachine vm) {
//        //String imgName = vm.getOperatingSystem().getImageName();
//
//    }
//
//    public void createVMDirectory(VirtualMachine vm){
//        ssh.exec("mkdir -p " + vmPath(vm));
//    }
//
//    private String vmPath(VirtualMachine vm) {
//        return getBase() + "/" + vm.getBaseId().getId();
//    }
//
//    void createDisk(VirtualMachine vm){
//        ssh.exec("qemu-img create " + baseImagePath(vm) + " " + vm.getDisk().getSize().bytes() + "B");
//    }
//
//    private String baseImagePath(VirtualMachine vm) {
//        return vmPath(vm) + "/base.img";
//    }
//
//    @Override
//    public void delete(VirtualMachine vm) {
//        //ssh.exec("qemu-system-x86_64 delvm " + vm.getBaseId().getId());
//        ssh.exec("rm -r " + vmPath(vm));
//    }
//}
