package src.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BooleanExpressionTabCtrl {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField expression;

    @FXML
    private TextField name;

    private ResultTable resultTableCtrl;
    @FXML
    private TextField author;

    @FXML
    private VBox resultPane;

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
        this.name.setText(name);
    }

    public void setAuthor(String author) {
        this.author.setText(author);
    }

    public void setContent(String content) {
        this.textArea.setText(content);
    }

    @FXML
    /*public void save() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.xml", "*.prop");
        fileChooser.getExtensionFilters().add(extFilter);
        //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);
        String path = file.getPath();
        String name = file.getName();
        Format format = null;
        switch (path.substring(path.lastIndexOf(".") + 1)) {
            case "txt" : {
                format = Format.TXT;
                break;
            }
            case "xml" :  {
                format = Format.XML;
                break;
            }
            default :{
                format = Format.TXT;
            }
        }

        System.out.println(path);
        System.out.println(name);
        ArrayList<String> content = new ArrayList(Arrays.asList(Tokenizer.splitSentences(textArea.getText())));
        //content = Arrays.asList(Tokenizer.splitSentences(textArea.getText()));

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().saveDocument(docToBeSaved);
    }*/

    public void SearchBooleanExpression(){
        try {
            resultTableCtrl.updateTable( PresentationCtrl.getInstance().tempBooleanExpressionSearch(expression.getText()));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void SaveExpression(){

        if(name.getText().length()==0) System.out.println("falta un nom");
        try{
            PresentationCtrl.getInstance().addBooleanExpression (name.getText(),  expression.getText());
        }
        catch (Exception e){
            System.out.println(e);
        }
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

    public String getAuthor() {
        return author.getText();
    }
}
