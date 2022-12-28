package com.nazarxexe.job.koth;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.configuration.ConfigurationSection;
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

    final KothService service;




    public Papi(Koth plugin, String name, KothService service) {
        this.plugin = plugin;
        this.name = name;
        this.service = service;
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
                if (!(service.isStatus())){
                    ret = String.valueOf((service.getEnd() - System.currentTimeMillis()/1000));
                }
                ret = String.valueOf((service.getStart() - System.currentTimeMillis())/1000);
            }
            case "king" -> {
                ret = getWinner();
            }
            case "status" ->{
                ret = service.isStatus() ? "active" : "non-active";
            }
            case "player_countdown" -> {
                if (!(service.isStatus())){
                    ret="-1";
                }
                ret = String.valueOf(service.getPlayersIN().get(p));
            }
            case "time" -> {
                long seconds;
                if (!(service.isStatus())){
                    seconds = ((service.getEnd() - System.currentTimeMillis()/1000));
                }
                seconds = ((service.getStart() - System.currentTimeMillis())/1000);
                ret = calculateTime(seconds);
            }
            case "time_stamp" -> {
                ret = "" + service.getStart() + "\n" + service.getEnd() + "\n" + (service.getStart() - System.currentTimeMillis()) + "\n" + (service.getEnd() - System.currentTimeMillis());
            }
        }


        return ret;
    }
    private String getWinner ()
    {
        if (service.getPlayersIN().isEmpty()){
            return "";
        }
        Collection<Integer> l = service.getPlayersIN().values();
        int w = Collections.max(l);
        return getKeyByValue(service.getPlayersIN(), w).getName();
    }

    private String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);

        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                (TimeUnit.SECONDS.toHours(seconds)* 60);

        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return "" + day + ":" + hours + ":" + minute +
                ":" + second;
    }
}
