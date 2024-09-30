package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        // Command to start the service
        String[] command = {"sudo", "systemctl", "start", "time_writer.service"};

        try {
            // Create a ProcessBuilder with the command
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO(); // Inherit IO to see the output in the console

            // Start the process
            Process process = pb.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Command exited with code: " + exitCode);

            // Optionally, read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
