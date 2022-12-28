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

    private String name;
    public KothEvent(KothMessage msg, String name) {
        message = msg;
        winner = null;
        this.name = name;
    }

    public KothEvent(KothMessage msg, String name, Player winner) {
        message = msg;
        this.winner = winner;
        this.name = name;
    }

    public KothMessage getMessageEnum(){
        return message;
    }

    public String getMessageString() {
        return message.toString();
    }

    public String getKothName() {
        return name;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
