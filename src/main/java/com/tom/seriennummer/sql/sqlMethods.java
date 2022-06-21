package com.tom.seriennummer.sql;

import com.tom.seriennummer.Main;
import org.bukkit.Material;
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

    /**
     * Seriennummer zu einem Item hinzufügen
     * @param p
     * @param number
     */
    public void addSN(Player p, int number) {
        ItemStack item = p.getItemInHand();
        Material mat = item.getType();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();

        // SQL-Methode welche eine neue Nummer erstellen
        // Prüfen, ob die SQL-Abfrage funktioniert hat und wenn nicht eine Message ausgeben
        if(plugin.mysql.update("INSERT INTO `sn_numbers`(`Number`, `Item`, `Meta`, `Player`) VALUES ('" + number + "','" + mat + "','" + item.getDurability() + "','" + p.getName() + "')")) {
            if(meta.hasLore()) {
                lore = meta.getLore();
            }

            // Neue Lore mit der Nummer zum Item hinzufügen
            lore.add("§cSN: " + number);
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            p.sendMessage("Es konnte keine verbindung zur Datenbank aufgebaut werden!");
        }
   }

    /**
     * Seriennummer von einem Item entfernen
     * @param p
     * @param number
     */
    public void removeSN(Player p, int number) {

        ItemStack item = p.getItemInHand();
        Material mat = item.getType();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        List<String> newLore = new ArrayList<String>();

        // Nummer aus der Datenbank löschen und abfragen, ob es funktioniert hat
        if(plugin.mysql.update("DELETE FROM `sn_numbers` WHERE `Number` = \"" + number + "\";")) {
            for (String s : lore) {
                // Wenn die Zeile s kein "SN:" hat, dann soll es zu newLore hinzugefügt werden
                if (!s.contains("SN:")) {
                    newLore.add(s);
                }
            }

            // Die Liste newLore als Lore setzten
            meta.setLore(newLore);
            item.setItemMeta(meta);
        } else {
            p.sendMessage("Es konnte keine verbindung zur Datenbank aufgebaut werden!");
        }

    }

    /**
     * Liefert eine Liste aller Seriennummern, welche von dem Spieler p erstellt wurden
     * @param p
     * @return
     */
    public List<Integer> getPlayerNumbers(Player p) {
        List<Integer> numbers = new ArrayList<Integer>();

        try {
            // Alle nummern aus der Datenbank abrufen und in die numbers List hinzufügen
            ResultSet rs = plugin.mysql.query("SELECT `Number` FROM `sn_numbers` WHERE `Player` = \"" + p.getName() + "\";");

            while(rs.next()) {
                numbers.add(rs.getInt("Number"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return numbers;

    }

    /**
     * Liefert das Item zu einer Seriennummer
     * @param number
     * @return
     */
    public List<String> getItem(int number) {
        String mat = null;
        int meta = 0;
        String player = null;

        List<String> myList = new ArrayList<String>();

        try {
            // Die Nummer number aus der Datenbank abrufen und dann Item, Meta und Player in eine String List setzten
            ResultSet rs = plugin.mysql.query("SELECT * FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");
            if(rs == null) {
                return null;
            }

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


    /**
     * Prüft, ob eine Seriennummer bereits existiert
     * @param number
     * @return
     */
    public List<Boolean> numberExist(int number) {

        // boolean List um nachher zugucken, ob eine verbindung zur DB aufgebaut werden konnte
        List<Boolean> myList = new ArrayList<Boolean>();
        try {
            // Die Nummer number aus der DB abrufen und in die boolean List hinzufügen
            ResultSet rs = plugin.mysql.query("SELECT * FROM `sn_numbers` WHERE `Number` = \"" + number + "\";");
            if(rs == null) {
                myList.add(false); // Nummer existiert
                myList.add(false); // Connection zu Datenbank
                return myList;
            }

            if(rs.next()) {
                myList.add(rs.getString("Number") != null);
                myList.add(true);
                return myList;
            }

            myList.add(false);
            myList.add(true);
            return myList;
        } catch(Exception e) {
            e.printStackTrace();
        }

        myList.add(false); // Nummer existiert
        myList.add(false); // Connection zu Datenbank
        return myList;
    }

    /**
     * Liefert alle Materialien, welche in der Datenbank gespeichert wurden
     * @return
     */
    public List<Material> getMaterialList() {
        List<Material> myList = new ArrayList<Material>();

        try {
            // Alle Items welche in der DB eine Seriennummer haben abrufen
            ResultSet rs = plugin.mysql.query("SELECT `Item` FROM `sn_numbers`;");

            while(rs.next()) {
                // Wenn ein Item schin in der List ist nicht nochmal reinschreiben
                if(!myList.contains(Material.getMaterial(rs.getString("Item")))) {
                    myList.add(Material.getMaterial(rs.getString("Item")));
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return myList;
    }

}
