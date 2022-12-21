package src.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.skin.LabelSkin;
import javafx.scene.control.skin.TabPaneSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
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
import java.util.Objects;
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
    private MenuItem closeDocButton;

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

    private VBox weightedSearchView;
    private WeightedSearchCtrl weightedSearchCtrl;


    //stores the position of the divider for the search panels to make all open with the same width
    private Double dividerPosition;

    //stores if the search panel is visible or not
    private boolean searchVisible;

    //counter of the new docs created, this value is only used in the name of the tab before the document is saved
    private int newDocCounter;

    HashMap<Tab, DocumentTabCtrl> tabControllers = new HashMap<>();

    public void initialize() {
        //initialize the search menus and the listeners for the buttons
        initializeSearchPanels();
        setListeners();
        searchVisible = false; //the program starts with the search panels not visible
        dividerPosition = 250.0; //initial position of the search panels
        
        restoreBackup(); //TODO: show a restore dialog if there is a backup
        
        newDocCounter = 0; // counter initialized at 0
    }

    /**
     * Initialize all the search panels of the side menu and store the controllers
     */
    private void initializeSearchPanels() {
        //load the panel that shows all the documents
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        try {
            allDocumentsView = loader.load();
            allDocumentsCtrl = loader.getController();
            allDocumentsCtrl.setForAllDocs();
            SplitPane.setResizableWithParent(allDocumentsView, false);
        } catch (IOException e) {
            setMessage("resultTable.fxml file not found, the allDocumentsView could not be loaded");
        }

        //load the panel to search by title and author
        loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/titleAuthorPanel.fxml"));
        try {
            titleAuthorSearchView = loader.load();
            titleAuthorSearchCtrl = loader.getController();
            SplitPane.setResizableWithParent(titleAuthorSearchView, false);
        } catch (IOException e) {
            setMessage("titleAuthorPanel.fxml file not found, the titleAuthorSearchView could not be loaded");
        }

        //load the panel of similarity search
        loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/similarityPanel.fxml"));
        try {
            similaritySearchView = loader.load();
            similaritySearchCtrl = loader.getController();
            SplitPane.setResizableWithParent(similaritySearchView, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //setMessage("similarityPanel.fxml file not found, the similaritySearchView could not be loaded");
        }
        
        //load the panel of boolean search
        loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/booleanExpressionPane.fxml"));
        try {
            booleanSearchView = loader.load();
            booleanSearchCtrl = loader.getController();
            SplitPane.setResizableWithParent(booleanSearchView, false);
        } catch (IOException e) {
            setMessage("booleanExpressionPane.fxml file not found, the booleanSearchCtrl could not be loaded");
        }

        //load the panel of weighted search
        loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/weightedPanel.fxml"));
        try {
            weightedSearchView = loader.load();
            weightedSearchCtrl = loader.getController();
            SplitPane.setResizableWithParent(weightedSearchView, false);
        } catch (IOException e) {
            setMessage("weightedPanel.fxml file not found, the weightSearchView could not be loaded");
        }
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
            similaritySearchCtrl.update();
        });

        weightButton.setOnAction(event -> {
            contractSearch(weightedSearchView);
        });
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
     * @return return the tab created
     * @throws IOException
     */
    private Tab newEmptyTab(String tabName, String fxmlFileName) throws IOException {

        boolean noTabs = tabPane.getTabs().isEmpty();
        Tab tab = new Tab(tabName);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/" + fxmlFileName));
        tab.setContent(loader.load());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        if (noTabs && searchVisible) {
            splitPane.getDividers().get(0).setPosition(dividerPosition);
        }
        //if the tab is a doc tab
        if (Objects.equals(fxmlFileName, "documentTab.fxml")) {
            //set close handler
            tab.setOnCloseRequest(event -> {
                DocumentTabCtrl tabCtrl = tabControllers.get(tab);

                if (tabCtrl.modified()) {
                    Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION, "This document is not saved, do you want to save it before close it?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    saveAlert.setTitle("Save before close");
                    saveAlert.initModality(Modality.APPLICATION_MODAL);
                    saveAlert.showAndWait();
                    if (saveAlert.getResult() == ButtonType.YES) {
                        tabPane.getSelectionModel().select(tab);
                        saveDocument();
                    }
                    else if (saveAlert.getResult() == ButtonType.CANCEL) {
                        event.consume();
                        return;
                    }
                }
                tabControllers.remove(tab);
                tabPane.getTabs().remove(tab);
            });
            //save the controller
            tabControllers.put(tab, loader.getController());
        }
        return tab;
    }

    /**
     * Opens the content of the DocumentInfo in a new Tab
     * @param documentInfo document to be opened
     * @throws IOException
     */
    public void openDocOnTab(DocumentInfo documentInfo) throws IOException {
        if (documentInfo == null) return;

        if (selectTab(documentInfo.getTitle(), documentInfo.getAuthor())) return;

        //open the documentInfo on new tab
        Tab tab = newEmptyTab(documentInfo.getFileName(), "documentTab.fxml");
        DocumentTabCtrl tabCtrl = tabControllers.get(tab);

        //set the content
        tabCtrl.setTitle(documentInfo.getTitle());
        tabCtrl.setAuthor(documentInfo.getAuthor());
        tabCtrl.setContent(documentInfo.getContent());
        tabCtrl.blockTitleAndAuthor();
        tabCtrl.setSaved();
    }

    private boolean selectTab(String title, String author) {
        for (Tab tab: tabControllers.keySet()) {
            DocumentTabCtrl tabCtrl = tabControllers.get(tab);
            if (Objects.equals(title, tabCtrl.getTitle())) {
                if (Objects.equals(author, tabCtrl.getAuthor())) {
                    tabPane.getSelectionModel().select(tab);
                    return true;
                }
            }
        }
        return false;
    }



    public void closeTab(String title, String author) {
        if (selectTab(title, author)) {
            closeCurrentTab();
        }
    }

    @FXML
    private void closeCurrentTab() {
        Event.fireEvent(tabPane.getSelectionModel().getSelectedItem(), new Event(Tab.TAB_CLOSE_REQUEST_EVENT));
    }

    public void closeAllTabs() {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getSelectionModel().select(i);
            closeCurrentTab();
        }
    }

    /**
     * Imports a file and opens it in a new tab
     * @throws IOException
     */
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.gut", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);
        if(file != null)
            try {
                openDocOnTab(importOneFile(file));
            }
            catch(IOException exception) {
                PresentationCtrl.getInstance().setMessage("ERROR: Unable to open new tab");
            }
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

        if(file != null) {
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

        if(file != null) {
            String path = file.getPath();
            String name = file.getName();

            ArrayList<String> content = Tokenizer.splitSentences(currentTabCtrl.getContent());

            DocumentInfo docToBeSaved = new DocumentInfo(null, currentTabCtrl.getTitle(), currentTabCtrl.getAuthor(), LocalDateTime.now(), LocalDateTime.now(), content, path, format, file.getName());

            PresentationCtrl.getInstance().export(docToBeSaved);
            updateAllSearchViews();
        }
    }

    @FXML
    private void importFile() {
        var start = Instant.now();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop", "*.gut");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage fileStage = new Stage();
        List<File> fileList = fileChooser.showOpenMultipleDialog(fileStage);
        
        CompletableFuture.runAsync(
            () -> fileList.stream()
                .forEach(this::importOneFile)
        )
        .thenRunAsync(() -> {
            var end = Instant.now();
            System.out.println("Loaded files in " + Duration.between(start, end).getSeconds() + " secs");
        });
    }
    
    private DocumentInfo importOneFile(File file) {
        
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
        similaritySearchCtrl.update();
    }

    public void setMessage(String message) {
        errorLable.setText(message);
    }

    public void doSimilaritySearch(String title, String author) {
        if (!searchVisible || splitPane.getItems().get(0) != similaritySearchView) {
            contractSearch(similaritySearchView);
        }
        similaritySearchCtrl.search(title, author);
    }

    public void showExceptionAlert (String message ) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }


}
