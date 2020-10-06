/*
 * ******************************************************
 *  *Copyright (c) 2020. Jesper Henriksen mhypers@gmail.com
 *
 *  * This file is part of Worlds project
 *  *
 *  * Worlds can not be copied and/or distributed without the express
 *  * permission of Jesper Henriksen
 *  ******************************************************
 */

package me.hypersmc.jumpwatch.worlds;

import me.hypersmc.jumpwatch.worlds.Handler.CommandHandler;
import me.hypersmc.jumpwatch.worlds.Handler.EventHandler;
import me.hypersmc.jumpwatch.worlds.Handler.xs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.logging.Logger;

public class Main extends JavaPlugin  implements Listener{
    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&3&lWorlds&7]&r");
    public static Plugin plugin;
    public FileConfiguration config = getConfig();

    //Werbserver things
    private boolean debug;
    public static String closeConnection = "!Close Connection!";
    private int listeningport;
    private Main m = this;
    private Thread acceptor;
    private boolean acceptorRunning;
    private ServerSocket ss;

    private synchronized boolean getAcceptorRunning() {
        return acceptorRunning;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Bukkit.getServer().getLogger().info( "\n" + "\n" +
                "░██╗░░░░░░░██╗░█████╗░██████╗░██╗░░░░░██████╗░░██████╗\n" +
                "░██║░░██╗░░██║██╔══██╗██╔══██╗██║░░░░░██╔══██╗██╔════╝\n" +
                "░╚██╗████╗██╔╝██║░░██║██████╔╝██║░░░░░██║░░██║╚█████╗░\n" +
                "░░████╔═████║░██║░░██║██╔══██╗██║░░░░░██║░░██║░╚═══██╗\n" +
                "░░╚██╔╝░╚██╔╝░╚█████╔╝██║░░██║███████╗██████╔╝██████╔╝\n" +
                "░░░╚═╝░░░╚═╝░░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░");
        Logger logger = this.getLogger();
        Bukkit.getServer().getLogger().info("\n \n"+ ChatColor.DARK_GRAY + "[]=====["+ ChatColor.GRAY +"Enabling "+ getDescription().getName() + ChatColor.DARK_GRAY + "]=====[]\n" + ChatColor.DARK_GRAY + "| " + ChatColor.RED + "Logged info:\n"+ ChatColor.DARK_GRAY +"|   "+ ChatColor.RED +"Name: "+ ChatColor.GRAY +"Player Housing\n"    + ChatColor.DARK_GRAY +"|   "+ ChatColor.RED +"Webserver: "+ ChatColor.GRAY + webserver() +"\n"    + ChatColor.DARK_GRAY +"|   "+ ChatColor.RED +"Developer: "+ ChatColor.GRAY +"" + this.getDescription().getAuthors() +"\n"+ ChatColor.DARK_GRAY +"|   "+ ChatColor.RED +"Version: "+ ChatColor.GRAY +"" + this.getDescription().getVersion() + "\n"+ ChatColor.DARK_GRAY +"|   "+ ChatColor.RED +"Plugins: "+ ChatColor.GRAY +"\n" + detechplugingWEAAWE());

        new UpdateChecker(this, 12345).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available.");
            }
        });

        if (this.getConfig().getBoolean("UseHtml") || !this.getConfig().getBoolean("UsePHP")){
            if (!new File(getDataFolder() + "/web/", "index.html").exists()) {
                saveResource("web/index.html", false);
            }
        }else if (!this.getConfig().getBoolean("UseHtml") || this.getConfig().getBoolean("UsePHP")){
            if (!new File(getDataFolder() + "/web/", "index.php").exists()) {
                saveResource("web/index.php", false);
            }
        }


        File file = new File("plugins/Worlds/web/index.html");
        File file2 = new File("plugins/Worlds/web/index.php");
        if (!file.exists()){
            Bukkit.getServer().getLogger().warning("No index for html was found!");
            Bukkit.getServer().getLogger().info("This error can be ignored if you use PHP");
        }
        if (!file2.exists()){
            Bukkit.getServer().getLogger().warning("No index for php was found!");
            Bukkit.getServer().getLogger().info("This error can be ignored if you use HTML");

        }
        debug = getConfig().getBoolean("debug");
        if (getConfig().getBoolean("EnableWebserver")) {
            if (getConfig().isSet("listeningport")) {
                Bukkit.getServer().getLogger().info(ChatColor.GRAY + "Found a listening port!");
                try {
                    listeningport = getConfig().getInt("listeningport");
                    ss = new ServerSocket(listeningport);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (getConfig().contains("listeningport")) {
                    Bukkit.getServer().getLogger().warning(ChatColor.YELLOW + "Listening port for registeration changes NOT FOUND! Using default value!");
                    try {
                        listeningport = getConfig().getInt("listeningport");
                        ss = new ServerSocket(listeningport);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Bukkit.getServer().getLogger().warning(ChatColor.DARK_RED + "Plugin disabled! NO VALUE WAS FOUND FOR LISTENING PORT!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }
        }
        acceptorRunning = true;
        acceptor = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket sock;
                Bukkit.getServer().getLogger().info(ChatColor.AQUA + "accepting connections");
                while (getAcceptorRunning()) {
                    try {
                        sock = ss.accept();
                        new AcceptedSocketConnection(sock, m).start();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                Bukkit.getServer().getLogger().info(ChatColor.LIGHT_PURPLE + "Done accepting connections");
            }
        });
        acceptor.start();
        /*
        Self reminder husk at uncomment den if sætning lige nede under.
         */
        //String str1 = Main.plugin.getConfig().getString("activationKey");
        //if (!new xs(str1, "https://zennodes.dk/ActivationKey/verify.php", this).setSecurityKey("TkHr6umrQ8OUPxeWt0ANuXa3zlTopUyF7nYUJaahbZMJAoRZOOzcLjCTG70zVJeDKavfJOC0ilD56Ll6MSV7PFVKkwaMpgwmnk4").reg()) return;


        MySQL SQL = new MySQL();
        SQL.setupDatabase();
        saveDefaultConfig();
        CommandHandler.getcommands();
        EventHandler.getevents();
    }
    public String detechplugingWEAAWE(){
        if ((Bukkit.getPluginManager().getPlugin("AsyncWorldEdit")) != null && (Bukkit.getPluginManager().getPlugin("WorldEdit")) != null) {
            return ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.GREEN +"WorldEdit\n" + ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.GREEN +"AsyncWorldEdit";
        }else if ((Bukkit.getPluginManager().getPlugin("AsyncWorldEdit")) == null && (Bukkit.getPluginManager().getPlugin("WorldEdit")) != null){
            return ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.GREEN +"WorldEdit\n" + ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.RED +"AsyncWorldEdit";
        }else if ((Bukkit.getPluginManager().getPlugin("AsyncWorldEdit")) == null && (Bukkit.getPluginManager().getPlugin("WorldEdit")) == null){
            return ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.RED +"WorldEdit\n" + ""+ ChatColor.DARK_GRAY +"| "+ ChatColor.RED +"AsyncWorldEdit";

        }
        return null;
    }
    public String webserver(){
        if (getConfig().getBoolean("EnableWebserver")){
            return "" + ChatColor.GREEN + "True";

        }else {
            return "" + ChatColor.RED + "False";
        }
    }
    @Override
    public void onDisable() {
        MySQL SQL = new MySQL();
        Connection conn = MySQL.db;
        if (conn == null)
            return;
        try {
            if (conn.isValid(5) || conn != null)
                SQL.closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        acceptorRunning = false;
        Socket sockCloser;
        try {
            sockCloser = new Socket("localhost", listeningport);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sockCloser.getOutputStream()));
            out.write(Main.closeConnection);
            out.close();
            sockCloser.close();
            Bukkit.getServer().getLogger().info(ChatColor.DARK_GREEN + "Closed listening web server successfully!");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}