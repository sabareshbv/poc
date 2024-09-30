package org.example;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
    private static JFrame frame;
    private static TrayIcon trayIcon;
    private static SystemTray tray;
    private static JLabel statusLabel;
    private static String[] statuses = {"Cold", "Heat", "Fire", "Wet", "Dry"};
    private static Random random = new Random();

    public static void main(String[] args) {
        // Create a new JFrame (window)
        frame = new JFrame("Open Browser and Execute Script");
        frame.setSize(400, 250);
        frame.setLayout(null); // Use null layout for absolute positioning

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        // Create a button to open the browser
        JButton browserButton = new JButton("Open localhost:7000");
        browserButton.setBounds(50, 50, 200, 30); // Set button position and size

        // Add ActionListener to the browser button
        browserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Create a URI object
                    URI uri = new URI("http://localhost:7000");

                    // Open the default browser with the specified URI
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(uri);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Create a button to execute the script
        JButton scriptButton = new JButton("Execute Script");
        scriptButton.setBounds(50, 100, 200, 30); // Set button position and size

        // Add ActionListener to the script button
        scriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the path to the directory of the running JAR file
                    String jarDir = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();

                    // Locate the script in the same directory as the JAR file
                    String scriptPath = jarDir + File.separator + "script.bat";

                    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptPath);
                    pb.directory(new File(jarDir)); // Set working directory
                    pb.redirectErrorStream(true); // Merge stdout and stderr

                    // Start the process
                    Process process = pb.start();

                    // Read the output from the process
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Wait for the process to complete
                    int exitCode = process.waitFor();
                    System.out.println("Script exited with code: " + exitCode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Create a JLabel for status
        statusLabel = new JLabel("Status: ");
        statusLabel.setBounds(50, 150, 300, 30); // Set label position and size

        // Add buttons and label to the frame
        frame.add(browserButton);
        frame.add(scriptButton);
        frame.add(statusLabel);

        // Handle window closing event to minimize to system tray
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });

        // Set the frame's visibility to true
        frame.setVisible(true);

        // Add application to the system tray
        addAppToTray();

        // Start the thread to update the status label
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Sleep for 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String newStatus = statuses[random.nextInt(statuses.length)];
                SwingUtilities.invokeLater(() -> statusLabel.setText("Status: " + newStatus));
            }
        }).start();
    }

    private static void addAppToTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported!");
            return;
        }

        tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("/Users/sabaresh-17844/Desktop/ballo.png"); // Replace with your icon path

        trayIcon = new TrayIcon(image, "Open Browser and Execute Script");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Open Browser and Execute Script");

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}
