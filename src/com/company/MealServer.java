package com.company;

import java.io.IOException;
import java.net.ServerSocket;

import static com.company.Main.MEAL_PORT_NUMBER;

public class MealServer extends Thread {
    public MealServer(){
        start();
    }
    @Override
    public void run(){
        System.out.println("Waiting for Meal Plan Requests");
        ServerSocket server = null;
        try {
            server = new ServerSocket(MEAL_PORT_NUMBER);
            while (true) {
                /**
                 * create a new {@link SocketServer} object for each connection
                 * this will allow multiple client connections
                 */
                new MealSocket(server.accept());
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
