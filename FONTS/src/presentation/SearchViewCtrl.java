package src.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;

public class SearchViewCtrl {

    @FXML
    private ComboBox<String> authorBox;
    @FXML
    private ComboBox<String> titleBox;

    //@FXML
    //private Button closeButton;

    @FXML
    private VBox resultPane;

    @FXML
    private VBox vbox;

    private ResultTable resultTableCtrl;

    private String currentTitle;
    private String currentAuthor;

    ArrayList<DocumentInfo> listDocs = new ArrayList<>();
    ArrayList<String> listTitles = new ArrayList<>();
    ArrayList<String> listAuthors = new ArrayList<>();

    public SearchViewCtrl() {
    }

    public void initialize() throws IOException{
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();

        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(1,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        //resultPane.getChildren().add(table);
        resultTableCtrl = loader.getController();

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


        titleBox.setItems(obsTitles);
        authorBox.setItems(obsAuthors);
    }

    private void setListeners() {

        //action
        titleBox.setOnAction(event -> {

        });
        authorBox.setOnAction(event -> {

        });

        /*
        titleBox.setOnShown(event -> {
            if (authorBox.getValue() != null) {
                titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getValue())));
            }
        });
         */

        //funcions per modificar contingut cuan s'entri text
        titleBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                resultTableCtrl.updateTable(PresentationCtrl.getInstance().getDocuments(newValue, authorBox.getValue()));
                //titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(titleBox.getValue())));
            }
        });
        authorBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                resultTableCtrl.updateTable(PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), newValue));
                //authorBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(authorBox.getValue())));
            }
        });

        //funcio per desplegar les opcions quan es faci focus
        titleBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                titleBox.setValue(titleBox.getEditor().getText());
                titleBox.hide();
            }
            else titleBox.show();
        });
        authorBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                authorBox.setValue(authorBox.getEditor().getText());
                authorBox.hide();
            }
            else authorBox.show();
        });

        titleBox.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBox.show();
        });
        authorBox.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBox.show();
        });
    }
}
