package cnc;

import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialManager {

    private SerialPort port;
    private BufferedReader reader;
    private BufferedWriter writer;

    private final BlockingQueue<String> responses = new LinkedBlockingQueue<>();

    public void connect(String portName, int baudRate) throws Exception {

        port = SerialPort.getCommPort(portName);
        port.setBaudRate(baudRate);
        port.setNumDataBits(8);
        port.setNumStopBits(1);
        port.setParity(SerialPort.NO_PARITY);

        if (!port.openPort())
            throw new RuntimeException("Impossible d'ouvrir le port");

        reader = new BufferedReader(new InputStreamReader(port.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));

        startReadingThread();

        System.out.println("Connecté à " + portName);
    }

    private void startReadingThread() {
        Thread t = new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("<< " + line);
                    responses.put(line);
                }
            } catch (Exception e) {
                System.out.println("Lecture arrêtée.");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void send(String command) throws Exception {
        System.out.println(">> " + command);
        writer.write(command + "\n");
        writer.flush();
    }

    public String waitForOk() throws Exception {
        while (true) {
            String response = responses.take();
            if (response.equals("ok") || response.startsWith("error"))
                return response;
        }
    }

    public void close() {
        port.closePort();
    }
}