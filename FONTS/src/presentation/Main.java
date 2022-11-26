package src.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/src/presentation/fxml/mainEditorWindow.fxml"));
        Scene scene = new Scene(root);

        String css = this.getClass().getResource("/src/presentation/css/main.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Macrosoft Word");
        //stage.sizeToScene();
        //stage.setHeight(500);
        //stage.setWidth(700);
        stage.setMinHeight(300);
        stage.setMinWidth(400);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
