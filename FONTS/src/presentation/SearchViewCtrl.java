package src.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;

public class SearchViewCtrl {

    @FXML
    private ComboBox<String> authorBox;
    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private Button closeButton;

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

        vbox.getChildren().add(1,table);

        //resultPane.getChildren().add(table);
        resultTableCtrl = loader.getController();



        listDocs = PresentationCtrl.getInstance().getAllDocuments();
        listTitles = PresentationCtrl.getInstance().getAllTitles();
        listAuthors = PresentationCtrl.getInstance().getAllAuthors();

        //resultTableCtrl.updateTable(listDocs);

        ObservableList<String> obsTitles = FXCollections.observableArrayList(listTitles);
        ObservableList<String> obsAuthors = FXCollections.observableArrayList(listAuthors);

        titleBox.setItems(obsTitles);
        authorBox.setItems(obsAuthors);

        titleBox.setOnAction(event -> {
            if (authorBox.getValue() != null) {
                titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(authorBox.getValue())));
            }
            //resultTableCtrl.updateTable(PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), authorBox.getValue()));
        });


        titleBox.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                resultTableCtrl.updateTable(PresentationCtrl.getInstance().getDocuments(newValue, authorBox.getValue()));
            }
        });


        titleBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                titleBox.setValue(titleBox.getEditor().getText());
            }
        });

        authorBox.setOnAction(event -> {
            if (titleBox.getValue() != null) {
                authorBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getAuthors(titleBox.getValue())));
            }
        });


        authorBox.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                resultTableCtrl.updateTable(PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), newValue));
            }
        });

        authorBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                authorBox.setValue(authorBox.getEditor().getText());
            }
        });
    }

    /*
    public void initializeTable() throws IOException {
        System.out.println("es carregara la taula");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        System.out.println("s'ha carregat");
        resultPane.getChildren().add(loader.load());
        resultTableCtrl = loader.getController();
    }

     */

    @FXML
    private void close(ActionEvent e) {
        Stage stage = (Stage) closeButton.getScene().getWindow();

        stage.close();
    }
}
