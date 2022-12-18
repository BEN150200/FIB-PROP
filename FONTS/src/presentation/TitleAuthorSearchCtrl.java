package src.presentation;

import javafx.application.Platform;
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
import java.util.Objects;

public class TitleAuthorSearchCtrl {

    @FXML
    private ComboBox<String> authorBox;
    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private VBox vbox;

    private ResultTableCtrl resultTableCtrl;

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
        resultTableCtrl.setForTitleAuthor();
        setListeners();
        update();
    }

    public void update() {

        listDocs = PresentationCtrl.getInstance().getAllDocuments();

        listTitles.add("");
        listAuthors.add("");

        listTitles.addAll(PresentationCtrl.getInstance().getAllTitles());
        listAuthors.addAll(PresentationCtrl.getInstance().getAllAuthors());

        if (titleBox.getValue() == null && authorBox.getValue() == null) resultTableCtrl.updateTable(listDocs);

        ObservableList<String> obsTitles = FXCollections.observableArrayList(listTitles);
        ObservableList<String> obsAuthors = FXCollections.observableArrayList(listAuthors);

        filteredTitles = new FilteredList<String>(obsTitles, p -> true);
        filteredAuthors = new FilteredList<String>(obsAuthors, p -> true);

        titleBox.setItems(filteredTitles);
        authorBox.setItems(filteredAuthors);

    }


    private void setListeners() {

        /*
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
            if (docs.isEmpty()) PresentationCtrl.getInstance().setMessage("There is no document with this title and author");
            resultTableCtrl.updateTable(docs);
        });

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
            if (docs.isEmpty()) PresentationCtrl.getInstance().setMessage("There is no document with this title and author");
            resultTableCtrl.updateTable(docs);
        });
        */









        titleBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = titleBox.getEditor();
            final String selected = titleBox.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {

                if (selected == null || !selected.equals(editor.getText())) {
                    filteredTitles.setPredicate(item -> {

                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });

                }
                ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(newValue, authorBox.getEditor().getText());
                if (docs.isEmpty()) PresentationCtrl.getInstance().setMessage("There is no document with this title and author");
                resultTableCtrl.updateTable(docs);
            });
        });

        authorBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = authorBox.getEditor();
            final String selected = authorBox.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {

                if (selected == null || !selected.equals(editor.getText())) {
                    filteredAuthors.setPredicate(item -> {

                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });

                }
                ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().getDocuments(titleBox.getEditor().getText(), newValue);
                if (docs.isEmpty()) PresentationCtrl.getInstance().setMessage("There is no document with this title and author");
                resultTableCtrl.updateTable(docs);
            });
        });




        titleBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused) {
                titleBox.hide();
            }
            else if (!wasFocused && isNowFocused) {
                if (titleBox.getItems().isEmpty())
                    PresentationCtrl.getInstance().setMessage("There are no titles in the System");
                titleBox.show();
            }
        });

        authorBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused) {
                authorBox.hide();
            }
            else if (!wasFocused && isNowFocused) {
                if (authorBox.getItems().isEmpty())
                    PresentationCtrl.getInstance().setMessage("There are no titles in the System");
                authorBox.show();
            }
        });

        titleBox.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBox.show();
        });

        authorBox.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBox.show();
        });

        titleBox.setOnAction(actionEvent ->  {
            if (authorBox.getEditor().getText().isEmpty()) {
                if (!Objects.equals(titleBox.getEditor().getText(), "") || !titleBox.getEditor().getText().isEmpty()) {
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));
                    FilteredList<String> tempFiltdAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                    authorBox.setItems(tempFiltdAuthors);
                } else {
                    String temp = authorBox.getEditor().getText();
                    authorBox.getItems().clear();
                    authorBox.setItems(filteredAuthors);
                    authorBox.getEditor().setText(temp);
                }
            }
            if (Objects.equals(titleBox.getEditor().getText(), "") || titleBox.getEditor().getText().isEmpty()) {
                String temp = authorBox.getEditor().getText();
                authorBox.getItems().clear();
                authorBox.setItems(filteredAuthors);
                authorBox.getEditor().setText(temp);
            }
        });

        authorBox.setOnAction(actionEvent ->  {
            if (titleBox.getEditor().getText().isEmpty()) {
                if (!Objects.equals(authorBox.getEditor().getText(), "") || !authorBox.getEditor().getText().isEmpty()) {
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                    FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                    titleBox.setItems(tempFiltTitles);
                }
                else {
                    String temp = titleBox.getEditor().getText();
                    titleBox.getItems().clear();
                    titleBox.setItems(filteredTitles);
                    titleBox.getEditor().setText(temp);
                }
            }
            if (Objects.equals(authorBox.getEditor().getText(), "") || authorBox.getEditor().getText().isEmpty()) {
                String temp = titleBox.getEditor().getText();
                titleBox.setItems(filteredTitles);
                titleBox.getEditor().setText(temp);
            }

        });

    }
}
