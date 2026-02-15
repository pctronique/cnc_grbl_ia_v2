package ui;

import cnc.*;
import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Port (ex: /dev/ttyUSB0 ou COM3): ");
        String port = scanner.nextLine();

        SerialManager serial = new SerialManager();
        serial.connect(port, 115200);

        GrblController grbl = new GrblController(serial);
        grbl.initialize();

        GcodeSender sender = new GcodeSender(serial);

        while (true) {

            System.out.println("""
                    1 - Move
                    2 - Laser ON
                    3 - Laser OFF
                    4 - Home
                    5 - Send Gcode file
                    0 - Quit
                    """);

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("X: ");
                    double x = Double.parseDouble(scanner.nextLine());
                    System.out.print("Y: ");
                    double y = Double.parseDouble(scanner.nextLine());
                    grbl.move(x, y);
                }
                case "2" -> grbl.laserOn(200);
                case "3" -> grbl.laserOff();
                case "4" -> grbl.home();
                case "5" -> {
                    System.out.print("Chemin fichier: ");
                    sender.sendFile(Path.of(scanner.nextLine()));
                }
                case "0" -> {
                    serial.close();
                    return;
                }
            }
        }
    }
}