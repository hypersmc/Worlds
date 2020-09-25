package me.hypersmc.jumpwatch.worlds.GUI;

import me.hypersmc.jumpwatch.worlds.Main;
import me.hypersmc.jumpwatch.worlds.WorldEditside.Schematicpaste;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;

public class MainGuiUser extends Schematicpaste implements Listener {
    //Gui's
    Gui gui = new Gui(6, "yeet");
    Gui gui2 = new Gui(3, "yeet2");
    //Fillitem
    GuiItem Fillitem = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.DARK_PURPLE + "").asGuiItem(event -> {
        // Handle your click action here
        event.setCancelled(true);
    });

    //Gui items for main
    GuiItem guiItem = ItemBuilder.from(Material.STONE).setName(ChatColor.DARK_PURPLE + "test").asGuiItem(event -> {
        // Handle your click action here
        event.setCancelled(true);
        File file = new File("plugins/Worlds/schematics/medieval-smithy.schematic");
        loadschem(file);
        double x = event.getWhoClicked().getLocation().getX();
        double y = event.getWhoClicked().getLocation().getY();
        double z = event.getWhoClicked().getLocation().getZ();
        World world = event.getWhoClicked().getLocation().getWorld();
        pasteschem(x, y, z, world);
    });

    //Gui items for

    public void maingui(Player player) {
        gui.open(player);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, Fillitem);
        }
        gui.setItem(11, guiItem);
        gui.setItem(15, guiItem);
    }


}
