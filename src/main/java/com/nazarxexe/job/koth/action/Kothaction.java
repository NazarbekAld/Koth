package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.KothService;
import com.nazarxexe.job.koth.event.KothEvent;
import com.nazarxexe.job.koth.event.KothMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.nazarxexe.job.koth.Koth.*;

public class Kothaction extends BukkitRunnable {

    final Koth plugin;

    final MiniMessage MM;
    final KothService service;

    final String name;

    public Kothaction(Koth plugin, KothService service) {
        this.plugin = plugin;
        this.service = service;
        this.name = service.getName();

        MM = MiniMessage.miniMessage();
    }

    @Override
    public void run() {
        long END = service.getEnd();
        long CURRENTTIME =System.currentTimeMillis();

        if (!service.isStatus()){
            return;
        }

        if (END < CURRENTTIME) {

            if (service.getPlayersIN().isEmpty()) {
                for (String i : service.getConfig().getStringList("BroadcastNobody")){
                    plugin.getServer().broadcast(MM.deserialize(i));
                }
                System.out.println(service.getPlayersIN().toString());


                service.setStatus(false);
                service.setEnd(System.currentTimeMillis() + service.getConfigStart() * 1000L + service.getConfigEnd() * 1000L);
                service.setStart(System.currentTimeMillis() + service.getConfigStart() * 1000L);
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().getPluginManager().callEvent(new KothEvent(KothMessage.NON_WINNER, name));
                    }
                }.runTask(plugin);
                return;
            }
            Player W = getWinner();


            for (String e : service.getConfig().getStringList("BroadcastWinner")){

                plugin.getServer().broadcast(MM.deserialize(PlaceholderAPI.setPlaceholders(W, e)));
            }
            for (String s : service.getConfig().getStringList("RewardCommands")){
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(W, s));
                    }
                }.runTask(plugin);
            }
            service.setStatus(false);
            service.setEnd(System.currentTimeMillis() + service.getConfigStart() * 1000L + service.getConfigEnd() * 1000L);
            service.setStart(System.currentTimeMillis() + service.getConfigStart() * 1000L);

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
        Collection<Integer> l = service.getPlayersIN().values();
        int w = Collections.max(l);
        return getKeyByValue(service.getPlayersIN(), w);
    }

    public Kothaction setup () {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (String s : service.getConfig().getStringList("BroadcastStart")){
                    plugin.getServer().broadcast(MM.deserialize(String.format(s, name)).asComponent());
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
