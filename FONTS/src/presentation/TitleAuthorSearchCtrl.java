package src.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;

public class TitleAuthorSearchCtrl {

    @FXML
    private ComboBox<String> authorBox;
    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private TextField authorField;
    @FXML
    private TextField titleField;

    @FXML
    private VBox vbox;

    private ResultTable resultTableCtrl;

    private String currentTitle;
    private String currentAuthor;

    ArrayList<DocumentInfo> listDocs = new ArrayList<>();
    ArrayList<String> listTitles = new ArrayList<>();
    ObservableList<String> obsTitles = FXCollections.observableArrayList();
    ArrayList<String> listAuthors = new ArrayList<>();
    ObservableList<String> obsAuthors = FXCollections.observableArrayList();

    FilteredList<String> filteredTitles = new FilteredList<>(obsTitles);
    FilteredList<String> filteredAuthors = new FilteredList<>(obsAuthors);


    public TitleAuthorSearchCtrl() {
    }

    public void initialize() throws IOException{
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();

        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(2,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        Font f = new Font(14);
        titleBox.getEditor().setFont(f);
        authorBox.getEditor().setFont(f);

        //resultPane.getChildren().add(table);
        resultTableCtrl = loader.getController();
        resultTableCtrl.setForTitleAuthorSearch();
        setListeners();
        update();
    }

    public void update() {

        listDocs = PresentationCtrl.getInstance().getAllDocuments();
        listTitles = PresentationCtrl.getInstance().getAllTitles();
        listAuthors = PresentationCtrl.getInstance().getAllAuthors();

        if (titleBox.getValue() == null && authorBox.getValue() == null) resultTableCtrl.updateTable(listDocs);

        ObservableList<String> obsTitles = FXCollections.observableArrayList(listTitles);
        ObservableList<String> obsAuthors = FXCollections.observableArrayList(listAuthors);
        filteredTitles = new FilteredList<>(obsTitles);
        filteredAuthors = new FilteredList<>(obsAuthors);

        titleBox.setItems(filteredTitles);
        authorBox.setItems(filteredAuthors);


    }

    private void setListeners() {

        //action

        titleBox.setOnAction(event -> { });
        authorBox.setOnAction(event -> { });

        /*
        titleBox.setOnShown(event -> {
            if (authorBox.getValue() != null) {
                titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getValue())));
            }
        });
         */

        /*
        //funcions per modificar contingut cuan s'entri text
        titleBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(newValue, authorBox.getValue());
                if (docs.isEmpty()) PresentationCtrl.getInstance().setError("There is no document with this title and author");
                resultTableCtrl.updateTable(docs);
                //titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(titleBox.getValue())));
            }
        });
        authorBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), newValue);
                if (docs.isEmpty()) PresentationCtrl.getInstance().setError("There is no document with this title and author");
                resultTableCtrl.updateTable(docs);
                //authorBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(authorBox.getValue())));
            }
        });

         */



        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTitles.setPredicate(item -> {

                // If the TextField is empty, return all items in the original list
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Check if the search term is contained anywhere in our list
                if (item.toLowerCase().contains(newValue.toLowerCase().trim())) {
                    return true;
                }

                // No matches found
                return false;
            });
            ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(newValue, authorBox.getValue());
            if (docs.isEmpty()) PresentationCtrl.getInstance().setError("There is no document with this title and author");
            resultTableCtrl.updateTable(docs);
        });
        titleBox.setItems(filteredTitles);


        authorField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredAuthors.setPredicate(item -> {

                // If the TextField is empty, return all items in the original list
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Check if the search term is contained anywhere in our list
                if (item.toLowerCase().contains(newValue.toLowerCase().trim())) {
                    return true;
                }

                // No matches found
                return false;
            });
            ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), newValue);
            if (docs.isEmpty()) PresentationCtrl.getInstance().setError("There is no document with this title and author");
            resultTableCtrl.updateTable(docs);
        });
        authorBox.setItems(filteredAuthors);

        //funcio per desplegar les opcions quan es faci focus
        titleField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused) {
                titleBox.setValue(titleField.getText());
                titleBox.hide();
            }
            else if (!wasFocused && isNowFocused) {
                if(titleBox.getItems().isEmpty()) PresentationCtrl.getInstance().setError("There are no titles in the System");
                titleBox.show();
            }
        });

        authorField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused) {
                authorBox.setValue(authorField.getText());
                authorBox.hide();
            }
            else if (!wasFocused && isNowFocused) {
                if(authorBox.getItems().isEmpty()) PresentationCtrl.getInstance().setError("There are no authors in the System");
                authorBox.show();
            }
        });

        titleField.setOnMouseClicked(mouseEvent -> {
            //titleBox.show();
            if(titleBox.getItems().isEmpty()) PresentationCtrl.getInstance().setError("There are no titles in the System");
            titleBox.show();
        });
        authorField.setOnMouseClicked(mouseEvent -> {
            //authorBox.show();
            if(authorBox.getItems().isEmpty()) PresentationCtrl.getInstance().setError("There are no authors in the System");
            authorBox.show();
        });
    }
}
