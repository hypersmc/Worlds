package me.hypersmc.jumpwatch.worlds;

import me.hypersmc.jumpwatch.worlds.Handler.CommandHandler;
import me.hypersmc.jumpwatch.worlds.Handler.EventHandler;
import me.hypersmc.jumpwatch.worlds.Handler.xs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.logging.Logger;

public class Main extends JavaPlugin  implements Listener{
    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&3&lWorlds&7]&r");
    public static Plugin plugin;
    public FileConfiguration config = getConfig();
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info( "\n" + "\n" +
                "░██╗░░░░░░░██╗░█████╗░██████╗░██╗░░░░░██████╗░░██████╗\n" +
                "░██║░░██╗░░██║██╔══██╗██╔══██╗██║░░░░░██╔══██╗██╔════╝\n" +
                "░╚██╗████╗██╔╝██║░░██║██████╔╝██║░░░░░██║░░██║╚█████╗░\n" +
                "░░████╔═████║░██║░░██║██╔══██╗██║░░░░░██║░░██║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░╚█████╔╝██║░░██║███████╗██████╔╝██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░");
        Logger logger = this.getLogger();

        new UpdateChecker(this, 12345).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available.");
            }
        });

        /*
        Self reminder husk at uncomment den if sætning lige nede under.
         */
        //String str1 = Main.plugin.getConfig().getString("activationKey");
        //if (!new xs(str1, "https://zennodes.dk/ActivationKey/verify.php", this).setSecurityKey("TkHr6umrQ8OUPxeWt0ANuXa3zlTopUyF7nYUJaahbZMJAoRZOOzcLjCTG70zVJeDKavfJOC0ilD56Ll6MSV7PFVKkwaMpgwmnk4").reg()) return;


        MySQL SQL = new MySQL();
        SQL.setupDatabase();
        saveDefaultConfig();
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
