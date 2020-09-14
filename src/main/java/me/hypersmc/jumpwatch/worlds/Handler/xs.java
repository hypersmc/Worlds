package me.hypersmc.jumpwatch.worlds.Handler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class xs {

    private String licenseKey;
    private Plugin plugin;
    private String vs;
    private LogType lt = LogType.NORMAL;
    private String SK = "TkHr6umrQ8OUPxeWt0ANuXa3zlTopUyF7nYUJaahbZMJAoRZOOzcLjCTG70zVJeDKavfJOC0ilD56Ll6MSV7PFVKkwaMpgwmnk4";
    private boolean debug = false;

    public xs(String licenseKey, String vs, Plugin plugin) {
        this.licenseKey = licenseKey;
        this.plugin = plugin;
        this.vs = vs;
    }

    public xs setSecurityKey(String securityKey){
        this.SK = securityKey;
        return this;
    }

    public xs setConsoleLog(LogType logType) {
        this.lt = logType;
        return this;
    }

    public xs debug() {
        debug = true;
        return this;
    }

    public boolean reg() {
        ValidationType vt = isValid();
        if (vt == ValidationType.VALID) {
            log(0, "Hey look at you. You did buy my plugin :D Here you go access to the plugin.");
            return true;
        } else {
            log(1, "Bad! you didn't even buy this plugin! Now I'm sad :(");
            Bukkit.getScheduler().cancelTasks(plugin);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }


    public boolean isValidSimple() {
        return (isValid() == ValidationType.VALID);
    }

    private String requestServer(String v1, String v2) throws IOException {
        URL url = new URL(vs + "?v1=" + v1 + "&v2=" + v2 + "&pl=" + plugin.getName());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        if (debug) {
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }

    public ValidationType isValid() {
        String rand = toBinary(UUID.randomUUID().toString());
        String sKey = toBinary(SK);
        String key = toBinary(licenseKey);

        try {
            String response = requestServer(xor(rand, sKey), xor(rand, key));

            if (response.startsWith("<")) {
                log(1, "The License-Server returned an invalid response!");
                log(1,
                        "SERVER-RESPONSE: " + (response.length() < 150 || debug ? response : response.substring(0, 150) + "..."));
                return ValidationType.PAGE_ERROR;
            }

            try {
                return ValidationType.valueOf(response);
            } catch (IllegalArgumentException exc) {
                String respRand = xor(xor(response, key), sKey);
                if (rand.substring(0, respRand.length()).equals(respRand))
                    return ValidationType.VALID;
                else
                    return ValidationType.WRONG_RESPONSE;
            }
        } catch (IOException e) {
            if (debug)
                e.printStackTrace();
            return ValidationType.PAGE_ERROR;
        }
    }

    //
    // Cryptographic
    //

    private static String xor(String s1, String s2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < (Math.min(s1.length(), s2.length())); i++)
            result.append(Byte.parseByte("" + s1.charAt(i)) ^ Byte.parseByte(s2.charAt(i) + ""));
        return result.toString();
    }

    //
    // Enums
    //

    public enum LogType {
        NORMAL, LOW, NONE;
    }

    public enum ValidationType {
        WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
    }

    //
    // Binary methods
    //

    private String toBinary(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    //
    // Console-Log
    //

    private void log(int type, String message) {
        if (lt == LogType.NONE || (lt == LogType.LOW && type == 0))
            return;
        System.out.println(message);
    }
}
