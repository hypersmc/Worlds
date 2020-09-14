package me.hypersmc.jumpwatch.worlds;

import me.hypersmc.jumpwatch.worlds.Handler.CommandHandler;
import me.hypersmc.jumpwatch.worlds.Handler.EventHandler;
import me.hypersmc.jumpwatch.worlds.Handler.xs;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Main extends JavaPlugin  implements Listener{
    public static Plugin plugin;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        String str1 = Main.plugin.getConfig().getString("activationKey");


        /*
        Self reminder husk at uncomment den if sætning lige nede under.
         */

        //if (!new xs(str1, "https://zennodes.dk/ActivationKey/verify.php", this).setSecurityKey("TkHr6umrQ8OUPxeWt0ANuXa3zlTopUyF7nYUJaahbZMJAoRZOOzcLjCTG70zVJeDKavfJOC0ilD56Ll6MSV7PFVKkwaMpgwmnk4").reg()) return;
        Bukkit.getServer().getLogger().info( "\n" +
                "░██╗░░░░░░░██╗░█████╗░██████╗░██╗░░░░░██████╗░░██████╗\n" +
                "░██║░░██╗░░██║██╔══██╗██╔══██╗██║░░░░░██╔══██╗██╔════╝\n" +
                "░╚██╗████╗██╔╝██║░░██║██████╔╝██║░░░░░██║░░██║╚█████╗░\n" +
                "░░████╔═████║░██║░░██║██╔══██╗██║░░░░░██║░░██║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░╚█████╔╝██║░░██║███████╗██████╔╝██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░");

        MySQL SQL = new MySQL();
        SQL.setupDatabase();
        CommandHandler.getcommands();
        EventHandler.getevents();
    }

    @Override
    public void onDisable() {
        MySQL SQL = new MySQL();
        Connection conn = MySQL.db;
        if (conn == null)
            return;
        try {
            if (conn.isValid(5) || conn != null)
                SQL.closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
