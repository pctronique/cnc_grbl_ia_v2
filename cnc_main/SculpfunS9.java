import com.fazecast.jSerialComm.SerialPort;
import java.io.*;

public class SculpfunS9 {

    public static void main(String[] args) throws Exception {

        // Adapter selon votre OS
        String portName = "/dev/ttyUSB0";   // Linux
        // String portName = "COM3";        // Windows

        SerialPort port = SerialPort.getCommPort(portName);

        port.setBaudRate(115200);
        port.setNumDataBits(8);
        port.setNumStopBits(1);
        port.setParity(SerialPort.NO_PARITY);

        if (!port.openPort()) {
            System.out.println("Impossible d'ouvrir le port");
            return;
        }

        System.out.println("Port ouvert");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(port.getInputStream()));

        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(port.getOutputStream()));

        // Attendre message de démarrage GRBL
        System.out.println("Attente GRBL...");
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(">> " + line);
            if (line.contains("Grbl")) {
                break;
            }
        }

        // Déverrouiller machine
        sendCommand("$X", in, out);

        // Exemple : mouvement laser OFF
        sendCommand("G0 X10 Y10", in, out);

        // Allumer laser faible puissance
        sendCommand("M3 S100", in, out);

        Thread.sleep(2000);

        // Éteindre laser
        sendCommand("M5", in, out);

        port.closePort();
        System.out.println("Terminé.");
    }

    private static void sendCommand(String cmd,
                                    BufferedReader in,
                                    BufferedWriter out) throws Exception {

        System.out.println("Envoi: " + cmd);
        out.write(cmd + "\n");
        out.flush();

        String response;
        while ((response = in.readLine()) != null) {
            System.out.println("<< " + response);
            if (response.equals("ok") || response.startsWith("error")) {
                break;
            }
        }
    }
}
