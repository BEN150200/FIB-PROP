package src.presentation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SimilaritySearchCtrl {

    @FXML
    private ComboBox<String> authorBox;

    @FXML
    private Spinner<Integer> numSpinner;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private VBox vbox;

    private ResultTableCtrl resultTableCtrl;

    ArrayList<DocumentInfo> listDocs = new ArrayList<>();
    ArrayList<String> listTitles = new ArrayList<>();
    ObservableList<String> obsTitles = FXCollections.observableArrayList();
    ArrayList<String> listAuthors = new ArrayList<>();
    ObservableList<String> obsAuthors = FXCollections.observableArrayList();

    FilteredList<String> filteredTitles = new FilteredList<>(obsTitles);
    FilteredList<String> filteredAuthors = new FilteredList<>(obsAuthors);

    FilteredList<String> tempFiltTitles = new FilteredList<>(obsTitles);

    FilteredList<String> tempFiltdAuthors = new FilteredList<>(obsAuthors);

    private int currentNum;


    public SimilaritySearchCtrl() {
    }

    public void initialize() throws IOException {
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();

        currentNum = 5;

        SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinnerFactory.setValue(currentNum);
        numSpinner.setValueFactory(spinnerFactory);

        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(3,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        Font f = new Font(14);
        titleBox.getEditor().setFont(f);
        authorBox.getEditor().setFont(f);


        resultTableCtrl = loader.getController();
        resultTableCtrl.setForSimilarity();
        setListeners();
        update();
    }

    public void update() {


        //listTitles.add("");
        //listAuthors.add("");

        listTitles.addAll(PresentationCtrl.getInstance().getAllTitles());
        listAuthors.addAll(PresentationCtrl.getInstance().getAllAuthors());

        ObservableList<String> obsTitles = FXCollections.observableArrayList(listTitles);
        ObservableList<String> obsAuthors = FXCollections.observableArrayList(listAuthors);

        //filteredTitles = new FilteredList<String>(obsTitles, p -> true);
        //filteredAuthors = new FilteredList<String>(obsAuthors, p -> true);

        titleBox.setItems(obsTitles);
        authorBox.setItems(obsAuthors);


    }
    private void setListeners() {
        //action
        /*
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
            });
        });

         */
        /*
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

         */

        /*
        titleBox.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBox.show();
        });

        authorBox.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBox.show();
        });
         */

        titleBox.setOnAction(actionEvent ->  {
            if (authorBox.getValue().isEmpty()) {
                if (!titleBox.getValue().isEmpty()) {
                    ArrayList<String> tempAuthors = PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getValue());
                    //tempAuthors.add("");
                    //tempAuthors = PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getValue());
                    //FilteredList<String> tempFiltdAuthors = new FilteredList<>(FXCollections.observableArrayList(tempAuthors), p -> true);
                    authorBox.setItems(FXCollections.observableArrayList(tempAuthors));
                } else {
                    String temp = authorBox.getValue();
                    authorBox.getItems().clear();
                    authorBox.setItems(obsAuthors);
                    authorBox.setValue(temp);
                }
            }
            if (titleBox.getValue().isEmpty()) {
                String temp = authorBox.getValue();
                authorBox.getItems().clear();
                authorBox.setItems(obsAuthors);
                authorBox.setValue(temp);
            }
        });

        authorBox.setOnAction(actionEvent ->  {
            if (titleBox.getValue().isEmpty()) {
                if (!authorBox.getValue().isEmpty()) {
                    ArrayList<String> tempTitles = PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getValue());
                    //tempTitles.add("");
                    //tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));
                    //FilteredList<String> tempFiltTitles = new FilteredList<>(FXCollections.observableArrayList(tempTitles), p -> true);
                    titleBox.setItems(FXCollections.observableArrayList(tempTitles));
                }
                else {
                    String temp = titleBox.getValue();
                    titleBox.getItems().clear();
                    titleBox.setItems(obsTitles);
                    titleBox.setValue(temp);
                }
            }
            if (authorBox.getValue().isEmpty()) {
                String temp = titleBox.getValue();
                titleBox.setItems(obsTitles);
                titleBox.setValue(temp);
            }

        });


    }

    @FXML
    private void search() {
        if (!titleBox.getValue().isEmpty() && !authorBox.getValue().isEmpty()) {
            ArrayList<DocumentInfo> resultDocs = PresentationCtrl.getInstance().similaritySearch(titleBox.getValue(), authorBox.getValue(), numSpinner.getValue());
            if (resultDocs == null) PresentationCtrl.getInstance().setMessage("the document not exists or there are no similar ones");
            else resultTableCtrl.updateTable(resultDocs);
        }
    }

    public void search(String title, String author) {
        titleBox.setValue(title);
        authorBox.setValue(author);
        search();
    }

}
