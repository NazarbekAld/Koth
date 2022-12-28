package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.event.PlayerInKothEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.nazarxexe.job.koth.Koth.*;

public class Countdown extends BukkitRunnable {

    final Koth plugin;
    final String name;

    final ConfigurationSection config;

    public Countdown(Koth plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.config = plugin.getConfig().getConfigurationSection(name);
    }

    @Override
    public void run() {

        if (!(plugin.getConfig().getBoolean("KothRunning"))) {
            playerlist.get(name).clear();
            return;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()){

            if (plugin.getServer().getOnlinePlayers().isEmpty()){
                System.out.println("NOBODY IN THE SERVER!!");
                break;
            }

            if (PlaceholderAPI.setPlaceholders(player, "%worldguard_region_name%").equalsIgnoreCase(config.getString("Region"))){
                playerlist.get(name).putIfAbsent(player, 1);
                playerlist.get(name).replace(player, playerlist.get(name).get(player)+1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getServer().getPluginManager().callEvent(new PlayerInKothEvent(player));
                    }
                }.runTask(plugin);
            }else {
                if (playerlist.get(name).get(player) == null)
                    return;
                playerlist.get(name).remove(player);
            }
        }
    }
}
