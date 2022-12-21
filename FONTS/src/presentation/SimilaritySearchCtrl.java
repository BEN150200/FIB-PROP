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
    private ComboBox<String> authorBoxNoEdit;

    @FXML
    private ComboBox<String> titleBoxNoEdit;

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
        titleBoxNoEdit.getEditor().setFont(f);
        titleBoxNoEdit.getEditor().setEditable(false);
        authorBoxNoEdit.getEditor().setFont(f);
        authorBoxNoEdit.getEditor().setEditable(false);

        resultTableCtrl = loader.getController();
        resultTableCtrl.setForSimilarity();
        resultTableCtrl.setK(currentNum);
        setListeners();
        update();
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

        titleBoxNoEdit.setItems(FXCollections.observableArrayList(listTitles));
        titleBoxNoEdit.getSelectionModel().select("");

        authorBoxNoEdit.setItems(FXCollections.observableArrayList(listAuthors));
        authorBoxNoEdit.getSelectionModel().select("");

        titleMod = false;
        authorMod = false;
    }

    /**
     * set the listeners for the two combobox
     */
    private void setListeners() {

        numSpinner.valueProperty().addListener((_1, _2, k) -> resultTableCtrl.setK(k));

        titleBoxNoEdit.getEditor().setOnMouseClicked(mouseEvent -> {
            titleBoxNoEdit.show();
        });

        authorBoxNoEdit.getEditor().setOnMouseClicked(mouseEvent -> {
            authorBoxNoEdit.show();
        });

        titleBoxNoEdit.setOnAction(actionEvent -> {
            try {
                if (Objects.equals(titleBoxNoEdit.getEditor().getText(), "") && authorMod) {
                    authorBoxNoEdit.setItems(FXCollections.observableArrayList(listAuthors));
                    authorBoxNoEdit.setVisibleRowCount(Math.min(listAuthors.size(), 10));
                    authorMod = false;
                }

                else if (authorMod && !titleMod) {
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBoxNoEdit.getEditor().getText()));
                    authorBoxNoEdit.setItems(FXCollections.observableArrayList(tempAuthors));
                    authorBoxNoEdit.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                }
                else if (Objects.equals(authorBoxNoEdit.getEditor().getText(), "") && !titleMod) {
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBoxNoEdit.getEditor().getText()));
                    authorBoxNoEdit.setItems(FXCollections.observableArrayList(tempAuthors));
                    authorBoxNoEdit.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                    authorMod = true;

                }
                else if (Objects.equals(authorBoxNoEdit.getEditor().getText(), "") && titleMod) {
                    titleBoxNoEdit.setItems(FXCollections.observableArrayList(listTitles));
                    titleBoxNoEdit.setVisibleRowCount(Math.min(listTitles.size(), 10));
                    ArrayList<String> tempAuthors = new ArrayList<>();
                    tempAuthors.add("");
                    tempAuthors.addAll(PresentationCtrl.getInstance().getAuthorsByTitle(titleBoxNoEdit.getEditor().getText()));
                    authorBoxNoEdit.setItems(FXCollections.observableArrayList(tempAuthors));
                    authorBoxNoEdit.setVisibleRowCount(Math.min(tempAuthors.size(), 10));
                    authorMod = true;
                    titleMod = false;
                }
                else if (Objects.equals(authorBoxNoEdit.getEditor().getText(), "") &&
                        Objects.equals(titleBoxNoEdit.getEditor().getText(), "")) {
                    titleMod = false;
                    authorMod = false;
                }
            }
            catch (StackOverflowError e) {

            }
        });

        authorBoxNoEdit.setOnAction(actionEvent -> {
            try {
                //if the author is not selected but the title modified, restore the title
                if (Objects.equals(authorBoxNoEdit.getEditor().getText(), "") && titleMod) {
                    titleBoxNoEdit.setItems(FXCollections.observableArrayList(listTitles));
                    titleBoxNoEdit.setVisibleRowCount(Math.min(listTitles.size(), 10));
                    titleMod = false;
                }
                //if title is modified and author not change the modified title list by the new one
                else if (titleMod && !authorMod) {
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBoxNoEdit.getEditor().getText()));
                    titleBoxNoEdit.setItems(FXCollections.observableArrayList(tempTitles));
                    titleBoxNoEdit.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                }
                //else if it is not selected, modifie it
                else if (Objects.equals(titleBoxNoEdit.getEditor().getText(), "") && !authorMod) {
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBoxNoEdit.getEditor().getText()));
                    titleBoxNoEdit.setItems(FXCollections.observableArrayList(tempTitles));
                    titleBoxNoEdit.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                    titleMod = true;
                } else if (Objects.equals(titleBoxNoEdit.getEditor().getText(), "") && authorMod) {
                    authorBoxNoEdit.setItems(FXCollections.observableArrayList(listAuthors));
                    authorBoxNoEdit.setVisibleRowCount(Math.min(listAuthors.size(), 10));
                    ArrayList<String> tempTitles = new ArrayList<>();
                    tempTitles.add("");
                    tempTitles.addAll(PresentationCtrl.getInstance().getTitlesByAuthor(authorBoxNoEdit.getEditor().getText()));
                    titleBoxNoEdit.setItems(FXCollections.observableArrayList(tempTitles));
                    titleBoxNoEdit.setVisibleRowCount(Math.min(tempTitles.size(), 10));
                    titleMod = true;
                    authorMod = false;
                }
                //if the two ar not selected, neither is modified
                else if (Objects.equals(authorBoxNoEdit.getEditor().getText(), "") &&
                        Objects.equals(titleBoxNoEdit.getEditor().getText(), "")) {
                    titleMod = false;
                    authorMod = false;
                }
            }
            catch (StackOverflowError e) {

            }
        });
    }

    /**
     * search the documents similar at the one with the title and author specified in the combobox
     * and show only the number of documents in the spinner that are more similars
     */
    @FXML
    private void search() {
        if (!Objects.equals(titleBoxNoEdit.getEditor().getText(), "")
        && !Objects.equals(authorBoxNoEdit.getEditor().getText(), "")) {
            PresentationCtrl.getInstance().similaritySearch(titleBoxNoEdit.getEditor().getText(), authorBoxNoEdit.getEditor().getText())
                .peek(resultTableCtrl::updateTable)
                .peekLeft(PresentationCtrl.getInstance()::setMessage);
        }
    }

    /**
     * do the search by similarity of the document represented by the parameters
     * @param title title of the document of the search
     * @param author author of the document of the search
     */
    public void search(String title, String author) {
        titleBoxNoEdit.getSelectionModel().select(title);
        authorBoxNoEdit.getSelectionModel().select(author);
        search();
    }


}
