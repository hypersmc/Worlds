package me.hypersmc.jumpwatch.worlds.Events;

import me.hypersmc.jumpwatch.worlds.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Join implements Listener {
    Main main = JavaPlugin.getPlugin(Main.class);
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerJoined(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPlayedBefore()) {
            p.sendTitle("" + ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("welcomebackmessage").replace("%player%",String.valueOf(p.getName())) ), ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("welcomebacksubmessage")), 2, main.getConfig().getInt("waitime"), 2);
        }else {
            p.sendTitle("" + ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("welcomemessage").replace("%player%",String.valueOf(p.getName())) ), ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("welcomesubmessage")), 2, main.getConfig().getInt("waitime"), 2);
        }
    }

}
