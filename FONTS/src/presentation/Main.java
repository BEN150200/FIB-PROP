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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/mainView.fxml"));

        Parent root = loader.load();

        MainViewCtrl mainViewCtrl = loader.getController();
        PresentationCtrl.getInstance().setMainViewCtrl(mainViewCtrl);

        Scene scene = new Scene(root);

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
