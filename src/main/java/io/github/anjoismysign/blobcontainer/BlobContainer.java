package io.github.anjoismysign.blobcontainer;

import io.github.anjoismysign.blobcontainer.command.BlobContainerCommand;
import io.github.anjoismysign.blobcontainer.director.ContainerManagerDirector;
import io.github.anjoismysign.blobcontainer.entity.ContainerProfile;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.cruder.ChunkedAccountCruder;
import org.bukkit.Bukkit;

public final class BlobContainer extends BlobPlugin {
    private static BlobContainer instance;

    public static BlobContainer getInstance(){
        return instance;
    }

    private ContainerManagerDirector director;
    private ChunkedAccountCruder<ContainerProfile> accountCruder;

    @Override
    public void onEnable() {
        instance = this;
        director = new ContainerManagerDirector(this);
        BlobContainerCommand.INSTANCE.load();
        Bukkit.getScheduler().runTask(this, ()->{
           accountCruder = new ChunkedAccountCruder<>(this, ContainerProfile.class);
        });
    }

    @Override
    public void onDisable(){
        super.onDisable();
        accountCruder.shutdown();
    }

    public ContainerManagerDirector getManagerDirector() {
        return director;
    }

    public ChunkedAccountCruder<ContainerProfile> getAccountCruder() {
        return accountCruder;
    }
}
