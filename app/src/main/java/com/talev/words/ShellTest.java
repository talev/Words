package com.talev.words;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Dimko Talev on 5/28/16.
 */
public class ShellTest {

    public static void main(String[] args) throws java.io.IOException, java.lang.InterruptedException {
//        System.getProperties().list(System.out);
        System.out.println(System.getProperty("os.name"));
        // Get runtime
        Runtime runtime = Runtime.getRuntime();
        // Start a new process: UNIX command ls
        Process process = runtime.exec("ls /");
        // You can or maybe should wait for the process to complete
        process.waitFor();
        System.out.println("Process exited with code = " + process.exitValue());
        // Get process' output: its InputStream
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // And print each line
        String s = null;
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
        }
        is.close();

//        System.out.println("Yes this is main script");
    }

}
