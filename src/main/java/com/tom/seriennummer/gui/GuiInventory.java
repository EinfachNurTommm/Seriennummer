package com.tom.seriennummer.gui;

import com.tom.seriennummer.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiInventory {

    private static Main plugin;

    public GuiInventory(Main main) {
        this.plugin = main;
    }


    public Inventory getInventroy(char c) {
        Inventory inv;

        int color = 0;
        String name = "";

        if(c == 'a') {
            inv = Bukkit.getServer().createInventory(null, 27, "Seriennummer §ahinzufügen");
            color = 5;
            name = "§aSeriennummer zu Item in deiner Hand hinzufügen?";
        } else {
            inv = Bukkit.getServer().createInventory(null, 27, "Seriennummer §centfernen");
            color = 14;
            name = "§cSeriennummer von Item in deiner Hand entfernen?";
        }

        for(int i = 0; i<27; i++) {
            inv.setItem(i, build(Material.STAINED_GLASS_PANE, 1, 7, "§a"));
        }

        inv.setItem(3, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(4, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(5, build(Material.STAINED_GLASS_PANE, 1, color, name));

        inv.setItem(12, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(13, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(14, build(Material.STAINED_GLASS_PANE, 1, color, name));

        inv.setItem(21, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(22, build(Material.STAINED_GLASS_PANE, 1, color, name));
        inv.setItem(23, build(Material.STAINED_GLASS_PANE, 1, color, name));

        return inv;
    }



    public ItemStack build(Material m, int anzahl, int subId, String name){
        ItemStack itemStack = new ItemStack(m, anzahl, (byte) subId);//Item, Anzahl, subID
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
