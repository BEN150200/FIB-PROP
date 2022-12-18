package src.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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


        stage.setOnCloseRequest(windowEvent -> {

            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION, "Save all the files before exit?", ButtonType.YES, ButtonType.NO);
            saveAlert.setTitle("Save Backup");
            saveAlert.initModality(Modality.APPLICATION_MODAL);
            saveAlert.initOwner(stage);
            saveAlert.showAndWait();
            if (saveAlert.getResult() == ButtonType.YES) {
                PresentationCtrl.getInstance().closeAllTabs();
            }
            else if (saveAlert.getResult() == ButtonType.NO) {
                windowEvent.consume();
            }
            PresentationCtrl.getInstance().doBackup();

        });

        stage.setTitle("Macrosoft Word");
        //stage.sizeToScene();
        stage.setHeight(500);
        stage.setWidth(1000);
        stage.setMinHeight(400);
        stage.setMinWidth(800);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
