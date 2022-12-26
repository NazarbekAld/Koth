package com.nazarxexe.job.koth;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

import static com.nazarxexe.job.koth.Koth.getKeyByValue;
import static com.nazarxexe.job.koth.Koth.playersIN;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class Papi extends PlaceholderExpansion {

    final Koth plugin;

    public Papi(Koth plugin) {
        this.plugin = plugin;
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
        return "koth";
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
                ret = String.valueOf((Long.valueOf(plugin.getConfig().getString("UNIXend")) - System.currentTimeMillis())/1000);
            }
            case "king" -> {
                ret = getWinner();
            }
            case "status" ->{
                ret = plugin.getConfig().getBoolean("KothRunning") ? "active" : "non-active";
            }
            case "player_countdown" -> {
                if (!(plugin.getConfig().getBoolean("KothRunning"))){
                    ret="-1";
                }
                ret = String.valueOf(playersIN.get(p));
            }
            default -> {
                ret = "Invalid placeholder!";
            }
        }


        return ret;
    }
    private String getWinner ()
    {
        if (playersIN.isEmpty()){
            return "";
        }
        Collection<Integer> l = playersIN.values();
        int w = Collections.max(l);
        return getKeyByValue(playersIN, w).getName();
    }
}
