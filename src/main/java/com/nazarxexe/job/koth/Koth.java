package com.nazarxexe.job.koth;

import com.nazarxexe.job.koth.action.Countdown;
import com.nazarxexe.job.koth.action.Kothaction;
import com.nazarxexe.job.koth.timer.KothTimer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Koth extends JavaPlugin {


    private static Koth plugin;

    private final List<Papi> placeholders = new ArrayList<>();
    private final List<Kothaction> actions = new ArrayList<>();
    private final List<KothTimer> timers = new ArrayList<>();
    private final List<Countdown> counts = new ArrayList<>();

    public static HashMap<String, Long> kothTimers;
    public static HashMap<String, Long> kothTimerEnds;
    public static HashMap<String, Boolean> kothStats;
    public static HashMap<String, HashMap<Player, Integer>> playerlist = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        this.plugin = this;
        this.kothTimerEnds = new HashMap<>();
        this.kothTimers = new HashMap<>();
        this.kothStats = new HashMap<>();
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
            System.out.println("Registering: " +i);
            ConfigurationSection config = plugin.getConfig().getConfigurationSection(i);
            if (config == null){
                System.out.println("Error loading " + i);
                return;
            }
            if (config.getString("UNIXend").equals("0")) {
                config.set("KothRunning", false);
                config.set("UNIXKothEND", String.valueOf(System.currentTimeMillis() + Long.valueOf(config.getString("CountdownWhenKoth")) * 1000 + Long.valueOf(config.getLong("Countdown")) * 1000));
                config.set("UNIXend", String.valueOf(System.currentTimeMillis() + Long.valueOf(config.getLong("Countdown") * 1000)));
                plugin.saveConfig();
                plugin.reloadConfig();
            }


            kothTimers.put(i, Long.valueOf(config.getString("UNIXend")));
            kothTimerEnds.put(i, Long.valueOf(config.getString("UNIXKothEND")));
            kothStats.put(i, config.getBoolean("KothRunning"));


            placeholders.add(new Papi(this, i));
            playerlist.put(i, new HashMap<>());
            Kothaction c = new Kothaction(this, i);
            actions.add(c);
            counts.add(new Countdown(this, i));
            timers.add(new KothTimer(this, c, i));
        }

        placeholders.forEach((p) -> { p.register(); });
        actions.forEach((a) -> { a.runTaskTimerAsynchronously(this, 0L, 20L); });
        timers.forEach((t) -> { t.runTaskTimerAsynchronously(this, 0L, 20L); });
        counts.forEach((c) -> { c.runTaskTimerAsynchronously(this, 0L, 20L); });

        getCommand("koth").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                switch (args[0])
                {
                    case "start" -> {
                        kothTimers.replace(args[1], 0L);
                    }
                    case "stop" -> {
                        kothTimerEnds.replace(args[1], 0L);
                    }
                    case "getp" -> {
                        for (Player i : playerlist.get(args[1]).keySet()){
                            sender.sendMessage(i.getName());
                        }
                    }
                    case "test" -> {
                        sender.sendMessage(actions.size() + "\n" + timers.size() + "\n" + counts.size());
                    }
                }

                return true;
            }
        });

    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        placeholders.forEach(PlaceholderExpansion::unregister);
        actions.forEach(BukkitRunnable::cancel);
        timers.forEach(BukkitRunnable::cancel);
        counts.forEach(BukkitRunnable::cancel);
        placeholders.clear();
        actions.clear();
        timers.clear();
        for (String i : plugin.getConfig().getKeys(false)){
            plugin.getConfig().getConfigurationSection(i).set("UNIXend", kothTimers.get(i));
            plugin.getConfig().getConfigurationSection(i).set("UNIXKothEND", kothTimerEnds.get(i));
            plugin.getConfig().getConfigurationSection(i).set("KothRunning", kothStats.get(i));
        }
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
