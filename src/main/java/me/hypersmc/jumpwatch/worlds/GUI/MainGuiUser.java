package me.hypersmc.jumpwatch.worlds.GUI;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MainGuiUser implements Listener {
    //Gui's
    Gui gui = new Gui(6, "yeet");
    Gui gui2 = new Gui(3, "yeet2");
    //Fillitem
    GuiItem Fillitem = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).setName("").asGuiItem(event -> {
        // Handle your click action here
        event.setCancelled(true);
    });

    //Gui items for main
    GuiItem guiItem = ItemBuilder.from(Material.STONE).setName("dw").asGuiItem(event -> {
        // Handle your click action here
        event.setCancelled(true);
        gui2.open(event.getWhoClicked());
    });
    //Gui items for

    public void maingui(Player player) {
        gui.open(player);
        for (int i = 0; i < 53; i++) {
            gui.setItem(i, Fillitem);
        }
        gui.setItem(10, guiItem);
        gui.setItem(16, guiItem);
    }


}
