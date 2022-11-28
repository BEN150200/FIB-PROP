package src.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import src.domain.core.DocumentInfo;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ResultTable {
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

    //ArrayList<DocumentInfo> docsList = new ArrayList<>();

    public void initialize() {
        tableTitle.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("title"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("author"));
        tableCreation.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("creationDate"));
        tableModification.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("modificationDate"));

        //docsList = PresentationCtrl.getInstance().getAllDocuments();
        updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    public void updateTable(ArrayList<DocumentInfo> newDocsList) {
        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(newDocsList);
        table.setItems(obsDocs);
    }
}



