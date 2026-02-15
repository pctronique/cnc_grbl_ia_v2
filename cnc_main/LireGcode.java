import java.nio.file.Files;
import java.nio.file.Paths;

public class LireGcode {
    public static void main(String[] args) throws Exception {

        String chemin = "E:/programme.nc"; // lettre de la clé USB
        String contenu = new String(Files.readAllBytes(Paths.get(chemin)));

        System.out.println(contenu);
    }
}
