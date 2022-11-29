package src.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.enums.Format;

import java.io.File;
import java.io.IOException;

public class MainViewCtrl {

    @FXML
    private TabPane tabPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane documentPane;

    private ResultTable resultTableCtrl;

    private SearchViewCtrl searchViewCtrl;

    String currentTitle;
    String currentAuthor;

    public void initialize() throws IOException {
        // load an initial tab:
        //newDocTab();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/search.fxml"));
        VBox table = loader.load();
        splitPane.getItems().add(1,table);
        //documentPane.setContent(table);
        //documentPane.getChildrenUnmodifiable().add(table);
        //documentPane.getChildren().add(table);
        searchViewCtrl = loader.getController();
    }

    @FXML
    private void newDocTab() throws IOException {
        Tab tab = new Tab("New Doc");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/documentTab.fxml"));

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
    private void newSearchTab() throws IOException {
        Tab tab = new Tab("Search");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/search.fxml"));
        tab.setContent(loader.load());
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

        searchViewCtrl.update();

    }


    public void openDocument(String title, String author) {
        PresentationCtrl.getInstance().openDocument(title, author);
    }

    public void openFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        String path = file.getPath();
        String format = path.substring(path.lastIndexOf(".") + 1);
        switch (format) {
            case "txt" : {
                PresentationCtrl.getInstance().importDocument(file.getPath(), Format.TXT);
                break;
            }
            case "xml" :  {
                PresentationCtrl.getInstance().importDocument(file.getPath(), Format.XML);
                break;
            }
            default :{
                System.out.println("format not correct");
            }
        }
    }

    public void switchToSearch(ActionEvent event) throws IOException {
        PresentationCtrl.getInstance().switchToSearch();
    }

    public void newDocument(ActionEvent event) throws IOException {

    }

    /**
     * Data Persistance Functions
     */

    @FXML
    private void doBackup() {
        PresentationCtrl.getInstance().doBackup();
    }

    @FXML
    private void restoreBackup() throws Exception {
        PresentationCtrl.getInstance().restoreBackup();
    }

    @FXML
    private void deleteBackup() {
        PresentationCtrl.getInstance().deleteBackup();
    }

    @FXML
    private void deleteData() {
        PresentationCtrl.getInstance().deleteData();
    }
}
