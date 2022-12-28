package com.nazarxexe.job.koth.timer;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.action.Kothaction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;


public class KothTimer extends BukkitRunnable {
    Koth plugin;

    final Kothaction action;
    final String name;
    ConfigurationSection config;


    public KothTimer(Koth p, Kothaction action, String name){
        this.plugin = p;
        this.action = action;
        this.name = name;
        this.plugin.getConfig().getConfigurationSection("config");

    }

    @Override
    public void run() {

        if (plugin.getConfig().getBoolean("KothRunning")){
            return;
        }

        long END = Long.valueOf(plugin.getConfig().getString("UNIXend"));
        long CURRENTTIME = System.currentTimeMillis();

        if (END < CURRENTTIME){
            config.set("KothRunning", true);
            action.setup();
            plugin.saveConfig();
            plugin.reloadConfig();
            return;
        }

    }
}
