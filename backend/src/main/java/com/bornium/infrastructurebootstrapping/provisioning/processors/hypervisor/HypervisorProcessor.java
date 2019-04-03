package com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;


public abstract class HypervisorProcessor<T extends Hypervisor> {
    protected final T hypervisor;
    protected final Ssh ssh;

    public HypervisorProcessor(T hypervisor) {
        this.hypervisor = hypervisor;
        ssh = new Ssh(hypervisor.getHost(), hypervisor.getPort(), hypervisor.getUsername(), hypervisor.getLoginCredentials());
    }

    public static void createVMs(Hypervisor hypervisor) {
        hypervisor.getVms().stream().forEach(vm -> hypervisor.getProcessor().create(vm));
    }

    public static void deleteVMs(Hypervisor hypervisor) {
        hypervisor.getVms().stream().forEach(vm -> hypervisor.getProcessor().delete(vm));
    }

    public void create(VirtualMachine vm) {
        try {
            delete(vm);
            createVMDirectory(vm);
            createDisks(vm);
            //downloadImage(vm);
            installVm(vm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWorkdir() {
        return "ib";
    }

    public String getBase() {
        return getWorkdir() + "/" + hypervisor.getType();
    }

    public String getImages() {
        return getWorkdir() + "/images";
    }

    /*public String getImagePath(VirtualMachine vm) {
        return getImages() + "/" + vm.getOperatingSystem().getImageName();
    }*/

    protected abstract void installVm(VirtualMachine vm) throws Exception;

    /*protected void downloadImage(VirtualMachine vm) {
        String imgName = vm.getOperatingSystem().getDownloadLink();

    }*/

    public void createVMDirectory(VirtualMachine vm) {
        ssh.execPrint("mkdir -p " + vmPath(vm));
    }

    public String vmPath(VirtualMachine vm) {
        return getBase() + "/" + vm.getId();
    }

    abstract void createDisks(VirtualMachine vm);

    protected String baseImagePath(VirtualMachine vm) {
        return vmPath(vm) + "/base.img";
    }

    public abstract void delete(VirtualMachine vm);

    public T getHypervisor() {
        return hypervisor;
    }

    public Ssh getSsh() {
        return ssh;
    }
}
