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
import src.helpers.Maths;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ResultTableCtrl implements Initializable{
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

    List<DocumentInfo> docsList = new ArrayList<>();

    private int size = 100;

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
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    DocumentInfo rowData = row.getItem();
                    try {
                        PresentationCtrl.getInstance().openDocument(rowData);
                    } catch (IOException e) {
                        PresentationCtrl.getInstance().setMessage("ERROR: document not opened");
                        System.out.println("ERROR: document not opened");
                    }
                }
            });
            return row ;
        });

        docsList = PresentationCtrl.getInstance().getAllDocuments();
        updateTable(PresentationCtrl.getInstance().getAllDocuments());
    }


    @FXML
    private void openDocument() {
        try {
            PresentationCtrl.getInstance().openDocument(table.getFocusModel().getFocusedItem());
        } catch (IOException e) {
            PresentationCtrl.getInstance().setMessage("ERROR: document not opened");
        }
    }

    @FXML
    private void deleteDocument() {
        PresentationCtrl.getInstance().deleteDocument(table.getFocusModel().getFocusedItem());
        table.getItems().remove(table.getFocusModel().getFocusedCell().getRow());
    }

    @FXML
    private void searchSimilars() {
        PresentationCtrl.getInstance().doSimilaritySearch(
                table.getFocusModel().getFocusedItem().getTitle(),table.getFocusModel().getFocusedItem().getAuthor());
        table.getItems().remove(table.getFocusModel().getFocusedCell().getRow());
    }



    public void setForTitleAuthor() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }
    
    public void setForAllDocs() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(true);
        tableCreation.setVisible(true);
    }

    public void setForBoolean() {
        tableSimilarity.setVisible(false);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }
    
    public void setForSimilarity() {
        tableSimilarity.setVisible(true);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }
    
    public void setForWeighted() {
        tableSimilarity.setVisible(true);
        tableModification.setVisible(false);
        tableCreation.setVisible(false);
    }

    public void setK(int k) {
        this.size = k;
        int rows = Math.min(k, docsList.size());
        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(docsList.subList(0, rows));
        table.setItems(obsDocs);
    }

    public void updateTable(List<DocumentInfo> newDocsList) {
        this.docsList = newDocsList.stream().map(d -> d.withSimilarity(Maths.round(100*d.getSimilarity(), 2))).collect(Collectors.toList());
        int k = Math.min(this.size, docsList.size());
        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(docsList.subList(0, k));
        table.setItems(obsDocs);
    }

    public void updateTable(ArrayList<DocumentInfo> newDocsList, int k) {
        System.out.println("Numero "+ k);
        if(newDocsList.size()<k) k=newDocsList.size();
        ObservableList<DocumentInfo> obsDocs = FXCollections.observableArrayList(newDocsList.subList(0,k));
        table.setItems(obsDocs);
    }


    public void clearTable() {
        table.getItems().clear();
    }
}



