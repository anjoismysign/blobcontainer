package io.github.anjoismysign.blobcontainer;

import io.github.anjoismysign.blobcontainer.api.ContainerOwner;
import io.github.anjoismysign.blobcontainer.configuration.ContainerConfiguration;
import io.github.anjoismysign.blobcontainer.entity.Container;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BlobContainerAPI {

    @Nullable
    public static Container getContainer(String identifier){
        return ContainerConfiguration.getInstance().getContainersByIdentifier().get(identifier);
    }

    @Nullable
    public static ContainerOwner getContainerOwner(Player player){
        return BlobContainer.getInstance().getAccountCruder().getAccount(player);
    }

}
