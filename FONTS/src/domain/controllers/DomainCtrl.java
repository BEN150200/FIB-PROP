package src.domain.controllers;

import src.domain.core.Author;
import src.domain.core.Document;
import src.domain.core.DocumentInfo;
import src.domain.core.Sentence;
import src.domain.core.Title;
import src.enums.Format;
import src.persistance.controllers.PersistanceCtrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import io.vavr.control.Either;

public class DomainCtrl {

    /**
     * Attributes
     **/
    private static DomainCtrl instance = null;
    private static final PersistanceCtrl persistanceCtrl = PersistanceCtrl.getInstance();

    /**
     * Constructor
     **/
    private DomainCtrl() {}

    /**
     * Function to get the instance of the class
     * @return the instance of the class
     */
    public static DomainCtrl getInstance() {
        if (instance == null) {
            instance = new DomainCtrl();
        }
        return instance;
    }

    /**
     * Public Functions
     */

    /**
     * Add Elements
     */

    /**
     * Funciont to add a Boolean Expression into the System
     * @param boolExpName The name of the Boolean expresion that will be saved
     * @param boolExp The boolean expresion in a String
     * @return Return True if the boolean expresin is added, if return False the boolean expresion is incorrect or there is another in the System with this name
     */
    public void addBooleanExpression(String boolExpName, String boolExp) throws Exception{
        BooleanExpressionCtrl.getInstance().saveExpression(boolExp, boolExpName);
    }

    /**
     * Function to know if a boolean expresion is in the System
     * @param boolExpName Name of the boolean expresion stored in the System
     * @return Returns true if the boolean expresion identified by boolExpName exists in the System, else return false.
     */
    public boolean existsBooleanExpression(String boolExpName){
        return BooleanExpressionCtrl.getInstance().existsBooleanExpression(boolExpName);
    }

    /**
     * Update Elements
     **/


    private void updateDocumentContent(Document doc, List<String> content) {

        ArrayList<Sentence> newContent = new ArrayList<>();
        for (String sentenceDoc : content) {
            Sentence sentence;
            if(!SentenceCtrl.getInstance().existsSentence(sentenceDoc)) {
                sentence = new Sentence(sentenceDoc);
            }
            else {
                sentence = SentenceCtrl.getInstance().getSentence(sentenceDoc);
            }
            newContent.add(sentence);
        }
        doc.updateDocumentContent(newContent);

    }

    /**
     * Delete Elements
     **/

    public boolean deleteDocument(String titleName, String authorName){
        if (DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {
            Document d = DocumentCtrl.getInstance().getDocument(titleName, authorName);

            d.delete();

            return true;
        }
        return false;
    }


    public boolean deleteBooleanExpression(String boolExpName) {
        return BooleanExpressionCtrl.getInstance().deleteExpression(boolExpName);
    }

    /**
     * Search Elements
     **/

    public ArrayList<String> getAllTitles(String titleName) {
        if (titleName.isEmpty()) return TitleCtrl.getInstance().getAllTitlesNames();
        return TitleCtrl.getInstance().getTitlesNamesPrefix(titleName);
    }

    public ArrayList<String> getAllAuthors(String authorName) {
        if (authorName.isEmpty()) return AuthorCtrl.getInstance().getAllAuthorsNames();
        return AuthorCtrl.getInstance().getAuthorsNamesPrefix(authorName);
    }

    public HashMap<String,String> getAllBooleanExpresions() {
        return BooleanExpressionCtrl.getInstance().getSavedExpressionsNames();
    }

    public ArrayList<String> getAllAuthorTitles(String authorName) {
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<Author> authors = AuthorCtrl.getInstance().getAuthorsPrefix(authorName);
        Set<Integer> docsID = new HashSet<Integer>();
        for (Author author : authors) {
            docsID.addAll(author.getAllDocsID());
        }
        for (Integer integer : docsID) {
            titles.add(DocumentCtrl.getInstance().getDocument(integer).getTitle().toString());
        }
        return titles;
    }

    public ArrayList<String> getAllTitleAuthors(String titleName) {
        ArrayList<String> authors = new ArrayList<String>();
        ArrayList<Title> titles = TitleCtrl.getInstance().getTitlesPrefix(titleName);
        Set<Integer> docsID = new HashSet<Integer>();
        for (Title title : titles) {
            docsID.addAll(title.getAllDocsID());
        }
        for (Integer integer : docsID) {
            authors.add(DocumentCtrl.getInstance().getDocument(integer).getAuthor().toString());
        }
        return authors;
    }



    public ArrayList<DocumentInfo> getDocsByTitleAndAuthor(String titleName, String authorName) {
        ArrayList<Title> titles = new ArrayList<>();
        if (titleName != null) titles = TitleCtrl.getInstance().getTitlesPrefix(titleName);
        else titles = TitleCtrl.getInstance().getAllTitles();

        ArrayList<Author> authors = new ArrayList<>();
        if (authorName != null) authors = AuthorCtrl.getInstance().getAuthorsPrefix(authorName);
        else authors = AuthorCtrl.getInstance().getAllAuthors();

        Set<Integer> titlesDocsIDs = new HashSet<Integer>();
        Set<Integer> authorsDocsIDs = new HashSet<Integer>();
        Set<Integer> docsIDs = new HashSet<Integer>();
        for (Title title : titles) {
            titlesDocsIDs.addAll(title.getAllDocsID());
        }
        for (Author author : authors) {
            authorsDocsIDs.addAll(author.getAllDocsID());
        }
        for (Integer integer : authorsDocsIDs) {
            if (titlesDocsIDs.contains(integer)) {
                docsIDs.add(integer);
            }
        }

        ArrayList<Document> docs = DocumentCtrl.getInstance().getDocuments(docsIDs);
        ArrayList<DocumentInfo> docsInfo = new ArrayList<DocumentInfo> ();

        for (Document doc : docs) {
            docsInfo.add(doc.getInfo());
        }

        return docsInfo;
    }

    public DocumentInfo getOneDocument(String titleName, String authorName) {
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        if (doc != null) return doc.getInfo();
        return null;
    }



    /**
     * Complex Search
     */

    /**
     *
     * @param titleName title that identifies the document
     * @param authorName author that identifies the document
     * @return Return the similar to the document identified by titleName and authorName (order is not guaranteed)
     */
    public CompletableFuture<Either<String, List<DocumentInfo>>> similarDocumentsSearch(String titleName, String authorName) {
        return SearchCtrl.getInstance().similarDocumentsSearch(titleName, authorName);
    }

    public ArrayList<DocumentInfo> savedBooleanExpressionSearch(String name){
        return SearchCtrl.getInstance().savedBooleanExpressionSearch(name);
    }

    public ArrayList<DocumentInfo> tempBooleanExpressionSearch (String boolExp) throws Exception {
        return SearchCtrl.getInstance().tempBooleanExpressionSearch(boolExp);
    }

    public ArrayList<DocumentInfo> getAllDocumentsInfo() {
        ArrayList<Document> docs = DocumentCtrl.getInstance().getAllDocuments();
        ArrayList<DocumentInfo> info = new ArrayList<DocumentInfo>();
        for (Document d : docs) {
            info.add(d.getInfo());
        }
        return info;
    }

    public CompletableFuture<Either<String, List<DocumentInfo>>> documentsByQuery(String query){
        return SearchCtrl.getInstance().documentsByQuery(query);

    }
    public String getExpression(String name){
        return BooleanExpressionCtrl.getInstance().getExpression(name);
    }

    private void addDocument(DocumentInfo docToBeSaved) throws Exception{
        if (DocumentCtrl.getInstance().existsDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor())) throw new Exception("A document with this title and author already exists.");
        Title title = TitleCtrl.getInstance().getTitle(docToBeSaved.getTitle());
        Author author = AuthorCtrl.getInstance().getAuthor(docToBeSaved.getAuthor());
        if (title == null) {
            title = new Title(docToBeSaved.getTitle());
        }
        if (author == null) {
            author = new Author(docToBeSaved.getAuthor());
        }

        Document doc = new Document(title, author, docToBeSaved);
        updateDocumentContent(doc, docToBeSaved.getContent());
    }


    public void saveDocument(String titleName, String authorName, List<String> content) throws Exception{
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        if (doc == null) throw new Exception("A document with this title and author does not exist.");
        updateDocumentContent(doc, content);
        exportDocument(titleName, authorName, doc.getFormat(), doc.getPath());
    }

    public void saveAsDocument(DocumentInfo docToBeSaved) throws Exception{
        Document doc = DocumentCtrl.getInstance().getDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor());
        if(doc != null) {
            doc.setPath(docToBeSaved.getPath());
            doc.setFormat(docToBeSaved.getFormat());
            saveDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor(), docToBeSaved.getContent());
        }
        else {
            addDocument(docToBeSaved);
            saveDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor(), docToBeSaved.getContent());
        }
    }

    public void exportDocument(DocumentInfo docToBeSaved) throws Exception{
        Document doc = DocumentCtrl.getInstance().getDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor());
        if(doc == null) {
            saveAsDocument(docToBeSaved);
        }
        else {
            saveDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor(), docToBeSaved.getContent());
            exportDocument(docToBeSaved.getTitle(), docToBeSaved.getAuthor(), docToBeSaved.getFormat(), docToBeSaved.getPath());
        }
    }


    public void loadData() throws Exception{
        // Load documents, titles and authors
        ArrayList<DocumentInfo> docsData = persistanceCtrl.loadDocumentsData();
        for(DocumentInfo docInfo: docsData) addDocument(docInfo);
        // Load saved boolean expressions
        ArrayList<String> expressionsNames = persistanceCtrl.loadExpressionsNames();
        ArrayList<String> expressions = persistanceCtrl.loadExpressions();
        for(int i = 0; i < expressionsNames.size(); i++){
            addBooleanExpression(expressionsNames.get(i), expressions.get(i));
        }
    }

    public void saveData() throws Exception{
        // Save documents data
        ArrayList<DocumentInfo> docsData = new ArrayList<DocumentInfo>();
        ArrayList<Document> documents = DocumentCtrl.getInstance().getAllDocuments();
        for(Document doc: documents){
            docsData.add(doc.getInfo());
        }
        persistanceCtrl.saveDocumentsData(docsData);

        // Save boolean expressions data
        ArrayList<String> expressionsNames = new ArrayList<String>();
        ArrayList<String> expressions = new ArrayList<String>();
        HashMap<String, String> expressionsMap = BooleanExpressionCtrl.getInstance().getSavedExpressionsNames();
        for(String expressionName: expressionsMap.keySet()){
            expressionsNames.add(expressionName);
            expressions.add(expressionsMap.get(expressionName));
        }
        persistanceCtrl.saveBooleanexpressionsData(expressionsNames, expressions);
    }


    // Exports data to file in given path, if the file does not exist, creates it.
    public void exportDocument(String titleName, String authorName, Format fileFormat, String path) throws Exception{
        if(!DocumentCtrl.getInstance().existsDocument(titleName, authorName)) throw new Exception("A document with this title and author does not exist.");
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        ArrayList<Sentence> sentences = doc.getSentences();
        ArrayList<String> content = new ArrayList<String>();
        for(Sentence sentence: sentences){
            content.add(sentence.toString());
        }
        persistanceCtrl.exportToFile(titleName, authorName, content, fileFormat, path);
    }

    public DocumentInfo importDocumentFromFile(String path, Format fileFormat) throws Exception{
        DocumentInfo docInfo = persistanceCtrl.importFromFile(path, fileFormat);
        addDocument(docInfo);
        return docInfo;
    }

    // Clears all data in the domain and persistence
    public void clearAllData() throws Exception{
        // Clear domain data
        AuthorCtrl.getInstance().clear();
        TitleCtrl.getInstance().clear();
        DocumentCtrl.getInstance().clear();
        SentenceCtrl.getInstance().clear();
        BooleanExpressionCtrl.getInstance().clear();
        SearchCtrl.getInstance().clear();

        // Clear persistence data
        persistanceCtrl.clearData();
    }
}
