package com;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;

public class LogTask extends Task {

    Properties properties;

    File logFile;

    File archiveFolder;

    public LogTask(Properties properties){
       this.properties = properties;
       this.archiveFolder = new File(properties.getProperty("localArchivePath"));
       if(!logFileExists()){
           logFile = createNewLogFile();
       }else{
           logFile = getLogFile();
       }
    }

    @Override
    public void perform() {
        getLogFromRemote();
    }

    private String getLogFromRemote(){
        String ip = properties.getProperty("remoteIP");
        int port = Integer.parseInt("remotePort");
        StringBuilder logString = new StringBuilder();

        try(Socket socket = new Socket(ip, port);
            Scanner scannerOfStream = new Scanner(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write("getLog".getBytes());

            while(scannerOfStream.hasNext()){
                logString.append(scannerOfStream.next());
            }

            } catch (UnknownHostException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }
        return logString.toString();
    }

    private boolean logFileExists(){
        File[] filesInProgramFolder = archiveFolder.listFiles();
        boolean logExists = false;
        for(File file: filesInProgramFolder){
            if(file.getName().endsWith(".log")){
             logExists = true;
            }
        }

        return logExists;
    }

    private File getLogFile(){
        File[] filesInProgramFolder = archiveFolder.listFiles();
        File latestLogFile = null;
        for(File file: filesInProgramFolder){
            if(file.getName().endsWith(".log")){
                if(latestLogFile == null){
                    latestLogFile = file;
                }else{
                    if(file.lastModified() > logFile.lastModified()){
                        latestLogFile = file;
                    }
                }
            }
        }
        return latestLogFile;
    }

    private File createNewLogFile(){
        File newLogFile = new File(properties.getProperty("localArchivePath"), "log_" + LocalDateTime.now().withNano(0).toString().replace("T", "_").replace(":", "-") + ".log");
        try {
            newLogFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newLogFile;
    }
}
