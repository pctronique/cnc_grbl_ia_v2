package ui;

import cnc.GrblController;
import cnc.SerialManager;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Scanner scanner = new Scanner(System.in);
        String port = scanner.nextLine();
        SerialManager serial = new SerialManager();
        serial.connect(port, 115200);

        GrblController controller = new GrblController(serial);
        controller.initialize();

        TextArea console = new TextArea();
        console.setEditable(false);

        Button connect = new Button("Connect");
        Button home = new Button("Home");
        Button pause = new Button("Pause");
        Button resume = new Button("Resume");
        
        home.setOnAction(e -> {
            try { controller.home(); } catch (Exception ex) {}
        });

        VBox root = new VBox(10, connect, home, pause, resume, console);

        stage.setScene(new Scene(root, 400, 500));
        stage.setTitle("Mini GRBL Controller");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
