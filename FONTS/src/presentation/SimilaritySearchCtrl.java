package src.presentation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import src.domain.core.DocumentInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class SimilaritySearchCtrl {

    @FXML
    private ComboBox<String> authorBox;

    @FXML
    private ComboBox<String> titleBox;

    @FXML
    private Spinner<Integer> numSpinner;

    @FXML
    private Button searchButton;

    @FXML
    private VBox vbox;

    private ResultTableCtrl resultTableCtrl;

    private final ArrayList<String> listTitles = new ArrayList<>();

    private final ArrayList<String> listAuthors = new ArrayList<>();

    /**
     * stores if the list of the titles in the titleBox is the modified one
     */
    private boolean titleMod;
    /**
     * stores if the list of the authors in the authorBox is the modified one
     */
    private boolean authorMod;

    /**
     * Creator
     */
    public SimilaritySearchCtrl() {
    }

    /**
     * initialize all the parameters and elements and update the content
     */
    public void initialize() {
        //load Result Table
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/presentation/fxml/resultTable.fxml"));
        VBox table;
        try {
            table = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.getChildren().add(3,table);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        resultTableCtrl = loader.getController();
        resultTableCtrl.setForSimilarity();

        //let spinner min and max number
        int currentNum = 5;
        SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinnerFactory.setValue(currentNum);
        numSpinner.setValueFactory(spinnerFactory);

        //set the text editor of the combobox non editable
        Font f = new Font(14);
        titleBox.getEditor().setFont(f);
        titleBox.getEditor().setEditable(false);
        authorBox.getEditor().setFont(f);
        authorBox.getEditor().setEditable(false);

        setListeners(); //set listeners
        update(); //Update the title and author lists
    }

    /**
     * Update the list of the titles and authors
     */
    public void update() {
        listTitles.clear();
        listTitles.add("");
        listTitles.addAll(PresentationCtrl.getInstance().getAllTitles());

        listAuthors.clear();
        listAuthors.add("");
        listAuthors.addAll(PresentationCtrl.getInstance().getAllAuthors());

        titleBox.setItems(FXCollections.observableArrayList(listTitles));
        titleBox.getSelectionModel().select("");

        authorBox.setItems(FXCollections.observableArrayList(listAuthors));
        authorBox.getSelectionModel().select("");

        titleMod = false;
        authorMod = false;
    }

    /**
     * set the listeners for the two combobox
     */
    private void setListeners() {

        titleBox.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBox.show();
        });

        authorBox.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBox.show();
        });

        titleBox.setOnAction(actionEvent -> {
            if (Objects.equals(titleBox.getSelectionModel().getSelectedItem(), "") && authorMod) {
                authorBox.setItems(FXCollections.observableArrayList(listAuthors));
                authorMod = false;
            }
            else if (Objects.equals(authorBox.getSelectionModel().getSelectedItem(), "")) {
                ArrayList<String> tempAuthors = new ArrayList<>();
                tempAuthors.add("");
                tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBox.getEditor().getText()));

                authorBox.setItems(FXCollections.observableArrayList(tempAuthors));
                authorMod = true;

            }
        });

        authorBox.setOnAction(actionEvent -> {
            if (Objects.equals(authorBox.getSelectionModel().getSelectedItem(), "") && titleMod) {
                titleBox.setItems(FXCollections.observableArrayList(listTitles));
                titleMod = false;
            }
            else if (Objects.equals(titleBox.getSelectionModel().getSelectedItem(), "")) {
                ArrayList<String> tempTitles = new ArrayList<>();
                tempTitles.add("");
                tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBox.getEditor().getText()));

                titleBox.setItems(FXCollections.observableArrayList(tempTitles));
                titleMod = true;
            }
        });
    }

    /**
     * search the documents similar at the one with the title and author specified in the combobox
     * and show only the number of documents in the spinner that are more similars
     */
    @FXML
    private void search() {
        if (!Objects.equals(titleBox.getEditor().getText(), "")
                && !Objects.equals(authorBox.getEditor().getText(), "")) {

            ArrayList<DocumentInfo> resultDocs = PresentationCtrl.getInstance()
                    .similaritySearch(
                            titleBox.getEditor().getText(),
                            authorBox.getEditor().getText(),
                            numSpinner.getValue()
                    );

            if (resultDocs == null) PresentationCtrl.getInstance().setMessage("the document not exists or there are no similar ones");
            else resultTableCtrl.updateTable(resultDocs);
        }
    }

    /**
     * do the search by similarity of the document represented by the parameters
     * @param title title of the document of the search
     * @param author author of the document of the search
     */
    public void search(String title, String author) {
        titleBox.getSelectionModel().select(title);
        authorBox.getSelectionModel().select(author);
        search();
    }

}
