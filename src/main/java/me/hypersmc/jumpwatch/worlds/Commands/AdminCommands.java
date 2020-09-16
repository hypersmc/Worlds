package me.hypersmc.jumpwatch.worlds.Commands;

import me.hypersmc.jumpwatch.worlds.Main;
import me.hypersmc.jumpwatch.worlds.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminCommands implements CommandExecutor {
    Main main = JavaPlugin.getPlugin(Main.class);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Sorry but all of the commands for Worlds are Player based. There might come some later one.");
        }else {
            if (cmd.getName().equalsIgnoreCase("WorldsA")) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    sender.sendMessage(main.prefix + "");
                    return true;

                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("help")) {
                        if (args[1].equalsIgnoreCase("1")){
                            sender.sendMessage(main.prefix + "");
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(main.prefix + "");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    main.reloadConfig();
                    sender.sendMessage(main.prefix + "Configuration file reloaded.");
                    return true;
                }
            }
        }
        return true;
    }
}
