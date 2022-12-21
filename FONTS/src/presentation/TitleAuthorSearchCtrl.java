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

    private final ArrayList<String> listTitles = new ArrayList<>();
    private final ArrayList<String> listAuthors = new ArrayList<>();

    private FilteredList<String> filteredTitles;
    private FilteredList<String> filteredAuthors;

    /**
     * Creator
     */
    public TitleAuthorSearchCtrl() {
    }

    /**
     * initialize all the parameters and elements and update the content
     */
    public void initialize() {
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table;
        try {
            table = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(2,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        resultTableCtrl = loader.getController();
        resultTableCtrl.setForTitleAuthor();

        Font f = new Font(14);
        titleBox.getEditor().setFont(f);
        authorBox.getEditor().setFont(f);

        setListeners(); //set listeners
        update(); //Update the title and author lists
    }

    /**
     * Update the list of the titles, authors and documents
     */
    public void update() {

        ArrayList<DocumentInfo> listDocs = PresentationCtrl.getInstance().getAllDocuments();
        if (Objects.equals(titleBox.getEditor().getText(), "") && Objects.equals(authorBox.getEditor().getText(), ""))
            resultTableCtrl.updateTable(listDocs);

        listTitles.clear();
        listTitles.add("");
        listTitles.addAll(PresentationCtrl.getInstance().getAllTitles());

        listAuthors.clear();
        listAuthors.add("");
        listAuthors.addAll(PresentationCtrl.getInstance().getAllAuthors());

        filteredTitles = new FilteredList<>(FXCollections.observableArrayList(listTitles), p -> true);
        filteredAuthors = new FilteredList<>(FXCollections.observableArrayList(listAuthors), p -> true);

        titleBox.setItems(filteredTitles);
        authorBox.setItems(filteredAuthors);
    }

    /**
     * set the listeners for the two combobox
     */
    private void setListeners() {
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
            if (Objects.equals(titleBox.getEditor().getText(), "") || titleBox.getEditor().getText().isEmpty()) {
                String temp = authorBox.getEditor().getText();
                authorBox.setItems(filteredAuthors);
                authorBox.getEditor().setText(temp);
            }

            else if (authorBox.getEditor().getText().isEmpty()) {
                ArrayList<String> tempAuthors = new ArrayList<>();
                tempAuthors.add("");
                tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));
                FilteredList<String> tempFiltAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                authorBox.setItems(tempFiltAuthors);

            }

        });



        authorBox.setOnAction(actionEvent ->  {
            if (Objects.equals(authorBox.getEditor().getText(), "") || authorBox.getEditor().getText().isEmpty()) {
                String temp = titleBox.getEditor().getText();
                titleBox.setItems(filteredTitles);
                titleBox.getEditor().setText(temp);
            }

            else if (titleBox.getEditor().getText().isEmpty()) {
                ArrayList<String> tempTitles = new ArrayList<>();
                tempTitles.add("");
                tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                titleBox.setItems(tempFiltTitles);

            }


        });

    }

}
