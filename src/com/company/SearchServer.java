package com.company;

import java.io.IOException;
import java.net.ServerSocket;

import static com.company.Main.SEARCH_PORT_NUMBER;

public class SearchServer extends Thread {
    public SearchServer(){
        start();
    }
    public void run(){
        System.out.println("Waiting for Search Requests");
        ServerSocket server = null;
        try {
            server = new ServerSocket(SEARCH_PORT_NUMBER);
            while (true) {
                /**
                 * create a new {@link SocketServer} object for each connection
                 * this will allow multiple client connections
                 */
                new SocketServer(server.accept());
            }
        }catch (IOException ex) {
            System.out.println("Unable to start server.");
        }finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
