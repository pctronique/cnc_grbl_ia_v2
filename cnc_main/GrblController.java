
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GrblController {

    private SerialManager serial;

    public GrblController(SerialManager serial) {
        this.serial = serial;
    }

    public void unlock() throws Exception {
        serial.send("$X");
    }

    public void home() throws Exception {
        serial.send("$H");
    }

    public void move(double x, double y) throws Exception {
        serial.send("G0 X" + x + " Y" + y);
    }

    public void laserOn(int power) throws Exception {
        serial.send("M3 S" + power);
    }

    public void laserOff() throws Exception {
        serial.send("M5");
    }

    public void status() throws Exception {
        serial.send("?");
    }
    
    public void sendFile(Path path) throws Exception {

        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                serial.send(line);
            }
        }
    }
    
}
