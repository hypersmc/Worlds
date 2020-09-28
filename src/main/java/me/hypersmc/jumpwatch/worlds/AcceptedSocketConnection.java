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

import java.io.*;
import java.net.Socket;

public class AcceptedSocketConnection extends Thread{
    Socket sock;
    Main plugin;
    public AcceptedSocketConnection(Socket sock, Main plugin) {
        // TODO Auto-generated constructor stub
        this.sock = sock;
        this.plugin = plugin;

    }
    @Override
    public void run() {
        try {
            BufferedOutputStream dataOut = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            dataOut = new BufferedOutputStream(sock.getOutputStream());

            String contentMimeType = "text/html";

            String s;
            int counterr = 0, contentLength = 0;
            //boolean gotEmptyLine = false;//TODO Remember why I did this line lol
            while (!(s = in.readLine()).equals("")) {
                if(counterr == 0 && s.equalsIgnoreCase(Main.closeConnection)){
                    out.close();
                    in.close();
                    sock.close();

                    return;
                }
                if(s.startsWith("Content-Length: ")){
                    contentLength = Integer.parseInt(s.split("Length: ")[1]);
                }
                counterr++;
            }

            String finalString = "";
            for(int i = 0; i < contentLength; i++){
                finalString += (char) in.read();
            }

            //This section is the response to the clients request, the web page:
            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
            out.write("Server: Apache/0.8.4\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
            out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
            out.write("\r\n");
            //Content of returned html page is here:
            File file = new File( plugin.getDataFolder() + "/web/index.html");
            File file2 = new File(plugin.getDataFolder() + "/web/index.php");

            if (file.isFile()) {
                if (plugin.getConfig().getBoolean("UseHtml")) {
                    BufferedReader reader;
                    reader = new BufferedReader(new FileReader(file));
                    String line = reader.readLine();
                    while (line != null) {
                        out.write(line);
                        line = reader.readLine();
                    }
                }



            } else if (file2.isFile()){
                if (plugin.getConfig().getBoolean("UsePHP")) {
                    BufferedReader reader2;
                    reader2 = new BufferedReader(new FileReader(file2));
                    String line2 = reader2.readLine();
                    while (line2 != null) {
                        out.write(line2);
                        line2 = reader2.readLine();
                    }
                }

            }else {
                out.write("<h1>Failed</h1>");
                out.write("<br><h2>Missing index.php or index.html</h2>");

            }



            out.close();
            in.close();
            sock.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}