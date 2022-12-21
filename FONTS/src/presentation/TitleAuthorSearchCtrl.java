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
     * stores if the list of the titles in the titleBox is the modified one
     */
    private boolean titleMod;
    /**
     * stores if the list of the authors in the authorBox is the modified one
     */
    private boolean authorMod;

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

        titleMod = false;
        authorMod = false;
    }

    /**
     * set the listeners for the two combobox
     */
    private void setListeners() {
        //listener to filter
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

        //listeners to check focus
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

        //listeners to open on mouse click
        titleBox.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBox.show();
        });
        authorBox.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBox.show();
        });



        //listeners to change the content of the list
        titleBox.setOnAction(actionEvent ->  {
            try {
                if (Objects.equals(titleBox.getEditor().getText(), "") && authorMod) {
                    authorBox.setItems(filteredAuthors);
                    authorBox.setVisibleRowCount(Math.min(listAuthors.size(), 10));
                    authorMod = false;
                }

                else if (authorMod && !titleMod) {
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));
                    FilteredList<String> tempFiltAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                    authorBox.setItems(tempFiltAuthors);
                    authorBox.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                }
                else if (Objects.equals(authorBox.getEditor().getText(), "") && !titleMod) {
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));
                    FilteredList<String> tempFiltAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                    authorBox.setItems(tempFiltAuthors);
                    authorBox.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                    authorMod = true;
                }
                else if (Objects.equals(authorBox.getEditor().getText(), "") && titleMod) {
                    titleBox.setItems(FXCollections.observableArrayList(listTitles));
                    titleBox.setVisibleRowCount(Math.min(listTitles.size(), 10));
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));
                    FilteredList<String> tempFiltAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                    authorBox.setItems(tempFiltAuthors);
                    authorBox.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                    authorMod = true;
                    titleMod = false;
                }
                else if (Objects.equals(authorBox.getEditor().getText(), "") &&
                        Objects.equals(titleBox.getEditor().getText(), "")) {
                    titleMod = false;
                    authorMod = false;
                }
            }
            catch (StackOverflowError e) {

            }
        });

        authorBox.setOnAction(actionEvent ->  {
            try {
                //if the author is not selected but the title modified, restore the title
                if (Objects.equals(authorBox.getEditor().getText(), "") && titleMod) {
                    titleBox.setItems(filteredTitles);
                    titleBox.setVisibleRowCount(Math.min(listTitles.size(), 10));
                    titleMod = false;
                }
                //if title is modified and author not change the modified title list by the new one
                else if (titleMod && !authorMod) {
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                    FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                    titleBox.setItems(tempFiltTitles);
                    titleBox.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                }
                //else if it is not selected, modifie it
                else if (Objects.equals(titleBox.getEditor().getText(), "") && !authorMod) {
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                    FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                    titleBox.setItems(tempFiltTitles);
                    titleBox.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                    titleMod = true;
                } else if (Objects.equals(titleBox.getEditor().getText(), "") && authorMod) {
                    authorBox.setItems(FXCollections.observableArrayList(listAuthors));
                    authorBox.setVisibleRowCount(Math.min(listAuthors.size(), 10));
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                    FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                    titleBox.setItems(tempFiltTitles);
                    titleBox.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                    titleMod = true;
                    authorMod = false;
                }
                //if the two ar not selected, neither is modified
                else if (Objects.equals(authorBox.getEditor().getText(), "") &&
                        Objects.equals(titleBox.getEditor().getText(), "")) {
                    titleMod = false;
                    authorMod = false;
                }
            }
            catch (StackOverflowError e) {

            }

        });

    }

}
