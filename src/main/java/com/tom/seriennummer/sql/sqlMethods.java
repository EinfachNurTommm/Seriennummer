package com.tom.seriennummer.sql;

import com.tom.seriennummer.Main;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class sqlMethods {

    private static Main plugin;

    public sqlMethods(Main main) {
        this.plugin = main;
    }

    public void addItem(Player p, int number) {
        ItemStack item = p.getItemInHand();
        Material mat = item.getType();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();

        if(meta.hasLore()) {
            lore = meta.getLore();
        }

        lore.add("§cSN: " + number);
        meta.setLore(lore);
        item.setItemMeta(meta);

        plugin.mysql.update("INSERT INTO `sn_numbers`(`Number`, `Item`, `Meta`, `Player`) VALUES ('" + number + "','" + mat + "','" + item.getDurability() + "','" + p.getName() + "')");
    }

    public void removeItem(Player p, int number) {

        ItemStack item = p.getItemInHand();
        Material mat = item.getType();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        List<String> newLore = new ArrayList<String>();

        for(int i = 0; i < lore.size(); i++) {
            if(!lore.get(i).contains("SN:")) {
                newLore.add(lore.get(i));
            }
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);

        plugin.mysql.update("DELETE FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");

    }

    public List<Integer> getPlayerNumbers(Player p) {
        List<Integer> numbers = new ArrayList<Integer>();

        try {
            ResultSet rs = plugin.mysql.query("SELECT `Number` FROM `sn_numbers` WHERE `Player` = \"" + p.getName() + "\";");

            while(rs.next()) {
                numbers.add(rs.getInt("Number"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return numbers;

    }

    public List<String> getItem(int number) {
        String mat = null;
        int meta = 0;
        String player = null;

        List<String> myList = new ArrayList<String>();

        try {
            ResultSet rs = plugin.mysql.query("SELECT * FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");
            System.out.println("SELECT * FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");

            if(rs.next()) {
                mat = rs.getString("Item");
                meta = rs.getInt("Meta");
                player = rs.getString("Player");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        myList.add(mat);
        myList.add(meta + "");
        myList.add(player);

        return myList;
    }


    public boolean numberExist(int number) {
        try {
            ResultSet rs = plugin.mysql.query("SELECT * FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");

            if(rs.next()) {
                return rs.getString("Number") != null;
            }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
