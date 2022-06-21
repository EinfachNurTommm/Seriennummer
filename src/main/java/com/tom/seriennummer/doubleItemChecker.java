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

    /**
     * Prüft, ob ein Item in irgendeinem Inventar auf dem Server doppelt vorhanden ist
     */
    public void checkInventorys() {
        List<Material> matList = plugin.sql.getMaterialList();
        HashMap<Integer, Player> playerNumbers = new HashMap<>();

        for(Player all : Bukkit.getOnlinePlayers()) { // Jeden Spieler auf dem Server holen
            for (Material material : matList) { // Alle Materialien welche in der DB gespeichter wurden abrufen
                if (all.getInventory().contains(material)) { // prüfen ob ein Item aus der DB im Inventar vom Spieler ist
                    for (int o = 0; o < 36; o++) { // Durch das ganze Inventar gehen um nachher durch jeden Slot gehen zu können
                        if (all.getInventory().getItem(o) != null) {
                            ItemStack item = all.getInventory().getItem(o);
                            if (item.getType().equals(material)) { // prüfen ob das Material im Slot eines der gespeicherten aus der DB ist
                                if (item.getItemMeta().hasLore()) {

                                    // Prüfen ob das Item die Lore "SN:" hat um die Seriennummer zu bekommen
                                    for (int l = 0; l < item.getItemMeta().getLore().size(); l++) {
                                        if (item.getItemMeta().getLore().get(l).contains("SN:")) {

                                            String[] myNumber = item.getItemMeta().getLore().get(l).split(" ");
                                            int number = Integer.parseInt(myNumber[1]);

                                            // Prüfen ob es die Nummer schon in der HashMap playerNumbers gibt
                                            if(!playerNumbers.containsKey(number)) {
                                                playerNumbers.put(number, all);
                                            } else {
                                                // Prüfen, wer es schon hat und dann ausgeben
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
