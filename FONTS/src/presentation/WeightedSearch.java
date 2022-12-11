package src.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;

public class WeightedSearch {
    @FXML
    private TextField input;

    @FXML
    private VBox vbox;

    @FXML
    private Spinner<Integer> numSpinner;

    private int currentNum;

    private ResultTable resultTableCtrl;

    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();
        currentNum = 5;

        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(1,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinnerFactory.setValue(currentNum);
        numSpinner.setValueFactory(spinnerFactory);


        resultTableCtrl = loader.getController();
        resultTableCtrl.setForWeightedSearch();

        setListeners();
    }

    private void setListeners() {
        input.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                search();
            }
        });
    }

    @FXML
    private void search() {
        if (input.getText().isEmpty()) resultTableCtrl.clearTable();
        else {
            ArrayList<DocumentInfo> docs = PresentationCtrl.getInstance().weightedSearch(input.getText(), numSpinner.getValue());
            if (docs.isEmpty()) PresentationCtrl.getInstance().setError("No document match the search");
            resultTableCtrl.updateTable(docs);
        }
    }

}
