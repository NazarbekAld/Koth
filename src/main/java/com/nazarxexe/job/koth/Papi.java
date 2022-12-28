package com.nazarxexe.job.koth;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

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
        return "koth_"+name;
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
                ret = String.valueOf((Long.valueOf(config.getString("UNIXend")) - System.currentTimeMillis())/1000);
            }
            case "king" -> {
                ret = getWinner();
            }
            case "status" ->{
                ret = config.getBoolean("KothRunning") ? "active" : "non-active";
            }
            case "player_countdown" -> {
                if (!(plugin.getConfig().getBoolean("KothRunning"))){
                    ret="-1";
                }
                ret = String.valueOf(playerlist.get(name).get(p));
            }
            case "time" -> {

                int seconds = (int) ((Integer.valueOf(config.getString("UNIXend")) - System.currentTimeMillis())/1000);

                int p1 = seconds % 60;
                int p2 = seconds / 60;
                int p3 = p2 % 60;

                p2 = p2 / 60;
                ret = String.format("%s:%s:%s", p2,p3,p1);
            }
            default -> {
                ret = "Invalid placeholder!";
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
}
