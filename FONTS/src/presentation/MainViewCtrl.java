package src.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;
import src.enums.Format;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainViewCtrl {

    @FXML
    private TabPane tabPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private MenuItem saveButton;

    private ResultTable resultTableCtrl;

    private int newDocCounter;

    //private SearchViewCtrl searchViewCtrl;
    //private ResultTable resultTable;

    //String currentTitle;
    //String currentAuthor;

    public void initialize() throws IOException {
        // load result tab:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();
        splitPane.getItems().add(1,table);
        resultTableCtrl = loader.getController();

        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        newDocCounter = 0;
    }

    @FXML
    private void newDocTab() throws IOException {
        ++newDocCounter;
        newEmptyTab("New Doc " + newDocCounter, "documentTab.fxml");
    }

    @FXML
    private void newSearchTab() throws IOException {
        newEmptyTab("Search", "search.fxml");
    }

    @FXML
    private void newBoolTab() throws IOException {
        newEmptyTab("Boolean Search", "booleanExpressionTab.fxml");
    }

    /**
     *
     * @param tabName Name of the new Tab to be created
     * @param fxmlFileName  String with the name of the FXML file with the content of the tab
     * @throws IOException
     */
    private void newEmptyTab(String tabName, String fxmlFileName) throws IOException {
        Tab tab = new Tab(tabName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/" + fxmlFileName));
        tab.setContent(loader.load());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
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

    public void switchToBooleanExpression (ActionEvent event) throws IOException {
        PresentationCtrl.getInstance().switchToBooleanExpression();
    }
    public void newDocument(ActionEvent event) throws IOException {

    }

    /**
     * File Management Functions
     */
    @FXML
    private void saveDocument() {

        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        boolean exists = PresentationCtrl.getInstance().saveDocument(title.getText(), author.getText(), content);

        if (!exists) saveAsDocument();

        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }


    @FXML
    private void saveAsDocument() {

        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();
        Format format = null;
        switch (path.substring(path.lastIndexOf(".") + 1)) {
            case "txt" : {
                format = Format.TXT;
                break;
            }
            case "xml" :  {
                format = Format.XML;
                break;
            }
            default :{
                format = Format.TXT;
            }
        }

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());


        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().saveAsDocument(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());

    }





    @FXML
    private void exportTXT() {

        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();
        Format format = Format.TXT;

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().export(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    @FXML
    private void exportXML() {
        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();
        Format format = Format.XML;

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().export(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    @FXML
    private void exportPROP() {
        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();
        Format format = Format.PROP;

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().export(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    @FXML
    private void importFile() {

    }

    /**
     * Data Persistence Functions
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
