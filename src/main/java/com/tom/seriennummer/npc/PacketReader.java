package com.tom.seriennummer.npc;

import com.tom.seriennummer.Main;
import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.inventory.ItemStack;


public class PacketReader {

    int spam = 0;

    Player p;
    Channel channel;

    private Main plugin;

    public PacketReader(Player player, Main plugin) {
        this.p = player;
        this.plugin = plugin;
    }

    public void inject(){
        CraftPlayer cPlayer = (CraftPlayer)this.p;
        channel = cPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {@Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {arg2.add(packet);readPacket(packet);}});
    }

    public void uninject(){
        if(channel.pipeline().get("PacketInjector") != null){
            channel.pipeline().remove("PacketInjector");
        }
    }


    public void readPacket(Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
            int id = (Integer)getValue(packet, "a");

            System.out.println(getValue(packet, "action").toString());

            if(getValue(packet, "action").toString() == "INTERACT") {

                if(spam != 0) {
                    spam = 0;
                    return;
                }

                ItemStack item = p.getItemInHand();
                boolean hasSN = false;

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



                spam = 1;
            }

        }
    }


    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }

}
