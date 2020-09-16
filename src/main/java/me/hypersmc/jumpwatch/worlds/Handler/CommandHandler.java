package me.hypersmc.jumpwatch.worlds.Handler;

import me.hypersmc.jumpwatch.worlds.Commands.AdminCommands;
import me.hypersmc.jumpwatch.worlds.Main;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements Listener {
    static Main main = JavaPlugin.getPlugin(Main.class);

    public static void getcommands() {
        main.getCommand("WorldsA").setExecutor(new AdminCommands());

    }
}
