package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;

/* 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.catalog.CatalogFeatures.Feature;
*/

import java.util.List;
import java.util.Set;

import java.util.HashSet;


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

    public ArrayList<String> getAllTitles() {
        return TitleCtrl.getInstance().getAllTitlesNames();
    }

    public ArrayList<String> getAllAuthors() {
        return AuthorCtrl.getInstance().getAllAuthorsNames();
    }

    public HashMap<String,String> getAllBooleanExpresions() {
        return null;
    }

    public ArrayList<String> getAllAuthorTitles(String authorName) {
        Author author = AuthorCtrl.getInstance().getAuthor(authorName);
        if (author == null) System.out.println("The author is not in the System");
        else {
            Set<Integer> docsID = author.getAllDocsID();
            ArrayList<Document> docs = DocumentCtrl.getInstance().getDocuments(docsID);
            ArrayList<String> docsTitlesNames = new ArrayList<String>();
            for (Document doc : docs) {
                docsTitlesNames.add(doc.getTitle().getTitleName());
            }
            return docsTitlesNames;
        }
        return null;
    }

    public ArrayList<String> getAllTitleAuthors(String titleName) {
        Title title = TitleCtrl.getInstance().getTitle(titleName);
        if (title == null) System.out.println("The title is not in the System");
        else {
            Set<Integer> docsID = title.getAllDocsID();
            ArrayList<Document> docs = DocumentCtrl.getInstance().getDocuments(docsID);
            ArrayList<String> docsAuthorsNames = new ArrayList<String>();
            for (Document doc : docs) {
                docsAuthorsNames.add(doc.getAuthor().getAuthorName());
            }
            return docsAuthorsNames;
        }
        return null;
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

    
    
}
