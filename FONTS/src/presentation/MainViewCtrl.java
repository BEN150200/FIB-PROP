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

    @FXML
    private Button allDocsButton;

    @FXML
    private Button titleAuthorButton;

    @FXML
    private Button booleanButton;

    @FXML
    private Button similarButton;

    @FXML
    private Button weightButton;

    @FXML
    private Label errorLable;



    private int newDocCounter;

    private boolean searchVisible;

    private VBox allDocumentsView;
    private ResultTable allDocumentsCtrl;

    private VBox titleAuthorSearchView;
    private SearchViewCtrl titleAuthorSearchCtrl;

    private VBox similaritySearchView;
    private ResultTable similaritySearchCtrl;

    private VBox booleanSearchView;
    private BooleanExpressionTabCtrl booleanSearchCtrl;

    private VBox weightSearchView;
    private WeightedSearch weightSearchCtrl;

    private Double dividerPosition;


    //String currentTitle;
    //String currentAuthor;

    public void initialize() throws Exception {
        // load result tab:
        initializeSearchPanels();
        setListeners();
        searchVisible = false;
        dividerPosition = 250.0;

        //table.setVisible(false);
        //table.setManaged(false);

        restoreBackup();
        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        newDocCounter = 0;
    }

    private void initializeSearchPanels() throws IOException {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        allDocumentsView = loader1.load();
        allDocumentsCtrl = loader1.getController();
        allDocumentsCtrl.setForAllDocs();
        SplitPane.setResizableWithParent(allDocumentsView, false);

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/search.fxml"));
        titleAuthorSearchView = loader2.load();
        titleAuthorSearchCtrl = loader2.getController();
        SplitPane.setResizableWithParent(titleAuthorSearchView, false);

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        similaritySearchView = loader3.load();
        similaritySearchCtrl = loader3.getController();
        similaritySearchCtrl.setForSimilarity();
        SplitPane.setResizableWithParent(similaritySearchView, false);

        FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/booleanExpressionTab.fxml"));
        booleanSearchView = loader4.load();
        booleanSearchCtrl = loader4.getController();
        SplitPane.setResizableWithParent(booleanSearchView, false);

        FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/weightedPanel.fxml"));
        weightSearchView = loader5.load();
        weightSearchCtrl = loader5.getController();
        SplitPane.setResizableWithParent(weightSearchView, false);

    }

    private void setListeners() {
        allDocsButton.setOnAction(event -> {
            contractSearch(allDocumentsView);
            allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
        });

        titleAuthorButton.setOnAction(event -> {
            contractSearch(titleAuthorSearchView);
            titleAuthorSearchCtrl.update();
        });

        booleanButton.setOnAction(event -> {
            contractSearch(booleanSearchView);
        });

        similarButton.setOnAction(event -> {
            contractSearch(similaritySearchView);
        });

        weightButton.setOnAction(event -> {
            contractSearch(weightSearchView);
        });

    }
    public void contractSearch(VBox newView) {
        //splitPane.setDividerPosition(1,0);
        if (searchVisible) {
            dividerPosition = splitPane.getDividers().get(0).getPosition();
            VBox current = (VBox) splitPane.getItems().get(0);
            if (current != newView) {
                splitPane.getItems().remove(0);
                //newView.setMinWidth(220.0);
                splitPane.getItems().add(0, newView);
                //splitPane.setDividerPosition(0, 0.3d);
                splitPane.getDividers().get(0).setPosition(dividerPosition);

            }
            else {
                splitPane.getItems().remove(0);
                searchVisible = false;
            }
        }
        else {
            //splitPane.setDividerPosition(0, 0.3d);
            //newView.setMinWidth(220.0);
            splitPane.getItems().add(0, newView);
            splitPane.getDividers().get(0).setPosition(dividerPosition);

            searchVisible = true;
            /*
            VBox table = (VBox) splitPane.getItems().get(1);
            table.setDisable(false);
            table.setVisible(true);
            table.setMaxWidth(600);
            table.managedProperty().bind(table.visibleProperty());
            splitPane.setDividerPosition(1, 150);
            searchVisible = true;
             */
        }
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
        boolean noTabs = tabPane.getTabs().isEmpty();
        Tab tab = new Tab(documentInfo.getFileName());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/documentTab.fxml"));
        tab.setContent(loader.load());
        DocumentTabCtrl documentTabCtrl = loader.getController();
        documentTabCtrl.setTitle(documentInfo.getTitle());
        documentTabCtrl.setAuthor(documentInfo.getAuthor());
        documentTabCtrl.setContent(documentInfo.getContent());

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        if (noTabs && searchVisible) {
            splitPane.getDividers().get(0).setPosition(dividerPosition);
        }
    }

    /**
     *
     * @param tabName Name of the new Tab to be created
     * @param fxmlFileName  String with the name of the FXML file with the content of the tab
     * @throws IOException
     */
    private void newEmptyTab(String tabName, String fxmlFileName) throws IOException {
        boolean noTabs = tabPane.getTabs().isEmpty();
        Tab tab = new Tab(tabName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/" + fxmlFileName));
        tab.setContent(loader.load());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        if (noTabs && searchVisible) {
            splitPane.getDividers().get(0).setPosition(dividerPosition);
        }

    }

    public void openFile(ActionEvent event) throws IOException {
        openDocOnTab(importFile());
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

        allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
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
        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format, file.getName());

        PresentationCtrl.getInstance().saveAsDocument(docToBeSaved);
        tabPane.getSelectionModel().getSelectedItem().setText(file.getName());
        allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
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

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format, file.getName());

        PresentationCtrl.getInstance().export(docToBeSaved);
        allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    @FXML
    private DocumentInfo importFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        String path = file.getPath();
        Format format = extractFormat(path);

        DocumentInfo docInfo = PresentationCtrl.getInstance().importDocument(path,format);
        allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
        return docInfo;
    }


    /**
     * Data Persistence Functions
     */

    @FXML
    private void doBackup() {
        PresentationCtrl.getInstance().doBackup();
    }

    @FXML
    private void restoreBackup() {
        try {
            PresentationCtrl.getInstance().restoreBackup();
        } catch (Exception e) {
            setError("ERROR: Backup not found");
        }
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

    public void setError(String error) {
        errorLable.setText(error);
    }
}
