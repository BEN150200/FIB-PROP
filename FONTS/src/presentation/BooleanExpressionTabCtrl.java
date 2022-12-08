package src.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.domain.expressions.BooleanExpression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooleanExpressionTabCtrl {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField expression;

    @FXML
    private ComboBox<String> name;

    private ResultTable resultTableCtrl;

    @FXML
    private VBox resultPane;

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

        //resultPane.getChildren().add(table);
        resultTableCtrl = loader.getController();

        //setListeners();

        name.getEditor().setOnMouseClicked(mouseEvent -> {
            name.show();
        });

        initExpr();

        //update();


    }

    /**
     * Setters
     */

    /**
     *
     * @param //title
     */
    public void setExpression(String expression) {
        this.expression.setText(expression);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setContent(String content) {
        this.textArea.setText(content);
    }


    public void SearchBooleanExpression(){
        try {
            resultTableCtrl.updateTable( PresentationCtrl.getInstance().tempBooleanExpressionSearch(expression.getText()));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void SaveExpression(){

        if(name.getValue().length()==0) {
            System.out.println("falta un nom");
            return;
        }
        //falta mirar si ja existeix o no
        try{
            PresentationCtrl.getInstance().addBooleanExpression (name.getValue(),  expression.getText());
        }
        catch (Exception e){
            System.out.println(e);
        }

        initExpr();
        System.out.println(expressions.size());
    }

    public void initExpr(){
        expressions = PresentationCtrl.getInstance().getAllBooleanExpresions();
    }

    public void putExpression(){
        String expr = expressions.get(name.getValue());

        if(expr!= null) setExpression(expr);
    }

    public void deleteExpression(){
        if(!PresentationCtrl.getInstance().deleteBooleanExpression(name.getValue()))  System.out.println("no existeixx");
        initExpr();
    }
    public void getExpressionsNames(){
        //System.out.println("agafant expressionsss: " + expr.size());
        ArrayList<String> noms = new ArrayList<>();
        for(Map.Entry<String, String> i : expressions.entrySet() ){
            //if(i.getKey().compareTo(name.getValue()) >= 0 )noms.add(i.getKey());
            //System.out.println("valor= " + name.getValue());
            noms.add(i.getKey());
        }
        ObservableList<String> nameList = FXCollections.observableArrayList(noms);
        name.setItems(nameList);
    }

    /**
     * Getters
     */

    /**
     *
     * @return
     */

    public String getExpression() {
        return expression.getText();
    }

}
