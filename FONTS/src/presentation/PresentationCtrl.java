package src.presentation;

import src.domain.controllers.DomainCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;
import src.enums.Format;

import java.io.IOException;
import java.util.ArrayList;

public class PresentationCtrl {
    private static PresentationCtrl instance;

    private MainViewCtrl mainViewCtrl;
    private DocumentTabCtrl documentTabCtrl;

    public PresentationCtrl() {}

    public static PresentationCtrl getInstance() {
        if (instance == null) {
            instance = new PresentationCtrl();
        }
        return instance;
    }

    public void setMainViewCtrl(MainViewCtrl mainViewCtrl) {
        this.mainViewCtrl = mainViewCtrl;
    }

    public void setDocumentTabCtrl(DocumentTabCtrl documentTabCtrl) {
        this.documentTabCtrl = documentTabCtrl;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Functions to controll the UI
     */

    public void openDocument(DocumentInfo docToOpen) throws IOException {
        mainViewCtrl.openDocOnTab(docToOpen);
    }
    /*
    private void switchScene(String newScene) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        Stage newStage = new Stage();
        newStage.setMinWidth(600);
        newStage.setMinHeight(400);
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void switchToSearch() throws IOException {
        switchScene("/src/presentation/fxml/search.fxml");
    }

    public void switchToBooleanExpression() throws IOException {
        switchScene("/src/presentation/fxml/booleanExpressionTab.fxml");
    }

    public void switchToText() throws IOException {

    }
     */




    /**
     * Getters from the domain
     */

    public ArrayList<DocumentInfo> getAllDocuments() {
        return DomainCtrl.getInstance().getAllDocumentsInfo();
    }

    public ArrayList<DocumentInfo> getDocuments (String title, String author) {
        return DomainCtrl.getInstance().getDocsByTitleAndAuthor(title, author);
    }

    /*
    public DocumentInfo getOneDocument (String title, String author) {
        return DomainCtrl.getInstance().getOneDocument(title, author);
    }
    */

    public ArrayList<String> getAllTitles() {
        return DomainCtrl.getInstance().getAllTitles(new String());
    }

    public ArrayList<String> getTitles(String t) {
        return DomainCtrl.getInstance().getAllTitles(t);
    }

    public ArrayList<String> getTitlesByAuthor(String author) {
        return DomainCtrl.getInstance().getAllAuthorTitles(author);
    }

    public ArrayList<String> getAllAuthors() {
        return DomainCtrl.getInstance().getAllAuthors(new String());
    }

    public ArrayList<String> getAuthors(String a) {
        return DomainCtrl.getInstance().getAllAuthors(a);
    }

    public ArrayList<String> getAuthorsByTitle(String title) {
        return DomainCtrl.getInstance().getAllTitleAuthors(title);
    }


    /**
     * Boolean Expressions functions
     */
    public ArrayList<DocumentInfo> tempBooleanExpressionSearch(String boolExp)throws Exception{
        return DomainCtrl.getInstance().tempBooleanExpressionSearch(boolExp);
    }

    public void addBooleanExpression (String boolExpName, String boolExp) throws Exception{
        DomainCtrl.getInstance().addBooleanExpression(boolExpName,boolExp);
    }




    /**
     * Persistence related functions
     */

    /**
     * File Management Functions
     */

    public boolean saveDocument(String title, String author, ArrayList<String> content) {
        return DomainCtrl.getInstance().saveDocument(title, author, content);
    }

    public void saveAsDocument(DocumentInfo docToBeSaved) {
        DomainCtrl.getInstance().saveAsDocument(docToBeSaved);
    }

    public void export(DocumentInfo docToBeSaved) {
        DomainCtrl.getInstance().exportDocument(docToBeSaved);
    }

    public DocumentInfo importDocument(String path, Format format) {
        return DomainCtrl.getInstance().importDocumentFromFile(path, format);
    }

    /**
     * Data Persistance Functions
     */
    public void doBackup() {
        DomainCtrl.getInstance().saveData();
    }

    public void restoreBackup() throws Exception {
        DomainCtrl.getInstance().loadData();

    }

    public void deleteBackup() {
        DomainCtrl.getInstance().clearAllData();
    }

    //TODO: afegir funcio per eliminar tot el contingut del sistema
    public void deleteData() {
    }

    public void setError(String error) {
        mainViewCtrl.setError(error);
    }
}

