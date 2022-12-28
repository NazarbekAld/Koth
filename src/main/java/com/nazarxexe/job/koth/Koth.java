package com.nazarxexe.job.koth;

import com.nazarxexe.job.koth.action.Countdown;
import com.nazarxexe.job.koth.action.Kothaction;
import com.nazarxexe.job.koth.timer.KothTimer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Koth extends JavaPlugin {


    private static Koth plugin;

    private List<Papi> placeholders;
    private List<Kothaction> actions;
    private List<KothTimer> timers;
    public static HashMap<String, HashMap<Player, Integer>> playerlist = new HashMap<>();

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

        if (getConfig().getString("UNIXend").equals("0")){
            getConfig().set("UNIXKothEND", String.valueOf(System.currentTimeMillis() +Long.valueOf(getConfig().getString("Countdown"))*1000 + Long.valueOf(getConfig().getLong("CountdownWhenKoth"))*1000));
            getConfig().set("UNIXend", String.valueOf(System.currentTimeMillis() +Long.valueOf(getConfig().getString("Countdown"))*1000));
            saveConfig();
            reloadConfig();
            System.out.println("Data updated.");

        }

        for (String i : plugin.getConfig().getKeys(false)) {
            placeholders.add(new Papi(this, i));
            playerlist.put(i, new HashMap<>());
            Kothaction c = new Kothaction(this, i);
            actions.add(c);
            timers.add(new KothTimer(this, c, i));
        }

        actions.forEach((a) -> { a.runTaskTimerAsynchronously(this, 0L, 20L); });
        timers.forEach((t) -> { t.runTaskTimerAsynchronously(this, 0L, 20L); });

        getCommand("koth").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

                switch (args[0]){
                    case "start" -> {
                        getConfig().set("UNIXend", "0");
                        sender.sendMessage("Starting up...");
                    }
                    case "force_stop" -> {
                        sender.sendMessage("Stopping...");
                        getServer().getPluginManager().disablePlugin(plugin);
                    }
                    case "time" -> {
                        sender.sendMessage(String.valueOf(System.currentTimeMillis()));
                    }
                }


                return true;
            }
        });
        getCommand("koth").setTabCompleter(new TabCompleter() {
            @Override
            public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                ArrayList<String> tab = new ArrayList<>();

                tab.add("start");
                tab.add("force_stop");
                tab.add("time");
                return tab;
            }
        });


    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
