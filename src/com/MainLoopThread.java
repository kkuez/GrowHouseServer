package com;

import java.io.File;
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
        while(!exit){
            for(Task task: taskList){
                task.perform();
            }
        }

        try {
            sleep(HOUR_IN_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //GETTER SETTER

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public static long getHourInMs() {
        return HOUR_IN_MS;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
