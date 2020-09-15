package me.hypersmc.jumpwatch.worlds;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class MySQL {
    public static ResultSet resultSet = null;

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
            Bukkit.getLogger().info("[" + Main.plugin.getName() + "] MySQL database closure successful.");
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[" + Main.plugin.getName() + "] Failed to close connection!");
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
            users = db.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.database + ".Users(`ID` INT(8) NOT NULL AUTO_INCREMENT, `User` VARCHAR(45) NULL, `Team` VARCHAR(45) NULL, `Money` INT(64) NULL, `Created` DATETIME NULL, PRIMARY KEY (`ID`));");
            users.executeUpdate();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[" + main.getName() + "] ERROR creating database resources!");
            e.printStackTrace();
        }
    }
}
