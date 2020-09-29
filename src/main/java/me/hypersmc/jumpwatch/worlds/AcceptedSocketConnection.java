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
import java.util.Date;
import java.util.StringTokenizer;

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
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());
            dataOut = new BufferedOutputStream(sock.getOutputStream());
            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken().toLowerCase();
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
            if (fileRequested.endsWith("/")) {
                fileRequested += DEFAULT_FILE;
            }

            File file = new File(main, fileRequested);
            int fileLength = (int) file.length();
            String content = getContentType(fileRequested);

            if (method.equals("GET")) { // GET method so we return content
                byte[] fileData = readFileData(file, fileLength);

                // send HTTP Headers
                out.write("HTTP/1.1 200 OK");
                out.write("Server: Java HTTP Server from SSaurel : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + content);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();
            }
            out.close();
            in.close();
            sock.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

}