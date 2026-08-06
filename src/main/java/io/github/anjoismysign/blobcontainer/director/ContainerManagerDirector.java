package io.github.anjoismysign.blobcontainer.director;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.director.manager.ContainerConfigurationManager;
import io.github.anjoismysign.blobcontainer.director.manager.PlayerContainerManager;
import io.github.anjoismysign.bloblib.manager.GenericManagerDirector;
import org.jetbrains.annotations.NotNull;

public class ContainerManagerDirector extends GenericManagerDirector<BlobContainer> {
    public ContainerManagerDirector(BlobContainer plugin) {
        super(plugin);
        addManager("ConfigurationManager",
                new ContainerConfigurationManager(this));
        addManager("PlayerContainerManager",
                new PlayerContainerManager(this));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getConfigurationManager().reload();
        getPlayerContainerManager().reload();
    }

    @Override
    public void unload() {
    }

    @NotNull
    public final ContainerConfigurationManager getConfigurationManager() {
        return getManager("ConfigurationManager", ContainerConfigurationManager.class);
    }

    @NotNull
    public final PlayerContainerManager getPlayerContainerManager(){
        return getManager("PlayerContainerManager", PlayerContainerManager.class);
    }
}