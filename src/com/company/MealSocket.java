package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class MealSocket extends Thread {
    private Socket socket = null;
    public MealSocket(Socket socket){
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        System.out.println("For a Meal Plan Request");
        start();
    }
    public void run(){
        //TODO LOTS
        System.out.println("Do Meal Work");
    }

}
