package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

import java.util.stream.Stream;

public abstract class ProvisioningTask {
    private final Hypervisor hypervisor;
    private final VirtualMachine virtualMachine;
    private final Ssh ssh;
    private final Credentials loginCredentials;
    private final OperatingSystem operatingSystem;
    private final MachineSpec machineSpec;

    public ProvisioningTask(Hypervisor hypervisor, Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec) {
        this.loginCredentials = loginCredentials;
        this.operatingSystem = operatingSystem;
        this.machineSpec = machineSpec;
        this.hypervisor = hypervisor;
        this.virtualMachine = virtualMachine;
        this.ssh = new Ssh(hypervisor.getHost(), hypervisor.getPort(), hypervisor.getUsername(), loginCredentials);
    }

    public void createVm() throws Exception{
        createVMDirectory();
        createDisks();
        downloadImage();
        installVmAndReboot();
        waitUntilVmBoot();
        postProcessVm();
    }

    protected abstract void postProcessVm();

    private void waitUntilVmBoot() throws InterruptedException {
        System.out.println("Waiting for VM to start up");
        int counter = 0;
        while(true){
            getSsh().disconnect();
            String res = getSsh().exec("ls");
            if(Stream.of(res.split("\n")).filter(str -> str.contains("exit-status:")).findFirst().get().contains("0"))
                break;
            counter++;
            if(counter >= 300)
                throw new RuntimeException("could not connect to newly created vm with name: " + virtualMachine.getId());
            Thread.sleep(1000);
        }
        System.out.println("VM started up");
    }

    public void deleteVm() throws Exception{
        delete();
    }

    public void recreateVm() throws Exception{
        deleteVm();
        createVm();
    }

    protected abstract void installVmAndReboot() throws Exception;

    protected abstract void createDisks() throws Exception;

    protected abstract void delete() throws Exception;

    public String getWorkdir() {
        return "ib";
    }

    public String getBase() {
        return getWorkdir() + "/" + hypervisor.getType();
    }

    public String getImages() {
        return getWorkdir() + "/images";
    }

    public String getImagePath() {
        return getImages() + "/" + operatingSystem.getInstallImage();
    }

    protected void downloadImage() {
        String imgName = operatingSystem.getDownloadLink();

    }

    public void createVMDirectory() {
        ssh.execPrint("mkdir -p " + Ssh.quote(vmPath()));
    }

    public String vmPath() {
        return getBase() + "/" + virtualMachine.getId();
    }

    protected String baseImagePath() {
        return vmPath() + "/base.img";
    }


    public Hypervisor getHypervisor() {
        return hypervisor;
    }

    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public Ssh getSsh() {
        return ssh;
    }

    public Credentials getLoginCredentials() {
        return loginCredentials;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public MachineSpec getMachineSpec() {
        return machineSpec;
    }
}
