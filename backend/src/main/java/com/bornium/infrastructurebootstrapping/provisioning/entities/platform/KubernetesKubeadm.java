package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Machine;
import com.bornium.infrastructurebootstrapping.provisioning.services.CredentialsService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class KubernetesKubeadm extends Kubernetes {
    public KubernetesKubeadm(String id, List<String> masters,List<String> slaves) {
        super(id, masters, slaves);
    }

    @Override
    public void install(CredentialsService credentialsService, List<Machine> machines) throws Exception {
        Map<String,Machine> relevantMachines = findRelevantMachines(machines).stream().collect(Collectors.toMap(m -> m.getId(), m -> m));

        for (String master : getMasters())
            installMaster(relevantMachines.get(master),credentialsService);

        for (String slave : getSlaves())
            installSlave(relevantMachines.get(slave),credentialsService);

    }

    private void installSlave(Machine machine, CredentialsService credentialsService) throws FileNotFoundException, JSchException, SftpException {
        //TODO
        installMaster(machine,credentialsService);
    }

    private void installMaster(Machine machine, CredentialsService credentialsService) throws FileNotFoundException, JSchException, SftpException {
        Ssh ssh = new Ssh(machine.getHost(), machine.getSshPort(), machine.getSshUser(),credentialsService.get(machine.getCredentials()));
        String folder = "ib-tmp/" + UUID.randomUUID().toString();
        ssh.execSudoPrint("mkdir -p " + folder);
        ssh.execSudoPrint("chmod 777 "+ folder);
        ssh.copyToRemote("src/main/resources/operatingsystem/containerlinux/kubernetes/kubeadm/scripts/install.sh", folder + "/install.sh");
        ssh.execSudoPrint("chmod 777 " + folder + "/install.sh");

        ssh.execSudoPrint("bash -c \"" + folder + "/install.sh "+ machine.getIp() +"\"");
    }

    private List<Machine> findRelevantMachines(List<Machine> machines) {
        return machines.stream().filter(m -> getMasters().contains(m.getId()) || getSlaves().contains(m.getId())).collect(Collectors.toList());
    }
}
