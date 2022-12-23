package src.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        textArea.setWrapText(true);
        modified = false;
        newdoc = true;
    }

    private void setListeners() {
        title.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!modified && !Objects.equals(oldValue, newValue)) modified = true;
        });

        author.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!modified && !Objects.equals(oldValue, newValue)) modified = true;
        });

        textArea.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!modified && !Objects.equals(oldValue, newValue)) modified = true;
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

    public void setContent(List<String> content) {
        var text = content.stream()
            .map(s -> s.replace('\n', ' ').stripTrailing())
            .collect(Collectors.joining("\n"));

        textArea.setText(text);
        newdoc = false;
    }

    public void setSaved() {
        modified = false;
        if (newdoc) blockTitleAndAuthor();
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

    public String getContent() {
        return textArea.getText();
    }

    public boolean modified() {
        return modified;
    }

    public boolean isNew() {
        return newdoc;
    }

    public boolean isBlanck() {
        return title.getText().isBlank() || author.getText().isBlank();
    }
}
