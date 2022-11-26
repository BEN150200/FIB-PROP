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

public class DomainCtrl {

    /**
     * Attributes
    **/
    private static DomainCtrl instance = null;
    private static PersistanceCtrl persistanceCtrl = null;
    
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
            persistanceCtrl = new PersistanceCtrl();
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
     * Function to add a Document into the System
     * @param titleName The String of the Name of the Title that identifies the new Document
     * @param authorName The String of the Name of the Author that identifies the new Document
     * @param content List of the strings of the Document in order of appearance.
     * @return True if the document is created sucsesfuly, else if return false, there is a Docuement in the System with this identifier.
     */
    public boolean addDocument(String titleName, String authorName, List<String> content) {
        if (!DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {

            Title title = TitleCtrl.getInstance().getTitle(titleName);
            Author author = AuthorCtrl.getInstance().getAuthor(authorName);
            if (title == null) {
                title = new Title(titleName);
            }
            if (author == null) {
                author = new Author(authorName);
            }

            Document doc = new Document(title, author);
            updateDocumentContent(doc, content);

            return true;
        }
        return false;
    }

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

    /**
     * 
     * @param titleName Title Name that identifies the Document to be modified
     * @param authorName Author Name that identifies the Document to be modified
     * @param content ArrayList of the Strings that will be the new content of the Document
     * @return Return false if there is no Document in the System identified by this title and author
     */
    public boolean updateDocument(String titleName, String authorName, List<String> content) {
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        if (doc == null) return false;

        updateDocumentContent(doc, content);
        
        return true;
    }

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

    public ArrayList<DocumentInfo> getAllAuthorDocuments(String authorName) {
        ArrayList<DocumentInfo> docsInfo = new ArrayList<DocumentInfo>();
        ArrayList<Author> authors = AuthorCtrl.getInstance().getAuthorsPrefix(authorName);
        HashSet<Integer> docsID = new HashSet<Integer>();
        for (Author author : authors) {
            docsID.addAll(author.getAllDocsID());
        }
        for (Integer docID : docsID) {
            Document doc = DocumentCtrl.getInstance().getDocument(docID);
            docsInfo.add(doc.getInfo());
        }
        return docsInfo;
    }

    public ArrayList<DocumentInfo> getAllTitleDocuments(String titleName) {
        ArrayList<DocumentInfo> docsInfo = new ArrayList<DocumentInfo>();
        ArrayList<Title> titles = TitleCtrl.getInstance().getTitlesPrefix(titleName);
        HashSet<Integer> docsID = new HashSet<Integer>();
        for (Title title : titles) {
            docsID.addAll(title.getAllDocsID());
        }
        for (Integer docID : docsID) {
            Document doc = DocumentCtrl.getInstance().getDocument(docID);
            docsInfo.add(doc.getInfo());
        }
        return docsInfo;
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

    public ArrayList<String> getDocumentContent(Integer docId) {
        Document doc = DocumentCtrl.getInstance().getDocument(docId);
        if (doc == null) return null;
        else {
            ArrayList<String> sentencesStrings = new ArrayList<String>();
            ArrayList<Sentence> sentences = doc.getSentences();
            sentencesStrings.add(doc.getTitle().toString());
            sentencesStrings.add(doc.getAuthor().toString());
            for (Sentence sentence : sentences) {
                sentencesStrings.add(sentence.toString());
            }
            return sentencesStrings;
        }
    }

    public ArrayList<String> getDocumentContent(String titleName, String authorName){
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        if(doc != null){
            ArrayList<Sentence> sentences = doc.getSentences();
            ArrayList<String> content = new ArrayList<>();
            for(Sentence s: sentences){
                content.add(s.toString());
            }
            return content;
        } else {
            return null;
        }
    }
    
    /**
     * Complex Search
     */

    /**
     * 
     * @param titleName title that identifies the document
     * @param authorName author that identifies the document
     * @param k number of document to be showed
     * @return Return the k documents more similars to the document identified by titleName and authorName
     */
    public ArrayList<DocumentInfo> similarDocumentsSearch(String titleName, String authorName, int k) {
        return SearchCtrl.getInstance().similarDocumentsSearch(titleName, authorName, k);
    }

    public ArrayList<DocumentInfo> storedBooleanExpressionSearch (String boolExpName) {
        return SearchCtrl.getInstance().storedBooleanExpressionSearch(boolExpName);
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

    public ArrayList<DocumentInfo> documentsByQuery(String query){
        return SearchCtrl.getInstance().documentsByQuery(query);
    
    }


    /**
     * presentation functions
     */

    /**
     * chech if document exists, if not, adds it in the system and creates the file.
     */
    public void saveDocument(String title, String author, List<String> content) {
        Document doc = DocumentCtrl.getInstance().getDocument(title, author);

        if (doc != null) {
            updateDocumentContent(doc, content);
            //TODO: call update the file
        }
        else {
            addDocument(title, author, content);
            //TODO: create file in the sistem
        }
    }

    public DocumentInfo openFile(String path) {
        //cria al controlador de persistencia per obtenir contingut del document
        DocumentInfo docInfo; // docInfo = persistencia.openFile(path)
        System.out.println(path);
        addDocument(null,null,null);
        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------------
    //  Persistance related methods
    //-------------------------------------------------------------------------------------------------------------------------

    public void loadData() throws  Exception{
        // Load documents, titles and authors
        ArrayList<DocumentInfo> docsData = persistanceCtrl.loadDocumentsData();
        DocumentCtrl documentCtrl = DocumentCtrl.getInstance();
        for(DocumentInfo docInfo: docsData){
            addDocument(docInfo.getTitle(), docInfo.getAuthor(), docInfo.getContent());
            Document doc = documentCtrl.getDocument(docInfo.getTitle(), docInfo.getAuthor());
            doc.setCreationDate(docInfo.getCreationDate());
            doc.setModificationDate(docInfo.getModificationDate());
            doc.setPath(docInfo.getPath());
            doc.setFormat(docInfo.getFormat());
        }
        // Load saved boolean expressions
        ArrayList<String> expressionsNames = persistanceCtrl.loadExpressionsNames();
        ArrayList<String> expressions = persistanceCtrl.loadExpressions();
        for(int i = 0; i < expressionsNames.size(); i++){
                addBooleanExpression(expressionsNames.get(i), expressions.get(i));
        }
    }

    public void saveData(){
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


    // Exports data to file in given path, if the file does not exists, creates it.
    public boolean exportDocument(String titleName, String authorName, Format fileFormat, String path){
        if(DocumentCtrl.getInstance().existsDocument(titleName, authorName)){
            Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
            ArrayList<Sentence> sentences = doc.getSentences();
            ArrayList<String> content = new ArrayList<String>();
            for(Sentence sentence: sentences){
                content.add(sentence.toString());
            }
            return persistanceCtrl.exportToFile(titleName, authorName, content, fileFormat, path);
        } 
        return false;
    }

    public boolean importDocumentFromFile(String path, Format fileFormat){
        DocumentInfo docInfo = persistanceCtrl.importFromFile(path, fileFormat);
        if(addDocument(docInfo.getTitle(), docInfo.getAuthor(), docInfo.getContent())){
            Document doc = DocumentCtrl.getInstance().getDocument(docInfo.getTitle(), docInfo.getAuthor());
            doc.setPath(path);
            doc.setFormat(fileFormat);
            return true;
        }
        return false;
    }
}
