package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.KothService;
import com.nazarxexe.job.koth.event.PlayerInKothEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.nazarxexe.job.koth.Koth.*;

public class Countdown extends BukkitRunnable {

    final Koth plugin;
    final KothService service;


    public Countdown(Koth plugin, KothService service) {
        this.service = service;
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (!(service.isStatus())) {
            service.getPlayersIN().clear();
            return;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()){

            if (plugin.getServer().getOnlinePlayers().isEmpty()){
                System.out.println("NOBODY IN THE SERVER!!");
                break;
            }

            if (PlaceholderAPI.setPlaceholders(player, "%worldguard_region_name%").equalsIgnoreCase(service.getConfig().getString("Region"))){
                if (service.getPlayersIN().get(player) == null){
                    service.getPlayersIN().put(player, 1);
                    return;
                }
                service.getPlayersIN().replace(player, service.getPlayersIN().get(player)+1);
                new BukkitRunnable() {
                    @Override
                    public void run()
                    {
                        player.getServer().getPluginManager().callEvent(new PlayerInKothEvent(player));
                    }
                }.runTask(plugin);
            } else {
                if (service.getPlayersIN().get(player) == null)
                    return;
                service.getPlayersIN().remove(player);
            }
        }
    }
}
