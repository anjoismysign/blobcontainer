package io.github.anjoismysign.blobcontainer.entity;

import io.github.anjoismysign.blobcontainer.BlobContainer;
import io.github.anjoismysign.blobcontainer.api.ContainerOwner;
import io.github.anjoismysign.blobcontainer.configuration.ContainerConfiguration;
import io.github.anjoismysign.blobcontainer.event.ContainerProfileLoadEvent;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.entities.PlayerDecorator;
import io.github.anjoismysign.bloblib.entities.PlayerDecoratorAware;
import io.github.anjoismysign.bloblib.entities.inventory.BlobInventory;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.psa.PostLoadable;
import io.github.anjoismysign.psa.PreUpdatable;
import io.github.anjoismysign.psa.crud.Crudable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainerProfile implements Crudable, PlayerDecoratorAware, PostLoadable, PreUpdatable, ContainerOwner {
    private final @NotNull String identification;
    private final @NotNull Map<String, String> serializedContainers;

    private transient PlayerDecorator playerDecorator;
    private transient Map<String, Map<Integer, @Nullable ItemStack>> live;

    private BlobContainer plugin(){
        return BlobContainer.getInstance();
    }

    public ContainerProfile(@NotNull String identification) {
        this.identification = identification;
        this.serializedContainers = new HashMap<>();
        onPostLoad();
    }

    @Override
    public void onPostLoad() {
        live = new HashMap<>();
        Map<String, Container> containers = ContainerConfiguration.getInstance().getContainersByIdentifier();
        Set<String> remove = serializedContainers.keySet().stream()
                .filter(identifier-> !containers.containsKey(identifier))
                .collect(Collectors.toSet());
        Map.copyOf(serializedContainers).forEach((identifier, base64)->{
            if (remove.contains(identifier)){
                serializedContainers.remove(identifier);
                return;
            }
            live.put(identifier, SerialContainer.deserialize(base64));
        });
        containers.values().forEach(container -> {
            String identifier = container.getIdentifier();
            if (live.containsKey(identifier)){
                return;
            }
            live.put(identifier, new HashMap<>());
        });
    }

    @Override
    public void onPreUpdate() {
        live.forEach((identifier, contents)->{
            serializedContainers.put(identifier, SerialContainer.serialize(contents));
        });
    }

    @Override
    public void setPlayerDecorator(@NotNull PlayerDecorator playerDecorator) {
        this.playerDecorator = playerDecorator;
        Runnable syncRunnable = () -> {
            @Nullable var player = getPlayer();
            if (player == null){
                return;
            }
            ContainerProfileLoadEvent loadEvent = new ContainerProfileLoadEvent(player);
            Bukkit.getPluginManager().callEvent(loadEvent);
        };
        if (Bukkit.isPrimaryThread()){
            plugin().getLogger().info("setPlayerDecorator on primary thread");
            syncRunnable.run();
        } else {
            plugin().getLogger().info("setPlayerDecorator on async thread");
            Bukkit.getScheduler().runTask(plugin(), syncRunnable);
        }
    }

    @Override
    public @NotNull String getIdentification() {
        return identification;
    }

    @Nullable
    public Player getPlayer() {
        return playerDecorator == null ? null : playerDecorator.lookup();
    }

    public void open(Container container){
        @Nullable Player player = getPlayer();
        if (player == null){
            return;
        }
        String identifier = container.getIdentifier();
        String blobInventory = container.getBlobInventory();
        String button = container.getButton();
        BlobInventory inventory = BlobLibInventoryAPI.getInstance().trackInventory(player, blobInventory).getInventory();
        Map<Integer, @Nullable ItemStack> contents = live.get(identifier);
        String locale = player.getLocale();
        contents.values().forEach(itemStack -> TranslatableItem.localize(itemStack, locale));
        inventory.setContents(button, contents);
        inventory.open(player);
    }

    public void update(String identifier, Map<Integer, @Nullable ItemStack> contents){
        live.put(identifier, contents);
    }
}
