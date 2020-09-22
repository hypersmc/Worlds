package me.hypersmc.jumpwatch.worlds;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class MySQL {
    public static ResultSet resultSet = null;
    public static ArrayList<String> PList = new ArrayList<>();

    public static Connection db = null;
    Main main = JavaPlugin.getPlugin(Main.class);

    private String host = main.getConfig().getString("SQLHost");

    private String port = main.getConfig().getString("SQLPort");

    private String database = main.getConfig().getString("SQLDatabaseName");

    private String user = main.getConfig().getString("SQLUsername");

    private String pass = main.getConfig().getString("SQLPassword");

    public void closeDatabase() {
        try {
            db.close();
            Bukkit.getLogger().info("[" + main.getDescription().getName() + "] MySQL database closure successful.");
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[" + main.getDescription().getName() + "] Failed to close connection!");
            e.printStackTrace();
        }
    }

    public void setupDatabase() {
        String url = null;
        if (this.user.equals("changeme") && this.pass.equals("changeme") && main.getConfig().getBoolean("useMySQL")) {
            Bukkit.getLogger().info("Ehh u forgot to change config lol.");
            Bukkit.getPluginManager().disablePlugin(main);
            return;
        }
        try {
            String driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + this.user + "&password=" + this.pass + "?autoReconnect=true?useUnicode=yes";
            Class.forName(driver);
            db = DriverManager.getConnection(url, this.user, this.pass);
            MakeDatabaseStuff();
            Bukkit.getServer().getLogger().info("[" + main.getName() + "] MYSQL connection complete.");
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[" + main.getName() + "] Could not connect to the " + this.database + " database!");
            Bukkit.getServer().getLogger().severe("Info: " + url);
            e.printStackTrace();
        }
    }

    public void MakeDatabaseStuff() {
        PreparedStatement users = null;
        try {
            users = db.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.database + ".Users(`ID` INT(8) NOT NULL AUTO_INCREMENT, `User` VARCHAR(45) NULL, `UUID` VARCHAR (45) NULL,`Team` VARCHAR(45) NULL, `Money` INT(64) NULL, `Created` DATETIME NULL, PRIMARY KEY (`ID`));");
            users.executeUpdate();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[" + main.getName() + "] ERROR creating database resources!");
            e.printStackTrace();
        }
    }

    public static void insterFirstUserIntoTable(String player, UUID uuid, Integer money, String team, String date ) {
        PreparedStatement pre = null;
        try {
            pre = db.prepareStatement("INSERT INTO Users VALUES ('" + player + "','" + uuid + "','" + money + "','" + team + "','" + date +"');");
            pre.executeUpdate();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().info(Main.prefix + "User either exist or database error have occurred. Check error bellow.");
            e.printStackTrace();
        }

    }

    public static int geteveryone(UUID uuid) {
        PreparedStatement pre = null;
        try {
            pre = db.prepareStatement("SELECT UUID FROM Users;");
            resultSet = pre.executeQuery();
            while (resultSet.next()) {
                String players = resultSet.getString("UUID");
                PList.add(players);
                if (PList.contains(uuid)) {
                    return 1;
                }else {
                    return 0;
                }
            }
        } catch (Exception e) {
            Bukkit.getServer().getLogger().info(Main.prefix + "User table is either empty or database error have occurred . Check error bellow.");
            e.printStackTrace();
        }
        return 0;
    }
}
