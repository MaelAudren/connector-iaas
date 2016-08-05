package org.ow2.proactive.connector.iaas.cloud.provider.vmware;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ow2.proactive.connector.iaas.model.Infrastructure;
import org.springframework.stereotype.Component;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.VirtualMachine;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;


@Component
public class VMWareProviderVirualMachineUtil {

    public VirtualMachineRelocateSpec getVirtualMachineRelocateSpec(Folder rootFolder)
            throws InvalidProperty, RuntimeFault, RemoteException {
        VirtualMachineRelocateSpec vmrs = new VirtualMachineRelocateSpec();
        vmrs.setPool(getManagedObjectReference(rootFolder));
        return vmrs;
    }

    public VirtualMachine searchVirtualMachineByName(String name, Folder rootFolder)
            throws InvalidProperty, RuntimeFault, RemoteException {
        return Optional.ofNullable( (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",
                name)).orElseThrow(() -> new ClientErrorException("The virtual machine "+name+ " could not be found",
                Response.Status.BAD_REQUEST));
    }

    public Set<VirtualMachine> getAllVirtualMachinesByInfrastructure(Folder rootFolder,
            Infrastructure infrastructure) {

        try {

            ManagedEntity[] managedEntities = new InventoryNavigator(rootFolder)
                    .searchManagedEntities("VirtualMachine");

            return IntStream.range(0, managedEntities.length)
                    .mapToObj(i -> (VirtualMachine) managedEntities[i]).collect(Collectors.toSet());

        } catch (RemoteException e) {
            throw new RuntimeException(
                "ERROR when retrieving VMWare instances for infrastructure : " + infrastructure, e);
        }

    }

    public Folder searchFolderByName(String name, Folder rootFolder)
            throws InvalidProperty, RuntimeFault, RemoteException {
        return (Folder) new InventoryNavigator(rootFolder).searchManagedEntity("Folder", name);
    }

    private ManagedObjectReference getManagedObjectReference(Folder rootFolder)
            throws InvalidProperty, RuntimeFault, RemoteException {
        ResourcePool rp = (ResourcePool) new InventoryNavigator(rootFolder)
                .searchManagedEntities("ResourcePool")[0];
        return rp.getMOR();
    }

}
