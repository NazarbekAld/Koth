package com.nazarxexe.job.koth.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;



public class KothEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private KothMessage message;

    public Player getWinner() {
        return winner;
    }

    private Player winner;

    public KothEvent(KothMessage msg) {
        message = msg;
        winner = null;
    }

    public KothEvent(KothMessage msg, Player winner) {
        message = msg;
        this.winner = winner;
    }

    public KothMessage getMessageEnum(){
        return message;
    }

    public String getMessageString() {
        return message.toString();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
