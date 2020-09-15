package me.hypersmc.jumpwatch.worlds.Commands;

import me.hypersmc.jumpwatch.worlds.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class AdminCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Sorry but all of the commands for Worlds are Player based. There might come some later one.");
        }else {
            if (cmd.getName().equalsIgnoreCase("WorldsA")) {
                if (args.length == 0) {
                    //stuff
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("help")) {
                        if (args[1].equalsIgnoreCase("1")){

                        }
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {

                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Main.plugin.reloadConfig();
                    sender.sendMessage("yeet reloaded.");
                }
            }
        }
        return true;
    }
}
