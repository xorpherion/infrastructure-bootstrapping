package com.bornium.boostrappingascode.entities.hypervisor;

import com.bornium.boostrappingascode.access.Ssh;
import com.bornium.boostrappingascode.entities.Base;
import com.bornium.boostrappingascode.entities.machine.VirtualMachine;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Hypervisor extends Base {

    @Transient
    private Ssh ssh;

    @ElementCollection
    private Map<String, VirtualMachine> machines = new HashMap<String, VirtualMachine>();

    public void connect() {

    }

    public abstract Object create(VirtualMachine vm);

    public abstract Object delete(VirtualMachine vm);

    public abstract void createVms();

    public Ssh getSsh() {
        return ssh;
    }

    public void setSsh(Ssh ssh) {
        this.ssh = ssh;
    }

    public Map<String, VirtualMachine> getMachines() {
        return machines;
    }

    public void setMachines(Map<String, VirtualMachine> machines) {
        this.machines = machines;
    }
}
