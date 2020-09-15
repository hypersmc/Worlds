package me.hypersmc.jumpwatch.worlds.Handler;

import me.hypersmc.jumpwatch.worlds.Events.Join;
import me.hypersmc.jumpwatch.worlds.Main;
import org.bukkit.plugin.java.JavaPlugin;

public class EventHandler {
    static Main main = JavaPlugin.getPlugin(Main.class);

    public static void getevents() {
        main.getServer().getPluginManager().registerEvents(new Join(), main);
    }
}
