package cnc;

public class GrblController {

    private final SerialManager serial;

    public GrblController(SerialManager serial) {
        this.serial = serial;
    }

    public void initialize() throws Exception {
        Thread.sleep(2000); // laisser GRBL démarrer
        serial.send("$X");  // unlock
        serial.waitForOk();
    }

    public void move(double x, double y) throws Exception {
        serial.send("G0 X" + x + " Y" + y);
        serial.waitForOk();
    }

    public void laserOn(int power) throws Exception {
        serial.send("M3 S" + power);
        serial.waitForOk();
    }

    public void laserOff() throws Exception {
        serial.send("M5");
        serial.waitForOk();
    }

    public void home() throws Exception {
        serial.send("$H");
        serial.waitForOk();
    }
}