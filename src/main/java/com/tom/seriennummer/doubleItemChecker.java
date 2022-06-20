package com.tom.seriennummer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class doubleItemChecker {

    private static Main plugin;

    public doubleItemChecker(Main main) {
        plugin = main;
    }

    public void checkInventorys() {
        List<Material> matList = plugin.sql.getMaterialList();
        HashMap<Integer, Player> playerNumbers = new HashMap<>();

        for(Player all : Bukkit.getOnlinePlayers()) {
            for (Material material : matList) {
                if (all.getInventory().contains(material)) {
                    for (int o = 0; o < 36; o++) {
                        if (all.getInventory().getItem(o) != null) {
                            ItemStack item = all.getInventory().getItem(o);
                            if (item.getType().equals(material)) {
                                if (item.getItemMeta().hasLore()) {
                                    for (int l = 0; l < item.getItemMeta().getLore().size(); l++) {
                                        if (item.getItemMeta().getLore().get(l).contains("SN:")) {

                                            String[] myNumber = item.getItemMeta().getLore().get(l).split(" ");
                                            int number = Integer.parseInt(myNumber[1]);

                                            if(!playerNumbers.containsKey(number)) {
                                                playerNumbers.put(number, all);
                                            } else {
                                                if(Objects.equals(all.getName(), playerNumbers.get(number).getName())) {
                                                    System.out.println("Der Spieler " + all.getName() + " hat mehrfach das selbe Item!");
                                                } else {
                                                    System.out.println("Der Spieler " + all.getName() + " und der Spieler " + playerNumbers.get(number).getName() + " haben das selbe Item!");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
