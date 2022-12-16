package src.presentation;

import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;
import src.enums.Format;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.vavr.control.Try;

public class MainViewCtrl {

    @FXML
    private TabPane tabPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private MenuItem saveButton;

    @FXML
    private MenuItem newDocButton;

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
    private ResultTableCtrl allDocumentsCtrl;

    private VBox titleAuthorSearchView;
    private TitleAuthorSearchCtrl titleAuthorSearchCtrl;

    private VBox similaritySearchView;
    private SimilaritySearchCtrl similaritySearchCtrl;

    private VBox booleanSearchView;
    private BooleanExprSearchCtrl booleanSearchCtrl;

    private VBox weightSearchView;
    private WeightedSearchCtrl weightSearchCtrl;


    //stores the position of the divider for the search panels to make all open with the same width
    private Double dividerPosition;

    //stores if the search panel is visible or not
    private boolean searchVisible;

    //counter of the new docs created, this value is only used in the name of the tab before the document is saved
    private int newDocCounter;

    HashMap<Tab, DocumentTabCtrl> tabControllers = new HashMap<>();




    //String currentTitle;
    //String currentAuthor;

    public void initialize() throws Exception {
        //initialize the search menus and the listeners for the buttons
        initializeSearchPanels();
        setListeners();
        setShortcuts();
        searchVisible = false; //the program starts with the search panels not visible
        dividerPosition = 250.0; //initial position of the search panels


        restoreBackup(); //TODO: show a restore dialog if there is a backup

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

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/similarityPanel.fxml"));
        similaritySearchView = loader3.load();
        similaritySearchCtrl = loader3.getController();
        SplitPane.setResizableWithParent(similaritySearchView, false);

        FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/src/presentation/fxml/booleanExpressionPane.fxml"));
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

    private void setShortcuts() {
        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        newDocButton.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        

    }
    /**
     * Handler of the side menu buttons, it manages the search panel that is currently opened and the new to be open
     * @param newView search that wants to be opened if it's not the current one
     */
    private void contractSearch(VBox newView) {
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
        newEmptyTab("Boolean Search", "booleanExpressionPane.fxml");
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
        if (fxmlFileName == "documentTab.fxml") {
            DocumentTabCtrl docController = loader.getController();
            tabControllers.put(tab, docController);
        }
    }

    /**
     * Opens the content of the DocumentInfo in a new Tab
     * @param documentInfo document to be opened
     * @throws IOException
     */
    public void openDocOnTab(DocumentInfo documentInfo) throws IOException {
        if (documentInfo == null) return;

        for (Tab tab: tabControllers.keySet()) {
            DocumentTabCtrl tabCtrl = tabControllers.get(tab);
            System.out.println(documentInfo.getTitle() == tabCtrl.getTitle());
            System.out.print(documentInfo.getTitle());
            System.out.print(tabCtrl.getTitle());
            if (documentInfo.getTitle() == tabCtrl.getTitle()) {

                if (documentInfo.getAuthor() == tabCtrl.getAuthor()) {
                    System.out.println("is opened");
                    tabPane.getSelectionModel().select(tab);
                }
            }
        }

        Tab tab = new Tab(documentInfo.getFileName());
        //loads the content and gets the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/documentTab.fxml"));
        tab.setContent(loader.load());
        DocumentTabCtrl documentTabCtrl = loader.getController();

        //set the content
        documentTabCtrl.setTitle(documentInfo.getTitle());
        documentTabCtrl.setAuthor(documentInfo.getAuthor());
        documentTabCtrl.setContent(documentInfo.getContent());
        documentTabCtrl.blockTitleAndAuthor();

        //adds the tab to the tabpane and selects it
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        tabControllers.put(tab, documentTabCtrl);

    }

    /**
     * Imports a file and opens it in a new tab
     * @throws IOException
     */
    public void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.gut", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        openDocOnTab(importOneFile(file));
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
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        DocumentTabCtrl currentTabCtrl = tabControllers.get(currentTab);

        ArrayList<String> content = Tokenizer.splitSentences(currentTabCtrl.getContent());

        if (currentTabCtrl.isNew()) {
            if (PresentationCtrl.getInstance().existsDocument(currentTabCtrl.getTitle(),currentTabCtrl.getAuthor())) {
                showExceptionAlert("The document is already in the system");
                return;
            }
        }

        else PresentationCtrl.getInstance().saveDocument(currentTabCtrl.getTitle(), currentTabCtrl.getAuthor(), content);

        //try to save it, if it exists
        boolean exists = PresentationCtrl.getInstance().saveDocument(currentTabCtrl.getTitle(), currentTabCtrl.getAuthor(), content);

        if (!exists) saveAsDocument();
        else  {
            updateAllSearchViews();
            currentTabCtrl.setSaved();
        }
    }

    @FXML
    private void saveAsDocument() {

        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        DocumentTabCtrl currentTabCtrl = tabControllers.get(currentTab);

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        Format format = extractFormat(path);

        ArrayList<String> content = Tokenizer.splitSentences(currentTabCtrl.getContent());
        DocumentInfo docToBeSaved = new DocumentInfo(null, currentTabCtrl.getTitle(), currentTabCtrl.getAuthor(), LocalDateTime.now(), LocalDateTime.now(), content, path, format, file.getName());

        PresentationCtrl.getInstance().saveAsDocument(docToBeSaved);
        tabPane.getSelectionModel().getSelectedItem().setText(file.getName());
        updateAllSearchViews();
        currentTabCtrl.setSaved();
        currentTabCtrl.blockTitleAndAuthor();
    }

    @FXML
    private void deleteDocument() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        DocumentTabCtrl currentTabCtrl = tabControllers.get(currentTab);
        PresentationCtrl.getInstance().deleteDocument(currentTabCtrl.getTitle(), currentTabCtrl.getAuthor());
        tabPane.getTabs().remove(currentTab);
        updateAllSearchViews();
    }

    public void saveAllDocs() {
        tabPane.getSelectionModel().selectFirst();
        int tabs = tabPane.getTabs().size();
        for (int i = 0; i < tabs; ++i) {
            saveDocument();
            tabPane.getSelectionModel().selectNext();
        }
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
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        DocumentTabCtrl currentTabCtrl = tabControllers.get(currentTab);

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();

        ArrayList<String> content = Tokenizer.splitSentences(currentTabCtrl.getContent());

        DocumentInfo docToBeSaved = new DocumentInfo(null, currentTabCtrl.getTitle(), currentTabCtrl.getAuthor(), LocalDateTime.now(), LocalDateTime.now(), content, path, format, file.getName());

        PresentationCtrl.getInstance().export(docToBeSaved);
        updateAllSearchViews();
    }

    @FXML
    private void importFile() throws IOException {
        var start = Instant.now();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        List<File> fileList = fileChooser.showOpenMultipleDialog(fileStage);
        
        CompletableFuture.allOf(
            fileList.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> safeImport(file)))
                .toArray(CompletableFuture[]::new)
        )
        .thenRunAsync(() -> {
            var end = Instant.now();
            System.out.println("Loaded files in " + Duration.between(start, end).getSeconds() + " secs");
        });
    }

    private Try<DocumentInfo> safeImport(File file) {
        return Try.of(() -> importOneFile(file));
    }

    private DocumentInfo importOneFile(File file) throws IOException {

        String path = file.getPath();
        Format format = extractFormat(path);

        DocumentInfo docInfo = PresentationCtrl.getInstance().importDocument(path,format);
        if (docInfo == null) {
            Alert docExists = new Alert(Alert.AlertType.ERROR);
            docExists.setContentText("A document with the same title and author already exists in the system");
            docExists.show();
        }
        else updateAllSearchViews();
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
            setMessage("ERROR: Backup not found");
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
            case "gut": {
                return Format.GUTEMBERG;
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

    public void setMessage(String message) {
        errorLable.setText(message);
    }


    public boolean unsavedDocuments() {
        for (Tab docTab : tabPane.getTabs()) {
            if (tabControllers.get(docTab).modified()) {
                return true;
            }
        }
        return false;
    }

    public void showExceptionAlert (String message ) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
