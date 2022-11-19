package src.presentation;

import javafx.stage.FileChooser;
import src.domain.controllers.DomainCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PresentationCtrl {
    private static PresentationCtrl instance;

    public PresentationCtrl() {}

    public static PresentationCtrl getInstance() {
        if (instance == null) {
            instance = new PresentationCtrl();
        }
        return instance;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMainScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openDocument(String path) {
        DomainCtrl.getInstance().openFile(path);
    }

    public void switchToTextScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/textWindow.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void newDocument(ActionEvent e) throws IOException {
        switchToTextScene(e);
        System.out.println("new document on scene text");
    }

    public void search(ActionEvent e) throws IOException {
        switchToTextScene(e);
        System.out.println("open");
    }


}
