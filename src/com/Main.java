package com;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    static File programFolder = new File(".").getAbsoluteFile();

    public static void main(String[] args) {
	// write your code here
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File(programFolder,"setup.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread mainLoopThread = new MainLoopThread(properties);
        mainLoopThread.start();
    }
}
