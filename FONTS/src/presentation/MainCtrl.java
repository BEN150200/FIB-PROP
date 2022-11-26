package src.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/editorTab.fxml"));

        tab.setContent(loader.load());
        //TabCtrl controller = loader.getController();

        tab.selectedProperty().addListener((obs, wfs, isSelected) -> {
            if (isSelected) {

                TextField title = (TextField) tab.getContent().lookup("#title");
                currentTitle = title.getText();
                TextField author = (TextField) tab.getContent().lookup("#author");
                currentAuthor = author.getText();
            }
        });

        //tabs.put(tab,controller);

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private void saveDocument() {

        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea content = (TextArea) currentTab.lookup("#textArea");

        System.out.println("Current Tab Title: " + title.getText());
        System.out.println("Current Tab Author: " + author.getText());
        System.out.println("Current Tab content: " + content.getText());

    }


    public void openDocument(String title, String author, String content) {

    }

    public void openFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);

    }

    public void switchToSearch(ActionEvent event) throws IOException {
        PresentationCtrl.getInstance().switchToSearch();
    }

    public void newDocument(ActionEvent event) throws IOException {

    }
}
