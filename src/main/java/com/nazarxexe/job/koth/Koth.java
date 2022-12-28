package com.nazarxexe.job.koth;

import com.nazarxexe.job.koth.action.Countdown;
import com.nazarxexe.job.koth.action.Kothaction;
import com.nazarxexe.job.koth.timer.KothTimer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Koth extends JavaPlugin {


    private static Koth plugin;

    private final List<Papi> placeholders = new ArrayList<>();
    private final List<KothService> services = new ArrayList<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        this.plugin = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            System.out.println("Started UP!");
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            System.out.println("Placeholder API required");
            getServer().getPluginManager().disablePlugin(plugin);
        }

        for (String i : plugin.getConfig().getKeys(false)) {
            KothService s = new KothService(i, this)
                    .runService();
            services.add(s);
            placeholders.add(new Papi(this, i, s));
        }

        placeholders.forEach((p) -> { p.register(); });
        getCommand("koth").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                switch (args[0])
                {
                    case "start" -> {
                        services.forEach((s) -> {
                            if (s.getName().equals(args[1])){
                                s.setStart(0);
                            }
                        });
                    }
                    case "stop" -> {
                        services.forEach((s) -> {
                            if (s.getName().equals(args[1])){
                                s.setEnd(0);
                            }
                        });
                    }
                }

                return true;
            }
        });

    }



    @Override
    public void onDisable() {
    }

    public static Koth getINS () {
        return plugin;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


}
