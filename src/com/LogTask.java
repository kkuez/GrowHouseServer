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

    private static int MAX_LOGFILE_SIZE_IN_MB = 300;

    public LogTask(Properties properties){
       this.properties = properties;
       this.archiveFolder = new File(properties.getProperty("localArchivePath"));
       if(!logFileExists(archiveFolder)){
           logFile = createNewLogFile();
       }else{
           logFile = getLogFile();
       }
    }

    @Override
    public void perform() {
        if(!checkFileSize(logFile)){
            createNewLogFile();
        }
        String remoteLog = getLogFromRemote();
        try(FileWriter fileWriter = new FileWriter(logFile)){
            fileWriter.append("gtwef");
            //fileWriter.append(remoteLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkFileSize(File file){
        long maxFileSizeInBytes = MAX_LOGFILE_SIZE_IN_MB * 1000000;
        return file.length() <= maxFileSizeInBytes;
    }

    private String getLogFromRemote(){
        String ip = properties.getProperty("remoteIP");
        int port = Integer.parseInt(properties.getProperty("remotePort"));
        StringBuilder logString = new StringBuilder();

        try(Socket socket = new Socket(ip, port);
            Scanner scannerOfStream = new Scanner(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.write("getLog***");
            //FLush muss sein, autoflash funzt nicht
            printWriter.flush();

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

    private boolean logFileExists(File folder){
        File[] filesInProgramFolder = folder.listFiles();
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
