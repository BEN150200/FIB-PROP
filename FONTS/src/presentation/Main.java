package src.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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

        Parent root = FXMLLoader.load(getClass().getResource("/textWindow.fxml"));
        Scene scene = new Scene(root);

        String css = this.getClass().getResource("/main.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Macrosoft Word");
        stage.setHeight(500);
        stage.setWidth(700);
        stage.setMinHeight(300);
        stage.setMinWidth(400);

        stage.setScene(scene);
        stage.show();
    }
}
