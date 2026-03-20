package io.github.anjoismysign.blobcontainer.director.manager;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.director.ContainerManager;
import io.github.anjoismysign.blobcontainer.director.ContainerManagerDirector;
import io.github.anjoismysign.blobcontainer.entity.ContainerProfile;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlayerContainerManager extends ContainerManager {

    public PlayerContainerManager(ContainerManagerDirector managerDirector) {
        super(managerDirector);
        reload();
    }

    @Override
    public void reload() {
        BlobContainer plugin = getPlugin();
        var inventoryApi = BlobLibInventoryAPI.getInstance();
        var closeNamespacedKey = new NamespacedKey(plugin, "close");
        getManagerDirector().getConfigurationManager().getConfiguration().getContainers().forEach(container -> {
            String identifier = container.getIdentifier();
            String blobInventory = container.getBlobInventory();
            String button = container.getButton();
            var inventoryDataRegistry = inventoryApi.getInventoryDataRegistry(blobInventory);
            inventoryDataRegistry.onClose(closeNamespacedKey.toString(), (inventoryCloseEvent, sharableInventory)->{
                Player player = (Player) inventoryCloseEvent.getPlayer();
                Map<Integer, @Nullable ItemStack> contents = sharableInventory.getContents(button);
                @Nullable ContainerProfile profile = BlobContainer.getInstance().getAccountCruder().getAccount(player);
                profile.update(identifier,contents);
            });
        });
    }

}