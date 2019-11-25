package com;

import java.net.SocketOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MainLoopThread extends Thread {

    private boolean exit;

    private List<Task> taskList;

    private final static long HOUR_IN_MS = 3600000;

    private Properties properties;

    public MainLoopThread(Properties properties){
        exit = false;
        taskList = new LinkedList<>();
        this.properties = properties;
    }

    @Override
    public void run() {
        System.out.println("asd");
        while(!exit){
            for(Task task: taskList){


            }
        }


        try {
            sleep(HOUR_IN_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
