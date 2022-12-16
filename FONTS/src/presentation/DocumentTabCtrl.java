package src.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class DocumentTabCtrl {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField title;
    @FXML
    private TextField author;

    private boolean modified;

    private boolean newdoc;

    public void initialize () {
        setListeners();
        modified = false;
        newdoc = true;
    }

    private void setListeners() {
        title.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (modified == false && oldValue != newValue) modified = true;
        });

        author.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (modified == false && oldValue != newValue) modified = true;
        });

        textArea.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (modified == false && oldValue != newValue) modified = true;
        });
    }

    public void blockTitleAndAuthor() {
        newdoc = false;
        title.setEditable(false);
        author.setEditable(false);
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
        newdoc = false;
    }

    public void setAuthor(String author) {
        this.author.setText(author);
        newdoc = false;
    }

    public void setContent(ArrayList<String> content) {
        for (String s : content) {
            if (s.lastIndexOf("\n") == -1) textArea.appendText(s+"\n");
            else textArea.appendText(s);
        }
        newdoc = false;
    }

    public void setSaved() {
        modified = false;
        if (newdoc) blockTitleAndAuthor();
    }

    public void setNew() {
        newdoc = true;
    }

    /*
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
     */

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

    public String getContent() {
        return textArea.getText();
    }

    public boolean modified() {
        return modified;
    }

    public boolean isNew() {
        return newdoc;
    }
}
