package cnc;

public class MachineState {

    public String state;
    public double x, y, z;

    public static MachineState parse(String line) {

        if (!line.startsWith("<"))
            return null;

        MachineState ms = new MachineState();

        String content = line.substring(1, line.length() - 1);
        String[] parts = content.split("\\|");

        ms.state = parts[0];

        for (String p : parts) {
            if (p.startsWith("MPos:")) {
                String[] coords = p.substring(5).split(",");
                ms.x = Double.parseDouble(coords[0]);
                ms.y = Double.parseDouble(coords[1]);
                ms.z = Double.parseDouble(coords[2]);
            }
        }

        return ms;
    }
}
