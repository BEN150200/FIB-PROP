package src.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainCtrl {

    @FXML
    private TabPane tabPane;

    String currentTitle;
    String currentAuthor;

    public void initialize() throws IOException {
        // load an initial tab:
        newTab();
    }

    @FXML
    private void newTab() throws IOException {
        Tab tab = new Tab("Doc");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/textEditor.fxml"));

        tab.setContent(loader.load());

        TabCtrl controller = loader.getController() ;

        tab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                currentTitle = controller.getTitle();
                currentAuthor = controller.getAuthor();

            }
        });

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    public void openDocument(String title, String author, String content) {

    }

    public void openFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        PresentationCtrl.getInstance().openDocument(file.getAbsolutePath());
        PresentationCtrl.getInstance().switchToTextScene(event);
    }

    public void openSearch(ActionEvent event) throws IOException {

    }

    public void newDocument(ActionEvent event) throws IOException {

    }
}
