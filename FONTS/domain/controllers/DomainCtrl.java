package domain.controllers;

import domain.core.Author;
import domain.core.Document;
import domain.core.ExpressionTreeNode;
import domain.core.Title;
import domain.indexing.core.IndexingController;
import domain.core.Sentence;

import domain.DocumentInfo;

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
    **/

    /**
     * Add Elements
    **/
    public boolean addDocument(String titleName, String authorName, List<String> content) {
        if (!DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {
            Title title = TitleCtrl.getInstance().getTitle(titleName);
            Author author = AuthorCtrl.getInstance().getAuthor(authorName);
            if (title == null) {
                title = new Title(titleName);
                TitleCtrl.getInstance().addTitle(title);
            }
            if (author == null) {
                author = new Author(authorName);
                AuthorCtrl.getInstance().addAuthor(author);
            }
            Document doc = new Document(title, author);
            DocumentCtrl.getInstance().addDocument(doc);
            Integer ID = doc.getID();
            title.addDoc(ID);
            author.addDoc(ID);

            updateDocumentContent(doc, content);

            return true;
        }
        return false;
    }

    /*
    public boolean addTitle(String titleName) {
        Title t = new Title(titleName);
        return TitleCtrl.getInstance().addTitle(t);
    }

    public boolean addAuthor(String authorName) {
        Author a = new Author(authorName);
        return AuthorCtrl.getInstance().addAuthor(a);
    }
    */

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
                SentenceCtrl.getInstance().addSentence(sentence);
                sentence.compute();
            }
            else {
                sentence = SentenceCtrl.getInstance().getSentence(sentenceDoc);
            }
            doc.addSentence(sentence);
            sentence.addDoc(doc.getID());
        }

        doc.updateModificationDate();
        doc.compute();
    }

    /**
     * Delete Elements
    **/
    
    public boolean deleteDocument(String titleName, String authorName){
        if (!DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {
            Document d = DocumentCtrl.getInstance().getDocument(titleName, authorName);
            DocumentCtrl.getInstance().deleteDocument(titleName, authorName);
            
            Author a = d.getAuthor();
            a.deleteDocument(d.getID());
            if(a.getNbDocuments() == 0){
                AuthorCtrl.getInstance().deleteAuthor(a.toString());
            }

            Title t = d.getTitle();
            t.deleteDocument(d.getID());
            if(t.getNbDocuments() == 0){
                TitleCtrl.getInstance().deleteTitle(t.toString());
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
                    SentenceCtrl.getInstance().deleteSentence(sentence.toString());
                    SearchCtrl.getInstance().removeSentence(sentence.id());
                }
            }
    }

    public boolean deleteBooleanExpression(String boolExpName) {
        return BooleanExpressionCtrl.getInstance().deleteExpression(boolExpName);
    }

    /**
     * Search Elements
    **/
    public ArrayList<DocumentInfo> getAllDocumentsInfo() {
        ArrayList<Document> docs = DocumentCtrl.getInstance().getAllDocuments();
        ArrayList<DocumentInfo> info = new ArrayList<DocumentInfo>();
        for (Document d : docs) {
            info.add(d.getInfo());
        }
        return info;
    }

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
    
    // Copiar a partir d'aqui 
    public ExpressionTreeNode getSavedExpressionTree(String boolExpName){
        return BooleanExpressionCtrl.getInstance().getSavedExpressionTree(boolExpName);
    }

    public ExpressionTreeNode createExpressionTree(String boolExp){
        return BooleanExpressionCtrl.getInstance().createExpressionTree(boolExp);
    }

    /*public ArrayList<Integer> booleanQueryDocs(ExpressionTreeNode root){
        return IndexingController.getInstance().booleanQueryDocs(root);
    }*/
}
