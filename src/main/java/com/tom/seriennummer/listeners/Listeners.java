package com.tom.seriennummer.listeners;

import com.tom.seriennummer.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Listeners implements Listener {

    private Main plugin;

    public Listeners(Main main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getSlot() != -999) { // Prüfen, ob der Klick im Inventar war, da sonst ein Fehler in der Konsole kommt
            if(e.getClickedInventory().getName() == "Seriennummer §ahinzufügen" || e.getClickedInventory().getName() == "Seriennummer §centfernen") {
                if(e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {

                    // Abfragen, ob der Klick dafür war um ein Item hinzuzufügen oder zu entfernen
                    if(e.getCurrentItem().getItemMeta().getDisplayName() == "§aSeriennummer zu Item in deiner Hand hinzufügen?") {
                        plugin.sql.addSN(p, plugin.getRandomNumber());
                        p.closeInventory();
                    } else if(e.getCurrentItem().getItemMeta().getDisplayName() == "§cSeriennummer von Item in deiner Hand entfernen?") {

                        int loreSize = p.getItemInHand().getItemMeta().getLore().size();

                        // Die Seriennummer aus der Lore splitten
                        for(int i = 0; i < loreSize; i++) {
                            if(p.getItemInHand().getItemMeta().getLore().get(i).contains("SN:")) {
                                String[] myNumber = p.getItemInHand().getItemMeta().getLore().get(i).split(" ");
                                int number = Integer.parseInt(myNumber[1]);
                                plugin.sql.removeSN(p, number);
                                p.closeInventory();
                            }
                        }
                    }
                }

            e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent e){
        NPC npc = e.getNPC();
        if (Objects.equals(npc.getName(), "§a§lBob")){
            Player p = e.getClicker();

            ItemStack item = p.getItemInHand();
            boolean hasSN = false;

            if(item.getType() != Material.AIR) {
                // Prüfen, ob das Item eine seriennummer hat
                if(item.getItemMeta().hasLore()) {
                    for(int i = 0; i < item.getItemMeta().getLore().size(); i++) {
                        if(item.getItemMeta().getLore().get(i).contains("SN:")) {
                            hasSN = true;
                        }
                    }

                    // wenn ein Item eine Seriennummer hat ein anderen Gui öffnen als, wenn es keine hat
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

        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();

        // (Nur zum Test) Einen NPC mit der Citizens API bei jedem sneaken spawnen
        if(e.isSneaking()){
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "§a§lBob");
            npc.spawn(p.getLocation());
        }
    }

}
