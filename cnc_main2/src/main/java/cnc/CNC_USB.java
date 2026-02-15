package cnc;

//import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;

public class CNC_USB {

    /*public static void main(String[] args) {

        // Liste les ports disponibles
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
        }

        // Choisir votre port (ex: COM3 ou /dev/ttyUSB0)
        SerialPort comPort = SerialPort.getCommPort("COM3");

        comPort.setBaudRate(115200);
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(1);
        comPort.setParity(SerialPort.NO_PARITY);

        if (comPort.openPort()) {
            System.out.println("Port ouvert !");

            try {
                OutputStream out = comPort.getOutputStream();
                InputStream in = comPort.getInputStream();

                // Exemple : envoyer une commande G-code
                String gcode = "G0 X10 Y10\n";
                out.write(gcode.getBytes());
                out.flush();

                // Lire réponse
                byte[] buffer = new byte[1024];
                int numRead = in.read(buffer);
                System.out.println("Reçu: " + new String(buffer, 0, numRead));

            } catch (Exception e) {
                e.printStackTrace();
            }

            comPort.closePort();
        } else {
            System.out.println("Impossible d'ouvrir le port.");
        }
    }*/
}
