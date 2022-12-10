package src.presentation;

import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;
import src.enums.Format;

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

    //bottom right lable that is used to show errors
    @FXML
    private Label errorLable;


    //all the search panels with his controllers
    private VBox allDocumentsView;
    private ResultTable allDocumentsCtrl;

    private VBox titleAuthorSearchView;
    private TitleAuthorSearchCtrl titleAuthorSearchCtrl;

    private VBox similaritySearchView;
    private ResultTable similaritySearchCtrl;

    private VBox booleanSearchView;
    private BooleanExpressionTabCtrl booleanSearchCtrl;

    private VBox weightSearchView;
    private WeightedSearch weightSearchCtrl;


    //stores the position of the divider for the search panels to make all open with the same width
    private Double dividerPosition;

    //stores if the search panel is visible or not
    private boolean searchVisible;

    //counter of the new docs created, this value is only used in the name of the tab before the document is saved
    private int newDocCounter;




    //String currentTitle;
    //String currentAuthor;

    public void initialize() throws Exception {
        //initialize the search menus and the listeners for the buttons
        initializeSearchPanels();
        setListeners();
        searchVisible = false; //the program starts with the search panels not visible
        dividerPosition = 250.0; //initial position of the search panels

        restoreBackup(); //TODO: show a restore dialog if there is a backup

        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        newDocCounter = 0; // counter initialized at 0
    }

    /**
     * Initialize all the search panels of the side menu and store the controllers
     * @throws IOException
     */
    private void initializeSearchPanels() throws IOException {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        allDocumentsView = loader1.load();
        allDocumentsCtrl = loader1.getController();
        allDocumentsCtrl.setForAllDocs();
        SplitPane.setResizableWithParent(allDocumentsView, false);

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/titleAuthorPanel.fxml"));
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

    /**
     * Set the listeners for the buttons of the side menu
     */
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

    /**
     * Handler of the side menu buttons, it manages the search panel that is currently opened and the new to be open
     * @param newView search that wants to be opened if it's not the current one
     */
    public void contractSearch(VBox newView) {
        // if there are a search panel opened
        if (searchVisible) {

            dividerPosition = splitPane.getDividers().get(0).getPosition(); //save the position
            VBox current = (VBox) splitPane.getItems().get(0);

            //if its diferent, change the panel
            if (current != newView) {
                splitPane.getItems().remove(0);
                splitPane.getItems().add(0, newView);
                splitPane.getDividers().get(0).setPosition(dividerPosition); //to maintain the same position
            }

            //if the current one is the same, close the search panel instead
            else {
                splitPane.getItems().remove(0);
                searchVisible = false;
            }
        }

        // if there is no search panel visible, just opens the one requested
        else {
            splitPane.getItems().add(0, newView);
            splitPane.getDividers().get(0).setPosition(dividerPosition); //to maintain the same position
            searchVisible = true;
        }
    }

    /**
     * Opens a new Document Tab that is empty
     * @throws IOException
     */
    @FXML
    private void newDocTab() throws IOException {
        ++newDocCounter;
        newEmptyTab("New Doc " + newDocCounter, "documentTab.fxml");
    }

    @FXML
    private void newSearchTab() throws IOException {
        newEmptyTab("Search", "titleAuthorPanel.fxml");
    }

    @FXML
    private void newAllDocsTab() throws IOException {
        newEmptyTab("Documents", "resultTable.fxml");
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

    /**
     * Opens the content of the DocumentInfo in a new Tab
     * @param documentInfo document to be opened
     * @throws IOException
     */
    public void openDocOnTab(DocumentInfo documentInfo) throws IOException {
        Tab tab = new Tab(documentInfo.getFileName());
        //loads the content and gets the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/documentTab.fxml"));
        tab.setContent(loader.load());
        DocumentTabCtrl documentTabCtrl = loader.getController();

        //set the content
        documentTabCtrl.setTitle(documentInfo.getTitle());
        documentTabCtrl.setAuthor(documentInfo.getAuthor());
        documentTabCtrl.setContent(documentInfo.getContent());

        //adds the tab to the tabpane and selects it
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Imports a file and opens it in a new tab
     * @throws IOException
     */
    public void openFile() throws IOException {
        openDocOnTab(importFile());
    }


    /**
     * File Management Functions
     */

    /**
     * Default save option, if the document exists just save it, if not exists calls the Save As option
     */
    @FXML
    private void saveDocument() {
        //get the content of the current tab
        Node currentTab = tabPane.getSelectionModel().getSelectedItem().getContent();
        TextField title = (TextField) currentTab.lookup("#title");
        TextField author = (TextField) currentTab.lookup("#author");
        TextArea textArea = (TextArea) currentTab.lookup("#textArea");
        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        //try to save it, if it exists
        boolean exists = PresentationCtrl.getInstance().saveDocument(title.getText(), author.getText(), content);

        if (!exists) saveAsDocument();
        else updateAllSearchViews();
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
        updateAllSearchViews();
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
        updateAllSearchViews();
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
        updateAllSearchViews();
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
                return Format.PROP;
            }
        }
    }

    private void updateAllSearchViews() {
        allDocumentsCtrl.updateTable(PresentationCtrl.getInstance().getAllDocuments());
        titleAuthorSearchCtrl.update();
    }

    public void setError(String error) {
        errorLable.setText(error);
    }


}
