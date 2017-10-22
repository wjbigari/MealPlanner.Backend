package com.company;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.ServerSocket;


public class Main {
    public static final int SEARCH_PORT_NUMBER = 8083;
    public static final int MEAL_PORT_NUMBER = 8080;
    public static void main(String[] args) {
          SearchServer sserver = new SearchServer();
          MealServer mserver = new MealServer();
//        System.out.println("SocketServer Example");
//        ServerSocket server = null;
//        try {
//            server = new ServerSocket(PORT_NUMBER);
//            while (true) {
//                /**
//                 * create a new {@link SocketServer} object for each connection
//                 * this will allow multiple client connections
//                 */
//                new SocketServer(server.accept());
//            }
//        } catch (IOException ex) {
//            System.out.println("Unable to start server.");
//        } finally {
//            try {
//                if (server != null)
//                    server.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
    }
}

