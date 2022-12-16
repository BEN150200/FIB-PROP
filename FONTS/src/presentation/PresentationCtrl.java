package src.presentation;

import src.domain.controllers.DomainCtrl;
import src.domain.core.DocumentInfo;
import src.enums.Format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.vavr.control.Either;

public class PresentationCtrl {
    private static PresentationCtrl instance;

    private MainViewCtrl mainViewCtrl;
    //private DocumentTabCtrl documentTabCtrl;

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

    /*
    public void setDocumentTabCtrl(DocumentTabCtrl documentTabCtrl) {
        this.documentTabCtrl = documentTabCtrl;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    */

    /**
     * Functions to controll the UI
     */

    public void openDocument(DocumentInfo docToOpen) throws IOException {
        mainViewCtrl.openDocOnTab(docToOpen);
    }


    /**
     * Getters from the domain
     */
    public ArrayList<DocumentInfo> getAllDocuments() {
        return DomainCtrl.getInstance().getAllDocumentsInfo();
    }

    public ArrayList<DocumentInfo> getDocuments (String title, String author) {
        return DomainCtrl.getInstance().getDocsByTitleAndAuthor(title, author);
    }

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

    public boolean existsDocument(String title, String author) {
        if (DomainCtrl.getInstance().getOneDocument(title, author) != null) {
            return true;
        }
        return false;
    }


    /**
     * Boolean Expressions Manage Functions
     */
    public void addBooleanExpression (String boolExpName, String boolExp) throws Exception{
        DomainCtrl.getInstance().addBooleanExpression(boolExpName,boolExp);
    }

    /**
     * Complex Searches Functions
     */
    public ArrayList<DocumentInfo> storedBooleanExpressionSearch(String boolExpName) {
        return DomainCtrl.getInstance().storedBooleanExpressionSearch(boolExpName);
    }
    public ArrayList<DocumentInfo> tempBooleanExpressionSearch(String boolExp)throws Exception{
        return DomainCtrl.getInstance().tempBooleanExpressionSearch(boolExp);
    }

    public ArrayList<DocumentInfo> similaritySearch(String title, String author, int k) {
        return DomainCtrl.getInstance().similarDocumentsSearch(title, author, k);
    }

    public HashMap<String,String> getAllBooleanExpresions() {
        return DomainCtrl.getInstance().getAllBooleanExpresions();
    }

    public boolean existsBooleanExpression(String boolExpName){
        return DomainCtrl.getInstance().existsBooleanExpression(boolExpName);
    }

    public boolean deleteBooleanExpression(String boolExpName) {
        return DomainCtrl.getInstance().deleteBooleanExpression(boolExpName);
    }

    public Either<String, ArrayList<DocumentInfo>> weightedSearch(String query) {
        return DomainCtrl.getInstance().documentsByQuery(query);
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

    public void deleteDocument(String title, String author) {
        DomainCtrl.getInstance().deleteDocument(title, author);
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



    public void saveAllDocs() {
        mainViewCtrl.saveAllDocs();
    }

    public boolean unsavedDocuments() {
        return mainViewCtrl.unsavedDocuments();
    }


    /**
     * Tractament de les excepcions
     */

    public void setError(String error) {
        mainViewCtrl.setMessage(error);
    }
    public void showExceptionAlert (String message) {
        mainViewCtrl.showExceptionAlert(message);
    }
}

