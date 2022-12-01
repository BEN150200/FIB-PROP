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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/search.fxml"));
        VBox table = loader.load();
        splitPane.getItems().add(1,table);
        contractSearch();
        //table.setVisible(false);
        //table.setManaged(false);
        //resultTableCtrl = loader.getController();

        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        newDocCounter = 0;
    }

    public void contractSearch() {
        Node table = splitPane.getItems().get(1);
        table.setDisable(true);
        table.setVisible(false);
        //table.setManaged(false);
        table.managedProperty().bind(table.visibleProperty());
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
    private void newAllDocsTab() throws IOException {
        newEmptyTab("Documents", "resultTable.fxml");
    }

    @FXML
    private void newBoolTab() throws IOException {
        newEmptyTab("Boolean Search", "booleanExpressionTab.fxml");
    }

    public void openDocOnTab(DocumentInfo documentInfo) throws IOException {
        Tab tab = new Tab(documentInfo.getFileName());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/documentTab.fxml"));
        tab.setContent(loader.load());

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
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

    public void openFile(ActionEvent event) throws IOException {
        importFile();
    }

    /*
    public void switchToSearch(ActionEvent event) throws IOException {
        PresentationCtrl.getInstance().switchToSearch();
    }

    public void switchToBooleanExpression (ActionEvent event) throws IOException {
        PresentationCtrl.getInstance().switchToBooleanExpression();
    }
     */

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
        Format format = extractFormat(path);

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().saveAsDocument(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }


    /**
     * Export  and Import functions
     */
    @FXML
    private void exportTXT() {
        export(Format.TXT);
    }

    @FXML
    private void exportXML() {
        export(Format.XML);
    }

    @FXML
    private void exportPROP() {
        export(Format.PROP);
    }

    private void export(Format format) {
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

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().export(docToBeSaved);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    @FXML
    private void importFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        String path = file.getPath();
        Format format = extractFormat(path);

        PresentationCtrl.getInstance().importDocument(path,format);
        resultTableCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
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

    private Format extractFormat(String path) {
        switch (path.substring(path.lastIndexOf(".") + 1)) {
            case "txt" : {
                return Format.TXT;
            }
            case "xml" :  {
                return Format.XML;
            }
            case "prop" :  {
                return Format.PROP;
            }

            default :{
                return Format.TXT;
            }
        }
    }
}
