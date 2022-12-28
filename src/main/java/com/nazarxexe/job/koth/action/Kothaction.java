package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.event.KothEvent;
import com.nazarxexe.job.koth.event.KothMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.nazarxexe.job.koth.Koth.*;

public class Kothaction extends BukkitRunnable {

    final Koth plugin;

    final MiniMessage MM;

    final String name;
    ConfigurationSection config;

    public Kothaction(Koth plugin, String name) {
        this.plugin = plugin;
        this.name =name;
        this.config = plugin.getConfig().getConfigurationSection(name);
        MM = MiniMessage.miniMessage();
    }

    @Override
    public void run() {
        long END = Long.valueOf(config.getString("UNIXKothEND"));
        long CURRENTTIME =System.currentTimeMillis();

        if (!(plugin.getConfig().getBoolean("KothRunning"))){
            return;
        }

        if (END < CURRENTTIME) {

            if (playerlist.get(name).isEmpty()) {
                for (String i : plugin.getConfig().getStringList("BroadcastNobody")){
                    plugin.getServer().broadcast(MM.deserialize(i));
                }
                System.out.println(playerlist.get(name).toString());


                config.set("KothRunning", false);
                config.set("UNIXKothEND", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getString("CountdownWhenKoth")) * 1000 + Long.valueOf(plugin.getConfig().getLong("Countdown")) * 1000));
                config.set("UNIXend", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getLong("Countdown") * 1000)));
                plugin.saveConfig();
                plugin.reloadConfig();
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.NON_WINNER, name));
                    }
                }.runTask(plugin);
                return;
            }
            Player W = getWinner();


            for (String e : config.getStringList("BroadcastWinner")){

                plugin.getServer().broadcast(MM.deserialize(PlaceholderAPI.setPlaceholders(W, e)));
            }
            for (String s : config.getStringList("RewardCommands")){
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(W, s));
                    }
                }.runTask(plugin);
            }
            config.set("UNIXKothEND", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getString("CountdownWhenKoth")) * 1000 + Long.valueOf(plugin.getConfig().getLong("Countdown")) * 1000));
            config.set("UNIXend", String.valueOf(System.currentTimeMillis() + Long.valueOf(plugin.getConfig().getLong("Countdown") * 1000)));
            config.set("KothRunning", false);
            plugin.saveConfig();
            plugin.reloadConfig();

            new BukkitRunnable(){

                @Override
                public void run() {
                    plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.WINNER, name, W));
                }
            }.runTask(plugin);
        }

    }

    private Player getWinner ()
    {
        Collection<Integer> l = playerlist.get(name).values();
        int w = Collections.max(l);
        return getKeyByValue(playerlist.get(name), w);
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
                        plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.START, name));
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
        return this;
    }

}
