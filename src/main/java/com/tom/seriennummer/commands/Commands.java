package com.tom.seriennummer.commands;

import com.tom.seriennummer.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Commands implements CommandExecutor {

    private Main plugin;

    public Commands(Main main) {
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        Player p = (Player) sender;
        boolean hasSN = false;

        ItemStack item = p.getItemInHand();

        if(args.length != 0) {
            switch(args[0]){
                case "additem":
                    if(item.getType() != Material.AIR) {
                        if (p.getItemInHand().getAmount() == 1) {
                            int number = plugin.getRandomNumber();

                            if (item.getItemMeta().hasLore()) {
                                for (int i = 0; i < item.getItemMeta().getLore().size(); i++) {
                                    if (item.getItemMeta().getLore().get(i).contains("SN")) {
                                        hasSN = true;
                                        p.sendMessage("Dieses Item hat bereits eine Seriennummer");
                                    }
                                }

                                if (!hasSN) {
                                    plugin.sql.addItem(p, number);
                                }

                            } else {
                                plugin.sql.addItem(p, number);
                            }
                        } else {
                            p.sendMessage("Du kannst nur einem Item gleichzeitig eine Seriennummer geben!");
                        }
                    }
                    break;

                case "getitem":
                    if(args.length == 2) {
                        int number = Integer.parseInt(args[1]);
                        if(plugin.sql.numberExist(number).get(1)){
                            if(plugin.sql.numberExist(number).get(0)) {

                                List<String> myList = plugin.sql.getItem(number);


                                if(myList != null) {
                                    Material mat = Material.getMaterial(myList.get(0));
                                    int meta = Integer.parseInt(myList.get(1));
                                    String playerName = myList.get(2);

                                    p.sendMessage("Das Item mit der Seriennummer " + number + " wurde von " + playerName + " erstellt und ist " + mat + " mit einer Meta von " + meta + ".");
                                }
                            } else {
                                p.sendMessage("Die Seriennummer " + number + " existiert nicht!");
                            }

                        } else {
                            p.sendMessage("Es konnte keine verbindung zur Datenbank hergestellt werden!");
                        }

                    } else {
                        p.sendMessage("Bitte nutze /seriennummer get [Nummer]");
                    }
                    break;

                case "getnumbers":
                    if(args.length == 2) {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player pp = Bukkit.getPlayer(args[1]);
                            List<Integer> numbers = plugin.sql.getPlayerNumbers(pp);

                            if(numbers.size() == 0) {
                                p.sendMessage("Der Spieler hat noch keine Seriennummern erstellt!");
                            } else {

                                p.sendMessage("Der Spieler: " + pp.getName() + " hat folgende Seriennummern erstellt:");
                                for(int i = 0; i<numbers.size(); i++) {
                                    int myNumber = i+1;
                                    p.sendMessage(myNumber + ". " + numbers.get(i));
                                }
                            }

                        } else {
                            p.sendMessage("Dieser Spieler existiert nicht!");
                        }

                    } else {
                        p.sendMessage("Bitte nutze /seriennummer getnumbers [Spielername]");
                    }

                    break;

                case "remove":
                    if(item.getType() != Material.AIR) {
                        if(item.getItemMeta().hasLore()) {

                            int loreSize = item.getItemMeta().getLore().size();

                            for(int i = 0; i < loreSize; i++) {
                                if(item.getItemMeta().getLore().get(i).contains("SN")) {

                                    String[] myNumber = item.getItemMeta().getLore().get(i).split(" ");
                                    int number = Integer.parseInt(myNumber[1]);
                                    plugin.sql.removeItem(p, number);
                                }
                            }
                        }
                    }
                    break;

                case "check":
                    plugin.checker.checkInventorys();
                    break;

                case "gui":
                    if(item.getType() != Material.AIR) {
                        if(item.getItemMeta().hasLore()) {
                            for(int i = 0; i < item.getItemMeta().getLore().size(); i++) {
                                if(item.getItemMeta().getLore().get(i).contains("SN:")) {
                                    hasSN = true;
                                }
                            }

                            if(!hasSN) {
                                p.openInventory(plugin.gui.getInventroy('a'));
                            } else {
                                p.openInventory(plugin.gui.getInventroy('r'));
                            }

                        } else {
                            if (p.getItemInHand().getAmount() == 1) {
                                p.openInventory(plugin.gui.getInventroy('a'));
                            } else {
                                p.sendMessage("Du kannst nur einem Item gleichzeitig eine Seriennummer geben!");
                            }
                        }
                    } else {
                        p.sendMessage("Du musst ein Item in der Hand haben!");
                    }
                    break;

                default:

                    return false;
            }
        }

        return true;
    }
}
