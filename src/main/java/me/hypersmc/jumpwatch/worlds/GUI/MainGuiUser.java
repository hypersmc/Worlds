package me.hypersmc.jumpwatch.worlds.GUI;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class MainGuiUser implements Listener {
    Gui gui = new Gui(3, "yeet");
    GuiItem guiItem = new GuiItem(new ItemStack(Material.STONE));


    public void maingui(Player player) {
        gui.open(player);
        gui.setItem(1, guiItem);
    }


}
