package io.github.anjoismysign.blobcontainer.command;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.BlobContainerAPI;
import io.github.anjoismysign.blobcontainer.api.ContainerOwner;
import io.github.anjoismysign.blobcontainer.configuration.ContainerConfiguration;
import io.github.anjoismysign.blobcontainer.entity.Container;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandTarget;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum BlobContainerCommand {
    INSTANCE;

    private static final String COMMAND_NAME = "blobcontainer";
    private static final String COMMAND_PERMISSION = "blobcontainer";
    private static final String COMMAND_DESCRIPTION = "Base command for BlobContainer plugin";

    private static final Command COMMAND = BukkitAdapter.getInstance().createCommand(COMMAND_NAME, COMMAND_PERMISSION, COMMAND_DESCRIPTION);

    private static final BlobLibMessageAPI MESSAGE_API = BlobLibMessageAPI.getInstance();

    public void load(){
        CommandTarget<Container> containers = new CommandTarget<>() {
            @Override
            public List<String> get() {
                return ContainerConfiguration.getInstance().getContainersByIdentifier().keySet().stream().toList();
            }

            @Override
            public @Nullable Container parse(String identifier) {
                return ContainerConfiguration.getInstance().getContainersByIdentifier().get(identifier);
            }
        };

        CommandTarget<Player> onlinePlayers = BukkitCommandTarget.ONLINE_PLAYERS();

        Command open = COMMAND.child("open");
        open.setParameters(containers);
        open.onExecute((permissionMessenger, args) -> {
            if (args.length < 1){
                return;
            }
            String identifier = args[0];
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            if (!(sender instanceof Player player)) {
                MESSAGE_API
                        .getMessage("System.Console-Not-Allowed-Command", sender)
                        .toCommandSender(sender);
                return;
            }
            @Nullable ContainerOwner containerOwner = BlobContainerAPI.getContainerOwner(player);
            if (containerOwner == null){
                MESSAGE_API
                        .getMessage("Player.Not-Inside-Plugin-Cache")
                        .toCommandSender(sender);
                return;
            }
            @Nullable Container container = containers.parse(identifier);
            if (container == null){
                MESSAGE_API
                        .getMessage("BlobContainer.Container-Doesnt-Exist")
                        .modder()
                        .replace("%container%", identifier)
                        .get()
                        .toCommandSender(sender);
                return;
            }
            containerOwner.open(container);
        });

        Command openFor = COMMAND.child("openFor");
        openFor.setParameters(containers, onlinePlayers);
        openFor.onExecute((permissionMessenger, args) -> {
            if (args.length < 2) {
                return;
            }
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            String identifier = args[0];
            Player player = onlinePlayers.parse(args[1]);
            if (player == null) {
                MESSAGE_API
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            @Nullable ContainerOwner containerOwner = BlobContainerAPI.getContainerOwner(player);
            if (containerOwner == null){
                MESSAGE_API
                        .getMessage("Player.Not-Inside-Plugin-Cache")
                        .toCommandSender(sender);
                return;
            }
            @Nullable Container container = containers.parse(identifier);
            if (container == null){
                MESSAGE_API
                        .getMessage("BlobContainer.Container-Doesnt-Exist")
                        .modder()
                        .replace("%container%", identifier)
                        .get()
                        .toCommandSender(sender);
                return;
            }
            containerOwner.open(container);
        });
    }
}
