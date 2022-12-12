package src.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ResultTable implements Initializable{
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
    private TableColumn<DocumentInfo, String> tableName;
    @FXML
    private TableColumn<DocumentInfo, Double> tableSimilarity;

    //ArrayList<DocumentInfo> docsList = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableTitle.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("title"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("author"));
        tableCreation.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("creationDate"));
        tableModification.setCellValueFactory(new PropertyValueFactory<DocumentInfo, LocalDateTime>("modificationDate"));
        tableName.setCellValueFactory(new PropertyValueFactory<DocumentInfo, String>("fileName"));
        tableSimilarity.setCellValueFactory(new PropertyValueFactory<DocumentInfo, Double>("similarity"));

        table.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {
                TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        table.setRowFactory(tv -> {
            TableRow<DocumentInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    DocumentInfo rowData = row.getItem();
                    try {
                        PresentationCtrl.getInstance().openDocument(rowData);
                    } catch (IOException e) {
                        PresentationCtrl.getInstance().setError("ERROR: document not opened");
                        System.out.println("ERROR: document not opened");
                    }
                }
            });
            return row ;
        });

        //docsList = PresentationCtrl.getInstance().getAllDocuments();
        updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }

    public void setForTitleAuthorSearch() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }

    public void setForAllDocs() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }

    public void setForSimilarity() {
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
        tableName.setVisible(false);
    }

    public void setForWeightedSearch() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }

    public void updateTable(ArrayList<DocumentInfo> newDocsList) {
        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(newDocsList);
        table.setItems(obsDocs);
    }

    public void clearTable() {
        table.getItems().clear();
    }
}



