package me.hypersmc.jumpwatch.worlds;

import me.hypersmc.jumpwatch.worlds.Handler.CommandHandler;
import me.hypersmc.jumpwatch.worlds.Handler.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin  implements Listener{
    public static Plugin plugin;

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info( "\n" +
                "░██╗░░░░░░░██╗░█████╗░██████╗░██╗░░░░░██████╗░░██████╗\n" +
                "░██║░░██╗░░██║██╔══██╗██╔══██╗██║░░░░░██╔══██╗██╔════╝\n" +
                "░╚██╗████╗██╔╝██║░░██║██████╔╝██║░░░░░██║░░██║╚█████╗░\n" +
                "░░████╔═████║░██║░░██║██╔══██╗██║░░░░░██║░░██║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░╚█████╔╝██║░░██║███████╗██████╔╝██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        MySQL SQL = new MySQL();
        SQL.setupDatabase();
        CommandHandler.getcommands();
        EventHandler.getevents();
    }

    @Override
    public void onDisable() {

    }

}
