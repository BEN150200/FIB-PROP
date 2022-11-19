package src.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TabCtrl {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField title;
    @FXML
    private TextField author;

    public void initialize () {
        textArea = new TextArea();
        title = new TextField();
        author = new TextField();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setAuthor(String author) {
        this.author.setText(author);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getAuthor() {
        return author.getText();
    }
}
