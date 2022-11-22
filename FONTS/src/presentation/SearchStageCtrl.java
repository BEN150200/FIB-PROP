package src.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;

import java.util.ArrayList;

public class SearchStageCtrl {


    //AutocompletionlTextField titleField;

    //AutocompletionlTextField authorField;

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
        AutocompletionlTextField titleField = new AutocompletionlTextField();
        AutocompletionlTextField authorField = new AutocompletionlTextField();
        fieldBox.getChildren().add(titleField);
        fieldBox.getChildren().add(authorField);
    }

    public void initialize() {

        allDocs = PresentationCtrl.getInstance().getAllDocuments();
        allTitles = PresentationCtrl.getInstance().getAllTitles();
        allAuthors = PresentationCtrl.getInstance().getAllAuthors();

        //titleField.setEntries(allTitles);
        //authorField.setEntries(allAuthors);

        //fieldBox.getChildren().add(titleField);
        //fieldBox.getChildren().add(authorField);

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


}
