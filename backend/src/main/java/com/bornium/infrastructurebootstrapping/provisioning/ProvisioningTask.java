package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.services.AuthenticationsService;

import java.util.stream.Stream;

public abstract class ProvisioningTask {
    private final Hypervisor hypervisor;
    private final VirtualMachine virtualMachine;
    private Credentials vmCredentials;
    private AuthenticationsService authenticationsService;
    private final Ssh hypervisorSsh;
    private final Ssh vmSsh;
    private final Credentials loginCredentials;
    private final OperatingSystem operatingSystem;
    private final MachineSpec machineSpec;

    public ProvisioningTask(Hypervisor hypervisor, Credentials hypervisorCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec, Credentials vmCredentials, AuthenticationsService authenticationsService) {
        this.loginCredentials = hypervisorCredentials;
        this.operatingSystem = operatingSystem;
        this.machineSpec = machineSpec;
        this.hypervisor = hypervisor;
        this.virtualMachine = virtualMachine;
        this.vmCredentials = vmCredentials;
        this.authenticationsService = authenticationsService;
        this.hypervisorSsh = new Ssh(hypervisor.getHost(), hypervisor.getPort(), hypervisor.getUsername(), hypervisorCredentials);
        this.vmSsh = new Ssh(virtualMachine.getHost(),22,virtualMachine.getSshUser(), vmCredentials);
    }

    public void createVm() throws Exception{
        createVMDirectory();
        createDisks();
        downloadImage();
        installVmAndReboot();
        waitUntilVmBoot();
        postProcessVm();
        installPlatform();
    }

    private void installPlatform() {
        operatingSystem.installPlatform(this);
    }

    protected abstract void postProcessVm();

    private void waitUntilVmBoot() throws InterruptedException {
        System.out.println("Waiting for VM to start up");
        int counter = 0;
        while(true){
            try {
                getVmSsh().disconnect();
                String res = getVmSsh().exec("ls");
                System.out.println(res);
                if (Stream.of(res.split("\n")).filter(str -> str.contains("exit-status:")).findFirst().get().contains("0"))
                    break;
            }catch (Exception e){
                e.printStackTrace();
            }
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
        hypervisorSsh.execPrint("mkdir -p " + Ssh.quote(vmPath()));
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

    public Ssh getHypervisorSsh() {
        return hypervisorSsh;
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

    public Ssh getVmSsh() {
        return vmSsh;
    }

    public Credentials getVmCredentials() {
        return vmCredentials;
    }

    public AuthenticationsService getAuthenticationsService() {
        return authenticationsService;
    }
}
