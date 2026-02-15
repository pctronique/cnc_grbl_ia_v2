package cnc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class GcodeSender {

    private static final int GRBL_BUFFER_SIZE = 128;

    private final SerialManager serial;
    private final BlockingQueue<String> incoming;

    public GcodeSender(SerialManager serial) {
        this.serial = serial;
        this.incoming = serial.getIncomingQueue();
    }

    public void sendFile(Path path) throws Exception {

        List<String> lines = Files.readAllLines(path)
                .stream()
                .map(String::trim)
                .filter(l -> !l.isEmpty() && !l.startsWith(";"))
                .toList();

        Queue<Integer> pendingBytes = new LinkedList<>();

        int bufferUsed = 0;
        int lineIndex = 0;

        while (lineIndex < lines.size() || !pendingBytes.isEmpty()) {

            // Envoi tant que buffer libre
            while (lineIndex < lines.size()) {

                String line = lines.get(lineIndex);
                int bytes = line.length() + 1;

                if (bufferUsed + bytes > GRBL_BUFFER_SIZE)
                    break;

                serial.send(line);
                pendingBytes.add(bytes);
                bufferUsed += bytes;
                lineIndex++;
            }

            // Attendre réponse GRBL
            String response = incoming.take();

            if (response.equals("ok")) {
                bufferUsed -= pendingBytes.poll();
                printProgress(lineIndex, lines.size());
            }

            if (response.startsWith("error")) {
                System.out.println("Erreur GRBL: " + response);
                break;
            }
        }

        System.out.println("\nFichier terminé.");
    }

    private void printProgress(int current, int total) {
        int percent = (int) ((current * 100.0) / total);
        System.out.print("\rProgression: " + percent + "%");
    }
}