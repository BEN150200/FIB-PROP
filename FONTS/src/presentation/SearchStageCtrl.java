package src.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchStageCtrl {

    /*
    @FXML
    AutoCompleteTextField titleField;
    @FXML
    AutoCompleteTextField authorField;

     */
    @FXML
    ChoiceBox authorBox;

    @FXML
    ChoiceBox titleBox;

    @FXML
    TableColumn tableTitle;

    @FXML
    TableColumn tableAuthor;

    @FXML
    TableColumn tableCreation;

    @FXML
    TableColumn tableModification;


    @FXML
    HBox fieldBox;

    @FXML
    Button closeButton;
    @FXML
    TableView table;


    ArrayList<DocumentInfo> allDocs = new ArrayList<>();
    ArrayList<String> allTitles = new ArrayList<>();
    ArrayList<String> allAuthors = new ArrayList<>();

    public SearchStageCtrl() {
    }

    public void initialize() {

        allDocs = PresentationCtrl.getInstance().getAllDocuments();
        allTitles = PresentationCtrl.getInstance().getAllTitles();
        allAuthors = PresentationCtrl.getInstance().getAllAuthors();

        ObservableList<String> oTitles = FXCollections.observableArrayList(allTitles);
        titleBox.setItems(oTitles);

        ObservableList<String> oAuthors = FXCollections.observableArrayList(allAuthors);
        authorBox.setItems(oAuthors);

        titleBox.setOnAction(event -> {
            //authorBox.getItems().clear();
            //The above line is important otherwise everytime there is an action it will just keep adding more
            if(titleBox.getValue()!=null & authorBox.getValue() == null) {//This cannot be null but I added because idk what yours will look like
                ArrayList<String> authors = PresentationCtrl.getInstance().getAuthors((String) titleBox.getValue());
                ObservableList<String> authorsTitle = FXCollections.observableArrayList(authors);
                authorBox.setItems(authorsTitle);
            }
        });

        authorBox.setOnAction(event -> {
            //titleBox.getItems().clear();
            //The above line is important otherwise everytime there is an action it will just keep adding more
            if(authorBox.getValue()!=null & titleBox.getValue() == null) {//This cannot be null but I added because idk what yours will look like
                ArrayList<String> titles = PresentationCtrl.getInstance().getTitles((String) authorBox.getValue());
                ObservableList<String> titlesAuthor = FXCollections.observableArrayList(titles);
                titleBox.setItems(titlesAuthor);
            }
        });





        /*
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titleName"));

        TableColumn authorCol = new TableColumn("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));


        //TableColumn creationCol = new TableColumn("Creation Date");
        //creationCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        //TableColumn modificationCol = new TableColumn("Modification Date");
        //modificationCol.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));

        table.getColumns().addAll(titleCol, authorCol);

        for (DocumentInfo doc: allDocs) {
            table.getItems().add(doc);
            //table.getColumns().addAll(doc.title(),doc.author(),doc.creationDate(),doc.modificationDate());
        }

         */
    }

    @FXML
    private void close(ActionEvent e) {
        Stage stage = (Stage) closeButton.getScene().getWindow();


        stage.close();
    }

    @FXML
    private void getTitles() {
        /*
        allTitles = PresentationCtrl.getInstance().getAllTitles();
        //System.out.println("eieei");
        //titleField.getEntries().addAll(allTitles);

        ObservableList<String> oTitles = FXCollections.observableArrayList(allTitles);
        titleBox.setItems(oTitles);
         */
    }

    @FXML
    private void getAuthors() {
        /*
        allAuthors = PresentationCtrl.getInstance().getAllAuthors();

        //authorField.getEntries().addAll(allAuthors);
        ObservableList<String> oAuthors = FXCollections.observableArrayList(allAuthors);
        authorBox.setItems(oAuthors);
         */
    }


}
