package io.github.anjoismysign.blobcontainer.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ContainerProfileLoadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    private final Player player;

    public ContainerProfileLoadEvent(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
