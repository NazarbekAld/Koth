package com.nazarxexe.job.koth.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerInKothEvent extends Event {

    Player player;

    public PlayerInKothEvent(Player player) {
        this.player = player;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer () {
        return player;
    }


}
