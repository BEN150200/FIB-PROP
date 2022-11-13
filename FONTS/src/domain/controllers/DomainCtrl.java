package src.domain.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.domain.DocumentInfo;
import src.domain.core.Author;
import src.domain.core.Document;
import src.domain.core.ExpressionTreeNode;
import src.domain.core.Sentence;
import src.domain.core.Title;
import src.domain.indexing.core.IndexingController;

public class DomainCtrl {

    /**
     * Attributes
    **/
    private static DomainCtrl instance = null;
    
    /**
     * Constructor
    **/
    public DomainCtrl() {
        iniCtrlDomain();
    }

    private void iniCtrlDomain() {
        
    }

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
     * @param titleName The String of the Name of the Title that identifies the new Document
     * @param authorName The String of the Name of the Author that identifies the new Document
     * @param content List of the strings of the Document in order of appearance.
     * @return True if the document is created sucsesfuly, else False.
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


    public boolean addBooleanExpresion(String boolExpName, String boolExp) {
        return BooleanExpressionCtrl.getInstance().saveExpression(boolExp, boolExpName);
    }


    public boolean existsBooleanExpression(String boolExpName){
        return BooleanExpressionCtrl.getInstance().existsBooleanExpression(boolExpName);
    }

    /**
     * Update Elements
    **/

    public boolean updateDocument(String titleName, String authorName, List<String> content) {
        Document doc = DocumentCtrl.getInstance().getDocument(titleName, authorName);
        if (doc == null) return false;

        updateDocumentContent(doc, content);
        
        return true;
    }

    private void updateDocumentContent(Document doc, List<String> content) {
        deleteDocumentContent(doc);

        for (String sentenceDoc : content) {
            Sentence sentence;
            if(!SentenceCtrl.getInstance().existsSentence(sentenceDoc)) {
                sentence = new Sentence(sentenceDoc);
            }
            else {
                sentence = SentenceCtrl.getInstance().getSentence(sentenceDoc);
            }
            
            doc.addSentence(sentence);
        }

        doc.updateModificationDate();
        doc.compute();
    }

    /**
     * Delete Elements
    **/
    
    public boolean deleteDocument(String titleName, String authorName){
        if (DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {
            Document d = DocumentCtrl.getInstance().getDocument(titleName, authorName);
            DocumentCtrl.getInstance().deleteDocument(titleName, authorName);
            
            Author a = d.getAuthor();
            a.deleteDocument(d.getID());
            if(a.getNbDocuments() == 0){
                a.delete();
            }

            Title t = d.getTitle();
            t.deleteDocument(d.getID());
            if(t.getNbDocuments() == 0){
                t.delete();
            }
            
            deleteDocumentContent(d);
            
            
            SearchCtrl.getInstance().removeDocument(d.getID());

            return true;
        }
        return false;
    }

    private void deleteDocumentContent(Document doc) {
        ArrayList<Sentence> content = doc.getSentences();
            for (Sentence sentence : content) {
                sentence.deleteDocument(doc.getID());
                if (sentence.getNbDocuments() == 0) {
                    sentence.delete();
                }
            }
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
        ArrayList<Title> titles = TitleCtrl.getInstance().getTitlesPrefix(titleName);
        ArrayList<Author> authors = AuthorCtrl.getInstance().getAuthorsPrefix(authorName);
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

    public ArrayList<DocumentInfo> tempBooleanExpressionSearch (String boolExp) {
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
}