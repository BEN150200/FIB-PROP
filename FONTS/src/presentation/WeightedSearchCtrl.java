package src.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class WeightedSearchCtrl {
    @FXML
    private TextField input;

    @FXML
    private VBox layout;

    @FXML
    private Spinner<Integer> numSpinner;

    private int currentNum;

    private ResultTableCtrl resultTableCtrl;

    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();
        currentNum = 5;

        VBox.setVgrow(table, Priority.ALWAYS);
        layout.getChildren().add(1,table);
        VBox.setVgrow(layout, Priority.ALWAYS);
        SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinnerFactory.setValue(currentNum);
        numSpinner.setValueFactory(spinnerFactory);


        resultTableCtrl = loader.getController();
        resultTableCtrl.setForWeighted();
        resultTableCtrl.setK(currentNum);

        setListeners();
    }

    private void setListeners() {
        layout.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                search(input.getText());
            }
        });

        numSpinner.valueProperty().addListener((_1, _2, k) -> resultTableCtrl.setK(k));

        input.textProperty().addListener((_1, _2, query) -> search(query));
    }

    @FXML
    private void search(String query) {
        if (query.isEmpty()) resultTableCtrl.clearTable();
        else {
            PresentationCtrl.getInstance()
                .weightedSearch(query)
                .peek(
                    error -> {
                        resultTableCtrl.clearTable();
                        PresentationCtrl.getInstance().setMessage(error);
                    },
                    result -> {
                        if(result.isEmpty())
                            PresentationCtrl.getInstance().setMessage("No document matches the search");
                        
                        resultTableCtrl.updateTable(result,numSpinner.getValue());
                    }
                );
        }
    }

    @FXML
    private void search() {
        search(input.getText());
    }
}
