package com.bornium.infrastructurebootstrapping;

import com.bornium.infrastructurebootstrapping.access.Ssh;
import com.bornium.infrastructurebootstrapping.commands.CloudCommands;
import com.bornium.infrastructurebootstrapping.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.entities.hypervisor.Virsh;
import com.bornium.infrastructurebootstrapping.entities.machine.Disk;
import com.bornium.infrastructurebootstrapping.entities.machine.Memory;
import com.bornium.infrastructurebootstrapping.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.entities.user.PasswordAuthentication;
import com.bornium.infrastructurebootstrapping.entities.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class DevMethods {
    @Test
    public void sshTest() {
        String user = System.getenv("USER");
        String pwd = System.getenv("PWD");
        String host = System.getenv("IP");
        Integer port = Integer.parseInt(System.getenv("PORT"));

        Ssh ssh = new Ssh(host, port, new User(user, new PasswordAuthentication(pwd)));

        ssh.connect();
        ssh.execSudo("virsh list --all");
        ssh.disconnect();
    }

    @Test
    public void sshInit() {
        String user = System.getenv("USER");
        String pwd = System.getenv("PWD");
        String host = System.getenv("IP");
        Integer port = Integer.parseInt(System.getenv("PORT"));

        Ssh ssh = new Ssh(host, port, new User(user, new PasswordAuthentication(pwd)));

        ssh.connect();
        ssh.exec("date", "date");
        ssh.execSudo("date", "date");
        ssh.exec("whoami");
        ssh.execSudo("whoami");
        ssh.execSudo("whoami");
        ssh.exec("whoami", "sudo whoami");

        ssh.disconnect();
    }

//    @Test
//    public void objectMapper() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        String qemu = mapper.writeValueAsString(new Qemu());
//        DefaultGroovyMethods.println(this, qemu);
//        Hypervisor h = mapper.readValue(qemu, Hypervisor.class);
//        DefaultGroovyMethods.println(this, h.getClass().getSimpleName());
//    }

    private Cloud createTestCloud(String user, String pwd, String host, Integer port) {
        Disk disk = new Disk();
        disk.setType(Disk.Type.SLOW);
        disk.setSize(new Memory(0, 20, 0, 0, 0));

        VirtualMachine vm = new VirtualMachine("testvm", new ContainerLinux(), disk, new Memory(0, 2, 0, 0, 0), 1, "00:0d:83:b1:c0:8e");
        Virsh virsh = new Virsh(host, port, new User(user, new PasswordAuthentication(pwd)), "testhypervisor");
        virsh.getMachines().put(vm.getBaseId().getId(), vm);
        Cloud c = new Cloud();
        c.getHypervisors().put(virsh.getBaseId().getId(), virsh);
        return c;
    }

    @Test
    public void createCloud() throws Exception {
        String user = System.getenv("USER");
        String pwd = System.getenv("PWD");
        String host = System.getenv("IP");
        Integer port = Integer.parseInt(System.getenv("PORT"));

        Cloud c = createTestCloud(user, pwd, host, port);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(c));

        CloudCommands cloudCommands = new CloudCommands(objectMapper);
        cloudCommands.create(c);
    }


    @Test
    public void deleteCloud() throws Exception {
        String user = System.getenv("USER");
        String pwd = System.getenv("PWD");
        String host = System.getenv("IP");
        Integer port = Integer.parseInt(System.getenv("PORT"));

        Cloud c = createTestCloud(user, pwd, host, port);

        CloudCommands cloudCommands = new CloudCommands(new ObjectMapper());
        cloudCommands.delete(c);
    }

}
