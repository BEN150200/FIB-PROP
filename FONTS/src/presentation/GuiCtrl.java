package src.presentation;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiCtrl extends Application  {

    public static void launchPresentation(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Macrosoft Word");
        stage.show();
    }
}
