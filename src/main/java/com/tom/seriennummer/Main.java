package com.tom.seriennummer;

import com.tom.seriennummer.commands.Commands;
import com.tom.seriennummer.gui.GuiInventory;
import com.tom.seriennummer.listeners.Listeners;
import com.tom.seriennummer.sql.MySQL;
import com.tom.seriennummer.sql.sqlMethods;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Main extends JavaPlugin {

    public sqlMethods sql = new sqlMethods(this);
    public GuiInventory gui = new GuiInventory(this);

    public static MySQL mysql;

    @Override
    public void onEnable() {

        mysql = new MySQL("localhost", "seriennummer", "admin", "banane");
        registerMyCommands();
        registerMyEvents();
        System.out.println("SerienNummer gestartet!");
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

        while(sql.numberExist(number)) {
            number = Integer.parseInt(String.format("%06d", rand.nextInt(999999)));
        }

        return number;

    }

}