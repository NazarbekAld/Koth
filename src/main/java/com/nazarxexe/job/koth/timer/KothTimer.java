package com.nazarxexe.job.koth.timer;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.action.Kothaction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import static com.nazarxexe.job.koth.Koth.kothStats;
import static com.nazarxexe.job.koth.Koth.kothTimers;


public class KothTimer extends BukkitRunnable {
    Koth plugin;

    final Kothaction action;
    final String name;
    ConfigurationSection config;


    public KothTimer(Koth p, Kothaction action, String name){
        this.plugin = p;
        this.action = action;
        this.name = name;
        this.config = this.plugin.getConfig().getConfigurationSection(name);

    }

    @Override
    public void run() {

        if (kothStats.get(name)){
            return;
        }

        long END = kothTimers.get(name);
        long CURRENTTIME = System.currentTimeMillis();

        if (END < CURRENTTIME){
            kothStats.replace(name, true);
            action.setup();
            return;
        }

    }
}
