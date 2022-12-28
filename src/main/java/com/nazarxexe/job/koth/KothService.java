package com.nazarxexe.job.koth;


import com.nazarxexe.job.koth.action.Countdown;
import com.nazarxexe.job.koth.action.Kothaction;
import com.nazarxexe.job.koth.timer.KothTimer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KothService {

    final String name;
    final Koth plugin;

    boolean status;
    long end;
    long start;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    final Countdown counter;
    final Kothaction action;
    final KothTimer timer;
    final ConfigurationSection config;

    public int getConfigStart() {
        return configStart;
    }

    public int getConfigEnd() {
        return configEnd;
    }

    final int configStart;
    final int configEnd;

    public HashMap<Player, Integer> getPlayersIN() {
        return playersIN;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    final HashMap<Player, Integer> playersIN;

    public String getName() {
        return name;
    }

    public KothService(String name, Koth plugin) {
        this.name = name;
        this.plugin = plugin;
        this.playersIN = new HashMap<>();
        this.counter = new Countdown(this.plugin, this);
        this.action = new Kothaction(this.plugin, this);
        this.timer = new KothTimer(this.plugin, this.action, this);
        this.config = plugin.getConfig().getConfigurationSection(name);

        this.configStart = this.config.getInt("Countdown");
        this.configEnd = this.config.getInt("CountdownWhenKoth");
    }

    public KothService runService()
    {
        counter.runTaskTimerAsynchronously(plugin, 0L, 20L);
        action.runTaskTimerAsynchronously(plugin, 0L, 20L);
        timer.runTaskTimerAsynchronously(plugin, 0L, 20L);
        return this;
    }


}
