package com.nazarxexe.job.koth;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.nazarxexe.job.koth.Koth.*;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class Papi extends PlaceholderExpansion {

    final Koth plugin;
    final String name;
    final ConfigurationSection config;



    public Papi(Koth plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.config = this.plugin.getConfig().getConfigurationSection(name);
    }

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {

        return true;
    }

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "NazarbekAlda";
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return name;
    }

    /**
     * if an expansion requires another plugin as a dependency, the proper name of the dependency should
     * go here. Set this to null if your placeholders do not require another plugin be installed on the server
     * for them to work
     */

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        String ret = "";
        // %koth_timeleft%
        switch (identifier) {
            case "timeleft" -> {
                if (!(config.getBoolean("KothRunning"))){
                    ret = String.valueOf((Long.valueOf(config.getString("UNIXKothEND")) - System.currentTimeMillis())/1000);
                }
                ret = String.valueOf((Long.valueOf(config.getString("UNIXend")) - System.currentTimeMillis())/1000);
            }
            case "king" -> {
                ret = getWinner();
            }
            case "status" ->{
                ret = config.getBoolean("KothRunning") ? "active" : "non-active";
            }
            case "player_countdown" -> {
                if (!(config.getBoolean("KothRunning"))){
                    ret="-1";
                }
                ret = String.valueOf(playerlist.get(name).get(p));
            }
            case "time" -> {
                long seconds;
                if (!(config.getBoolean("KothRunning"))){
                    seconds = ((Long.valueOf(config.getString("UNIXKothEND")) - System.currentTimeMillis())/1000);
                }
                seconds = (Long.valueOf(config.getString("UNIXend")) - System.currentTimeMillis())/1000;
                ret = calculateTime(seconds);
            }
        }


        return ret;
    }
    private String getWinner ()
    {
        if (playerlist.get(name).isEmpty()){
            return "";
        }
        Collection<Integer> l = playerlist.get(name).values();
        int w = Collections.max(l);
        return getKeyByValue(playerlist.get(name), w).getName();
    }

    private String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);

        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                (TimeUnit.SECONDS.toHours(seconds)* 60);

        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return "" + day + " :" + hours + " :" + minute +
                " :" + second;
    }
}
