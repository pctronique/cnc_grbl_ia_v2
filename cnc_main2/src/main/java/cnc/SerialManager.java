package cnc;

import cnc.events.GrblListener;
import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialManager {

    private SerialPort port;
    private BufferedReader reader;
    private BufferedWriter writer;

    private final BlockingQueue<String> responses = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> incoming = new LinkedBlockingQueue<>();
    private final List<GrblListener> listeners = new CopyOnWriteArrayList<>();

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
                    incoming.put(line);
                    
                    //responses.put(line);
                    for (GrblListener l : listeners)
                        l.onLineReceived(line);

                    MachineState state = MachineState.parse(line);
                    
                    if (state != null) {
                        for (GrblListener l : listeners)
                            l.onStateChanged(state);
                    }

                    if (line.startsWith("error")) {
                        for (GrblListener l : listeners)
                            l.onError(line);
                    }
                    
                    if (line.startsWith("ALARM")) {
                        for (GrblListener l : listeners)
                            l.onError("ALARM détectée: " + line);
                    }

                    if (state != null) {
                        System.out.println("Etat: " + state.state +
                                           " X=" + state.x +
                                           " Y=" + state.y);
                    }
                }
            } catch (Exception e) {
                System.out.println("Lecture arrêtée.");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public BlockingQueue<String> getIncomingQueue() {
        return incoming;
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

    public void addListener(GrblListener listener) {
        listeners.add(listener);
    }
    
    public void sendRealtime(char c) throws Exception {
        port.getOutputStream().write(c);
        port.getOutputStream().flush();
    }

    public void reset() throws Exception {
        port.getOutputStream().write(0x18);
        port.getOutputStream().flush();
    }

    public void close() {
        port.closePort();
    }
}