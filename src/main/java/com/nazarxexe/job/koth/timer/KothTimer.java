package com.nazarxexe.job.koth.timer;

import com.nazarxexe.job.koth.Koth;
import com.nazarxexe.job.koth.KothService;
import com.nazarxexe.job.koth.action.Kothaction;
import org.bukkit.scheduler.BukkitRunnable;



public class KothTimer extends BukkitRunnable {
    Koth plugin;

    final Kothaction action;

    final KothService service;


    public KothTimer(Koth p, Kothaction action, KothService service){
        this.plugin = p;
        this.action = action;

        this.service = service;
    }

    @Override
    public void run() {

        if (service.isStatus()){
            return;
        }

        long END = service.getStart();
        long CURRENTTIME = System.currentTimeMillis();

        if (END < CURRENTTIME){
            service.setStatus(true);
            action.setup();
            return;
        }

    }
}
