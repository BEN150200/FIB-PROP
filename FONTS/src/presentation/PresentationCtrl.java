package src.presentation;

import src.domain.controllers.DomainCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;

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
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void switchToSearch() throws IOException {
        switchScene("/search.fxml");
    }

    public void switchToText() throws IOException {

    }

    /**
     * Getters from the domain
     */
    public ArrayList<DocumentInfo> getAllDocuments() {
        return DomainCtrl.getInstance().getAllDocumentsInfo();
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

    /**
     *
     * @param title
     * @param author
     * @param content
     */

    public void saveDocument(String title, String author, String content) {
        List<String> strings = new ArrayList<>();
        strings = List.of(Tokenizer.splitSentences(content));
        DomainCtrl.getInstance().saveDocument(title, author, strings);
    }

    public void openDocument(String path) {
        DomainCtrl.getInstance().openFile(path);
    }

}
