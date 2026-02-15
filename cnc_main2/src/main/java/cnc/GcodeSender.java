package cnc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GcodeSender {

    private final SerialManager serial;

    public GcodeSender(SerialManager serial) {
        this.serial = serial;
    }

    public void sendFile(Path path) throws Exception {

        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith(";"))
                continue;

            serial.send(line);
            String response = serial.waitForOk();

            if (response.startsWith("error")) {
                System.out.println("Erreur détectée, arrêt.");
                break;
            }
        }

        System.out.println("Fichier terminé.");
    }
}
