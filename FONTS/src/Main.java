package src;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application  {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root);

        //Image logo = new Image("img.png");
        //stage.getIcons().add(logo);
        stage.setTitle("Macrosoft Word");

        stage.setScene(scene);
        stage.show();
    }
}
