package src.presentation;

import javafx.fxml.FXML;
import src.domain.controllers.DomainCtrl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;
import src.enums.Format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PresentationCtrl {
    private static PresentationCtrl instance;

    public PresentationCtrl() {}

    public static PresentationCtrl getInstance() {
        if (instance == null) {
            instance = new PresentationCtrl();
        }
        return instance;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Functions to controll the UI
     */

    /**
     *
     * @param newScene
     * @throws IOException
     */
    private void switchScene(String newScene) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(newScene));
        Stage newStage = new Stage();
        //newStage.setMaxWidth(600);
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

    public ArrayList<String> getTitles(String author) {
        return DomainCtrl.getInstance().getAllAuthorTitles(author);
    }

    public ArrayList<String> getAllAuthors() {
        return DomainCtrl.getInstance().getAllAuthors(new String());
    }

    public ArrayList<String> getAuthors(String title) {
        return DomainCtrl.getInstance().getAllTitleAuthors(title);
    }

    public DocumentInfo openDocument(String title, String author) {
        return null;
    }

    /**
     * Persistance related functions
     */

    /**
     *
     * @param title
     * @param author
     * @param content
     */
    public void saveDocument(DocumentInfo docToBeSaved) {
        DomainCtrl.getInstance().saveDocument(docToBeSaved);
    }

    public void importDocument(String path, Format format) {
        DomainCtrl.getInstance().importDocumentFromFile(path, format);
    }

    public void exportDocument() {

    }


    public void doBackup() {
        DomainCtrl.getInstance().saveData();
    }

    public void restoreBackup() throws Exception {
        DomainCtrl.getInstance().loadData();

    }

    //TODO: afegir funcio per eliminar els fitxers de data
    public void deleteBackup() {

    }

    //TODO: afegir funcio per eliminar tot el contingut del sistema
    public void deleteData() {

    }

    public ArrayList<DocumentInfo> tempBooleanExpressionSearch(String boolExp)throws Exception{
        return DomainCtrl.getInstance().tempBooleanExpressionSearch(boolExp);
    }


}
