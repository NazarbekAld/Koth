package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.event.KothEvent;
import com.nazarxexe.job.koth.event.KothMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.nazarxexe.job.koth.Koth.getKeyByValue;
import static com.nazarxexe.job.koth.Koth.playersIN;

public class Kothaction extends BukkitRunnable {

    final Koth plugin;

    MiniMessage MM;

    public Kothaction(Koth plugin) {
        this.plugin = plugin;
        MM = MiniMessage.miniMessage();
    }

    @Override
    public void run() {
        long END = Long.valueOf(plugin.getConfig().getString("UNIXKothEND"));
        long CURRENTTIME =System.currentTimeMillis();

        if (!(plugin.getConfig().getBoolean("KothRunning"))){
            return;
        }

        if (END < CURRENTTIME) {

            if (playersIN.isEmpty()) {
                for (String i : plugin.getConfig().getStringList("BroadcastNobody")){
                    plugin.getServer().broadcast(MM.deserialize(i));
                }
                System.out.println(playersIN.toString());


                plugin.getConfig().set("KothRunning", false);
                plugin.getConfig().set("UNIXKothEND", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getString("CountdownWhenKoth")) * 1000 + Long.valueOf(plugin.getConfig().getLong("Countdown")) * 1000));
                plugin.getConfig().set("UNIXend", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getLong("Countdown") * 1000)));
                plugin.saveConfig();
                plugin.reloadConfig();
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.NON_WINNER));
                    }
                }.runTask(plugin);
                return;
            }
            Player W = getWinner();


            for (String e : plugin.getConfig().getStringList("BroadcastWinner")){

                plugin.getServer().broadcast(MM.deserialize(PlaceholderAPI.setPlaceholders(W, e)));
            }
            for (String s : plugin.getConfig().getStringList("RewardCommands")){
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(W, s));
                    }
                }.runTask(plugin);
            }
            plugin.getConfig().set("UNIXKothEND", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getString("CountdownWhenKoth")) * 1000 + Long.valueOf(plugin.getConfig().getLong("Countdown")) * 1000));
            plugin.getConfig().set("UNIXend", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getLong("Countdown") * 1000)));
            plugin.getConfig().set("KothRunning", false);
            plugin.saveConfig();
            plugin.reloadConfig();

            new BukkitRunnable(){

                @Override
                public void run() {
                    plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.WINNER, W));
                }
            }.runTask(plugin);
        }

    }

    private Player getWinner ()
    {
        Collection<Integer> l = playersIN.values();
        int w = Collections.max(l);
        return getKeyByValue(playersIN, w);
    }

    public Kothaction setup () {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (String s : plugin.getConfig().getStringList("BroadcastStart")){
                    plugin.getServer().broadcast(MM.deserialize(s).asComponent());
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.START));
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
        return this;
    }

}
