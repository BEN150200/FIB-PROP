package src.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import src.domain.core.DocumentInfo;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchStageCtrl implements Initializable {

    @FXML
    private ChoiceBox authorBox;
    @FXML
    private ChoiceBox titleBox;
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
    private HBox fieldBox;

    @FXML
    private Button closeButton;


    private String currentTitle;
    private String currentAuthor;

    private AutoCompletionBinding<String> autoCompTitle;
    private AutoCompletionBinding<String> autoCompAuthor;
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

        ObservableList<String> oTitles = FXCollections.observableArrayList(listTitles);
        titleBox.setItems(oTitles);

        ObservableList<String> oAuthors = FXCollections.observableArrayList(listAuthors);
        authorBox.setItems(oAuthors);

        titleBox.setOnAction(event -> {
            //authorBox.getItems().clear();
            //The above line is important otherwise everytime there is an action it will just keep adding more
            /*
            if(titleBox.getValue()!=null & authorBox.getValue() == null) {//This cannot be null but I added because idk what yours will look like
                ArrayList<String> authors = PresentationCtrl.getInstance().getAuthors((String) titleBox.getValue());
                ObservableList<String> authorsTitle = FXCollections.observableArrayList(authors);
                authorBox.setItems(authorsTitle);
                currentTitle = (String)titleBox.getValue();
            }
            else if (currentAuthor != (String)authorBox.getValue()) {
                titleBox.getSelectionModel().clearSelection();
                ArrayList<String> authors = PresentationCtrl.getInstance().getAuthors((String) titleBox.getValue());
                ObservableList<String> authorsTitle = FXCollections.observableArrayList(authors);
                authorBox.setItems(authorsTitle);
                currentAuthor = (String)titleBox.getValue();
            }
             */
            if(titleBox.getValue()!=null & authorBox.getValue() == null) {
                listDocs = PresentationCtrl.getInstance().getDocuments((String)titleBox.getValue(), null);
            }
            else listDocs = PresentationCtrl.getInstance().getDocuments((String)titleBox.getValue(), (String)authorBox.getValue());
            ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(listDocs);
            table.setItems(obsDocs);
        });


        authorBox.setOnAction(event -> {
            //titleBox.getItems().clear();
            //The above line is important otherwise everytime there is an action it will just keep adding more
            /*
            if(authorBox.getValue()!=null & titleBox.getValue() == null) {//This cannot be null but I added because idk what yours will look like
                ArrayList<String> titles = PresentationCtrl.getInstance().getTitles((String) authorBox.getValue());
                ObservableList<String> titlesAuthor = FXCollections.observableArrayList(titles);
                titleBox.setItems(titlesAuthor);
                currentAuthor = (String)authorBox.getValue();
            }
            else if (currentTitle != (String)titleBox.getValue()) {
                authorBox.getSelectionModel().clearSelection();
                ArrayList<String> titles = PresentationCtrl.getInstance().getTitles((String) authorBox.getValue());
                ObservableList<String> titlesAuthor = FXCollections.observableArrayList(titles);
                titleBox.setItems(titlesAuthor);
                currentTitle = (String)authorBox.getValue();
            }
             */
            if(authorBox.getValue()!=null & titleBox.getValue() == null) {
                listDocs = PresentationCtrl.getInstance().getDocuments(null, (String)authorBox.getValue());
            }
            else listDocs = PresentationCtrl.getInstance().getDocuments((String)titleBox.getValue(), (String)authorBox.getValue());
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
