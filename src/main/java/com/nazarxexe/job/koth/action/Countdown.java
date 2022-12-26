package com.nazarxexe.job.koth.action;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.event.PlayerInKothEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static com.nazarxexe.job.koth.Koth.*;

public class Countdown extends BukkitRunnable {

    final Koth plugin;

    public Countdown(Koth plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (!(plugin.getConfig().getBoolean("KothRunning"))) {
            playersIN.clear();
            return;
        }
        for (Player player : plugin.getServer().getOnlinePlayers()){

            if (plugin.getServer().getOnlinePlayers().isEmpty()){
                System.out.println("NOBODY IN THE SERVER!!");
                break;
            }

            if (PlaceholderAPI.setPlaceholders(player, "%worldguard_region_name%").equalsIgnoreCase(plugin.getConfig().getString("Region"))){
                if (playersIN.get(player) == null){
                    playersIN.put(player, 1);
                }
                playersIN.replace(player, playersIN.get(player)+1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getServer().getPluginManager().callEvent(new PlayerInKothEvent(player));
                    }
                }.runTask(plugin);
            }else {
                if (playersIN.get(player) == null)
                    return;
                playersIN.remove(player);
            }
        }
    }
}
