package me.hypersmc.jumpwatch.worlds.Commands;

import me.hypersmc.jumpwatch.worlds.GUI.MainGuiUser;
import me.hypersmc.jumpwatch.worlds.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UserCommands extends MainGuiUser implements CommandExecutor  {
    Main main = JavaPlugin.getPlugin(Main.class);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(main.prefix + " This is a user side only command!");
        }else {
            if (cmd.getName().equalsIgnoreCase("Worlds")) {
                if (args.length == 0){
                    Player player = (Player) sender;
                    maingui(player);
                }
            }
        }
        return true;
    }
}
