package com.tom.seriennummer;

import com.tom.seriennummer.commands.Commands;
import com.tom.seriennummer.gui.GuiInventory;
import com.tom.seriennummer.listeners.Listeners;
import com.tom.seriennummer.npc.NPC;
import com.tom.seriennummer.sql.MySQL;
import com.tom.seriennummer.sql.sqlMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public final class Main extends JavaPlugin {

    public sqlMethods sql = new sqlMethods(this);
    public GuiInventory gui = new GuiInventory(this);
    public doubleItemChecker checker = new doubleItemChecker(this);
    public NPC npcManager;

    public static MySQL mysql;

    @Override
    public void onEnable() {

        mysql = new MySQL("localhost", "seriennummer", "admin", "banane");
        if(mysql.hasConnection()) {
            this.npcManager = new NPC();
            registerMyCommands();
            registerMyEvents();
            startScheduler();
            System.out.println("SerienNummer gestartet!");
        } else {
            System.out.println("Seriennummer wurde nicht gestartet, da die Datenbank nicht erreicht werden konnte!");
        }
    }

    @Override
    public void onDisable() {
        System.out.println("SerienNummer gestoppt!");
    }

    public void registerMyCommands(){
        Commands cmds = new Commands(this);
        getCommand("seriennummer").setExecutor(cmds);
    }

    public void registerMyEvents() {
        new Listeners(this);
    }


    public int getRandomNumber() {
        Random rand = new Random();
        int number = Integer.parseInt(String.format("%06d", rand.nextInt(999999)));

        if(sql.numberExist(number).get(1)) {
            while(sql.numberExist(number).get(0)) {
                number = Integer.parseInt(String.format("%06d", rand.nextInt(999999)));
            }
        } else {
            return -1;
        }

        return number;
    }

    private void startScheduler() {
        int sec = 10;
        new BukkitRunnable() {

            @Override
            public void run() {
                checker.checkInventorys();
                System.out.println("Inventare gecheckt!");
            }

        }.runTaskTimer(this, 20*sec, 20*sec);
    }

}
