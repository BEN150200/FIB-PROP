package src.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooleanExprSearchCtrl {
    @FXML
    private TextField expression;

    @FXML
    private ComboBox<String> name;

    private ResultTableCtrl resultTableCtrl;

    private HashMap<String,String> expressions = new HashMap<>();
    @FXML
    private VBox vbox;

    public void initialize() throws IOException {
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table = loader.load();

        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(1,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        resultTableCtrl = loader.getController();
        resultTableCtrl.setForBoolean();

        name.getEditor().setOnMouseClicked(mouseEvent -> {
            name.show();
        });
    }
    public void setExpression(String expression) {
        this.expression.setText(expression);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void SearchBooleanExpression(){ //realitza la cerca, si és una expressió no guardada crea l'arbre
        try {
            if(PresentationCtrl.getInstance().existsBooleanExpression(name.getValue()) &&
                    PresentationCtrl.getInstance().getExpression(name.getValue()).equals(expression.getText())){
                resultTableCtrl.updateTable( PresentationCtrl.getInstance().savedBooleanExpressionSearch(name.getValue()));
            }
            else {
                resultTableCtrl.updateTable( PresentationCtrl.getInstance().booleanExpressionSearch(expression.getText()));
            }
            PresentationCtrl.getInstance().setMessage("Search done");
        }
        catch (Exception e){
            PresentationCtrl.getInstance().showExceptionAlert(e.getMessage());
        }
    }


    public void ModifyExpression(){ //modifica la expressió amb aquest nom si la nova expressio es correcte
        if(!PresentationCtrl.getInstance().existsBooleanExpression(name.getValue())){
            PresentationCtrl.getInstance().showExceptionAlert("The expression "+ name.getValue() +" does not exists");
            return;
        }
        try{
            PresentationCtrl.getInstance().addBooleanExpression (name.getValue(),  expression.getText());
            PresentationCtrl.getInstance().setMessage("Expression modified");
        }
        catch (Exception e){
            PresentationCtrl.getInstance().showExceptionAlert(e.getMessage());
        }
    }
    public void SaveExpression(){ //guarda la expressió si no existeix cap amb aquest nom i és correcte

        if(name.getValue().length()==0) {
            PresentationCtrl.getInstance().showExceptionAlert("The expression needs a name to be saved");
            return;
        }
        if(PresentationCtrl.getInstance().existsBooleanExpression(name.getValue())){
            PresentationCtrl.getInstance().showExceptionAlert("There is already a expression named " + name.getValue());
            return;
        }
        try{
            PresentationCtrl.getInstance().addBooleanExpression (name.getValue(),  expression.getText());
            PresentationCtrl.getInstance().setMessage("Expression saved");
        }
        catch (Exception e){
            PresentationCtrl.getInstance().showExceptionAlert(e.getMessage());
        }
    }

    public void initExpr(){ //inicialitza les expressions a la comboBox
        expressions = PresentationCtrl.getInstance().getAllBooleanExpressions();
        getExpressionsNames();
    }

    public void putExpression(){
        String expr = expressions.get(name.getValue());
        if(expr!= null) setExpression(expr);
    }

    public void deleteExpression(){ //elimina la expressio
        if(!PresentationCtrl.getInstance().deleteBooleanExpression(name.getValue())) {
            PresentationCtrl.getInstance().showExceptionAlert(" no existeix");
        }
        else {
            PresentationCtrl.getInstance().setMessage("expressio eliminada");
        }
    }
    public void getExpressionsNames(){
        ArrayList<String> noms = new ArrayList<>();
        for(Map.Entry<String, String> i : expressions.entrySet() ){
            noms.add(i.getKey());
        }
        ObservableList<String> nameList = FXCollections.observableArrayList(noms);
        name.setItems(nameList);
    }
}
