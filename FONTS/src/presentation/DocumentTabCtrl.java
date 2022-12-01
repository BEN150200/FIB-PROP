package src.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.domain.core.DocumentInfo;
import src.domain.preprocessing.Tokenizer;
import src.enums.Format;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DocumentTabCtrl {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField title;
    @FXML
    private TextField author;

    public void initialize () {
        
    }

    /**
     * Setters
     */

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setAuthor(String author) {
        this.author.setText(author);
    }

    public void setContent(String content) {
        this.textArea.setText(content);
    }

    @FXML
    public void save() {

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

        ArrayList<String> content = Tokenizer.splitSentences(textArea.getText());

        DocumentInfo docToBeSaved = new DocumentInfo(null, title.getText(), author.getText(), LocalDateTime.now(), LocalDateTime.now(), content, path, format);

        PresentationCtrl.getInstance().saveAsDocument(docToBeSaved);
    }

    /**
     * Getters
     */

    /**
     *
     * @return
     */

    public String getTitle() {
        return title.getText();
    }

    public String getAuthor() {
        return author.getText();
    }
}
