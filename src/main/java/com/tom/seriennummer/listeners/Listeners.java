package com.tom.seriennummer.listeners;

import com.tom.seriennummer.Main;
import com.tom.seriennummer.npc.PacketReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Listeners implements Listener {

    private Main plugin;

    public Listeners(Main main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getSlot() != -999) {
            if(e.getClickedInventory().getName() == "Seriennummer §ahinzufügen" || e.getClickedInventory().getName() == "Seriennummer §centfernen") {
                if(e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName() == "§aSeriennummer zu Item in deiner Hand hinzufügen?") {
                        plugin.sql.addItem(p, plugin.getRandomNumber());
                        p.closeInventory();
                    } else if(e.getCurrentItem().getItemMeta().getDisplayName() == "§cSeriennummer von Item in deiner Hand entfernen?") {

                        int loreSize = p.getItemInHand().getItemMeta().getLore().size();

                        for(int i = 0; i < loreSize; i++) {
                            if(p.getItemInHand().getItemMeta().getLore().get(i).contains("SN")) {

                                String[] myNumber = p.getItemInHand().getItemMeta().getLore().get(i).split(" ");
                                int number = Integer.parseInt(myNumber[1]);
                                plugin.sql.removeItem(p, number);
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
    public void onJoin(PlayerJoinEvent e) {
        PacketReader pr = new PacketReader(e.getPlayer(), plugin);
        pr.inject();
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e){
        Player p = e.getPlayer();

        if(e.isSneaking()){
            Location loc = p.getLocation();
            plugin.npcManager.createNPC(p, "Test");
        }
    }


}
