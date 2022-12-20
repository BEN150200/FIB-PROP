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
        return DomainCtrl.getInstance().getOneDocument(title, author) != null;
    }


    /**
     * Boolean Expressions Manage Functions
     */
    public void addBooleanExpression (String boolExpName, String boolExp) throws Exception{
        DomainCtrl.getInstance().addBooleanExpression(boolExpName,boolExp);
    }

    public HashMap<String,String> getAllBooleanExpressions() {
        return DomainCtrl.getInstance().getAllBooleanExpresions();
    }

    public boolean existsBooleanExpression(String boolExpName){
        return DomainCtrl.getInstance().existsBooleanExpression(boolExpName);
    }

    public boolean deleteBooleanExpression(String boolExpName) {
        return DomainCtrl.getInstance().deleteBooleanExpression(boolExpName);
    }

    public String getExpression(String name){
        return DomainCtrl.getInstance().getExpression(name);
    }


    /**
     * Complex Searches Functions
     */




    public ArrayList<DocumentInfo> tempBooleanExpressionSearch(String boolExp)throws Exception{
        return DomainCtrl.getInstance().tempBooleanExpressionSearch(boolExp);
    }

    public ArrayList<DocumentInfo> savedBooleanExpressionSearch(String name)throws Exception{
        return DomainCtrl.getInstance().savedBooleanExpressionSearch(name);
    }


    public ArrayList<DocumentInfo> similaritySearch(String title, String author, int k) {
        return DomainCtrl.getInstance().similarDocumentsSearch(title, author, k);
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
        try {
            DomainCtrl.getInstance().saveDocument(title, author, content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveAsDocument(DocumentInfo docToBeSaved) {
        try {
            DomainCtrl.getInstance().saveAsDocument(docToBeSaved);
        } catch (Exception e) {
            showExceptionAlert(e.getMessage());
        }
    }

    public void deleteDocument(String title, String author) {
        DomainCtrl.getInstance().deleteDocument(title, author);
    }

    public void deleteDocument(DocumentInfo documentInfo) {
        mainViewCtrl.closeTab(documentInfo.getTitle(), documentInfo.getAuthor());
        deleteDocument(documentInfo.getTitle(), documentInfo.getAuthor());
    }

    public void export(DocumentInfo docToBeSaved) {
        try {
            DomainCtrl.getInstance().exportDocument(docToBeSaved);
        } catch (Exception e) {
            showExceptionAlert(e.getMessage());
        }
    }

    public DocumentInfo importDocument(String path, Format format) {
        try {
            return DomainCtrl.getInstance().importDocumentFromFile(path, format);
        } catch (Exception e) {
            showExceptionAlert(e.getMessage());
        }
        return null;
    }

    /**
     * Data Persistance Functions
     */
    public void doBackup() {
        try {
            DomainCtrl.getInstance().saveData();
        } catch (Exception e) {
            showExceptionAlert(e.getMessage());
        }
    }

    public void restoreBackup() throws Exception {
        DomainCtrl.getInstance().loadData();
    }

    public void deleteBackup() {
        try {
            DomainCtrl.getInstance().clearAllData();
        } catch (Exception e) {
            showExceptionAlert(e.getMessage());
        }
    }

    //TODO: afegir funcio per eliminar tot el contingut del sistema
    public void deleteData() {
    }

    public void closeAllTabs() {
        mainViewCtrl.closeAllTabs();
    }

    /**
     * Tractament de les excepcions
     */

    public void setMessage(String message) {
        mainViewCtrl.setMessage(message);
    }
    public void showExceptionAlert (String message) {
        mainViewCtrl.showExceptionAlert(message);
    }

    public void doSimilaritySearch(String title, String author) {
        mainViewCtrl.doSimilaritySearch(title, author);
    }
}

