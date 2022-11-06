package domain.controllers;

import domain.core.Author;
import domain.core.Document;
import domain.core.Title;

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
            Title t = TitleCtrl.getInstance().getTitle(titleName);
            Author a = AuthorCtrl.getInstance().getAuthor(authorName);
            if (t == null) {
                t = new Title(titleName);
                TitleCtrl.getInstance().addTitle(t);
            }
            if (a == null) {
                a = new Author(authorName);
                AuthorCtrl.getInstance().addAuthor(a);
            }
            Document d = new Document(t, a);
            DocumentCtrl.getInstance().addDocument(d);
            Integer ID = d.getID();
            t.addDoc(ID);
            a.addDoc(ID);
            d.updateContent(content);
            return true;
        }
        return false;
    }

    public boolean addTitle(String titleName) {
        Title t = new Title(titleName);
        return TitleCtrl.getInstance().addTitle(t);
    }

    public boolean addAuthor(String authorName) {
        Author a = new Author(authorName);
        return AuthorCtrl.getInstance().addAuthor(a);
    }

    public boolean addBooleanExpresion(String boolExpName, String boolExp) {
        return false;
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
            return true;
        }
        return false;
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
        return null;
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

    

    
    
}
