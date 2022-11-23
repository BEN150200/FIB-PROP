package src.presentation;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import src.domain.core.DocumentInfo;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchStageCtrl implements Initializable {

    @FXML
    private ComboBox<String> authorBox;
    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private TableView<DocumentInfo> table;
    @FXML
    private TableColumn<DocumentInfo, String> tableTitle;
    @FXML
    private TableColumn<DocumentInfo, String> tableAuthor;
    @FXML
    private TableColumn<DocumentInfo, LocalDateTime> tableCreation;
    @FXML
    private TableColumn<DocumentInfo, LocalDateTime> tableModification;

    @FXML
    private Button closeButton;

    private String currentTitle;
    private String currentAuthor;

    //private AutoCompletionBinding<String> autoCompTitle;
    //private AutoCompletionBinding<String> autoCompAuthor;
    //private SuggestionProvider<String> titleProvider;
    //private SuggestionProvider<String> authorProvider;
    ArrayList<DocumentInfo> listDocs = new ArrayList<>();
    ArrayList<String> listTitles = new ArrayList<>();
    ArrayList<String> listAuthors = new ArrayList<>();

    public SearchStageCtrl() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableTitle.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("title"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("author"));
        tableCreation.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("creationDate"));
        tableModification.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("modificationDate"));


        listDocs = PresentationCtrl.getInstance().getAllDocuments();
        listTitles = PresentationCtrl.getInstance().getAllTitles();
        listAuthors = PresentationCtrl.getInstance().getAllAuthors();

        ObservableList<String> obsTitles = FXCollections.observableArrayList(listTitles);
        ObservableList<String> obsAuthors = FXCollections.observableArrayList(listAuthors);

        titleBox.setItems(obsTitles);
        authorBox.setItems(obsAuthors);
        /*
        titleProvider = SuggestionProvider.create(listTitles);
        new AutoCompletionTextFieldBinding<>(titleBox, titleProvider).setDelay(0);

        authorProvider = SuggestionProvider.create(listAuthors);
        new AutoCompletionTextFieldBinding<>(authorBox, authorProvider).setDelay(0);
         */
        titleBox.setOnAction(event -> {
            if (authorBox.getValue() != null) {
                /*
                titleProvider.clearSuggestions();
                titleProvider.addPossibleSuggestions(PresentationCtrl.getInstance().getTitles(authorBox.getText()));
                new AutoCompletionTextFieldBinding<>(titleBox, titleProvider);
                 */

                titleBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getTitles(authorBox.getValue())));
            }
            listDocs = PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), authorBox.getValue());
            ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(listDocs);
            table.setItems(obsDocs);
        });

        titleBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                titleBox.setValue(titleBox.getEditor().getText());
            }
        });

        authorBox.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                authorBox.setValue(authorBox.getEditor().getText());
            }
        });


        authorBox.setOnAction(event -> {
            if (titleBox.getValue() != null) {
                /*
                authorProvider.clearSuggestions();
                authorProvider.addPossibleSuggestions(PresentationCtrl.getInstance().getAuthors(titleBox.getText()));
                new AutoCompletionTextFieldBinding<>(authorBox, authorProvider);
                */
                authorBox.setItems(FXCollections.observableArrayList(PresentationCtrl.getInstance().getAuthors(titleBox.getValue())));
            }
            listDocs = PresentationCtrl.getInstance().getDocuments(titleBox.getValue(), authorBox.getValue());
            ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(listDocs);
            table.setItems(obsDocs);
        });

        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(listDocs);
        table.setItems(obsDocs);


    }

    @FXML
    private void close(ActionEvent e) {
        Stage stage = (Stage) closeButton.getScene().getWindow();


        stage.close();
    }
}
