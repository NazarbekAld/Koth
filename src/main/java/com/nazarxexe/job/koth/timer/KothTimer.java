package com.nazarxexe.job.koth.timer;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.action.Kothaction;
import org.bukkit.scheduler.BukkitRunnable;

import static com.nazarxexe.job.koth.Koth.getAction;

public class KothTimer extends BukkitRunnable {
    Koth plugin;
    public KothTimer(Koth p){
        this.plugin = p;
    }

    @Override
    public void run() {

        if (plugin.getConfig().getBoolean("KothRunning")){
            return;
        }

        long END = Long.valueOf(plugin.getConfig().getString("UNIXend"));
        long CURRENTTIME = System.currentTimeMillis();

        if (END < CURRENTTIME){
            plugin.getConfig().set("KothRunning", true);
            getAction().setup();
            plugin.saveConfig();
            plugin.reloadConfig();
            return;
        }

    }
}
