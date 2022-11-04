package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javafx.util.Pair;

public class DocumentCtrl {
    private static DocumentCtrl instance = null;
    private int usedID;
    
    private HashMap<Pair<String,String>, Integer> documentsID = new HashMap<Pair<String,String>, Integer>();
    private HashMap<Integer, Document> documents = new HashMap<Integer, Document>();

    public static DocumentCtrl getInstance() {
        if (instance == null) {
            instance = new DocumentCtrl();
        }
        return instance;
    }
    
    public DocumentCtrl() {
        usedID = 0;
    }

    /**
     * Public Functions
    **/

    /**
     * Getters
    **/
    public boolean existsDocument(Integer docID) {
        return documents.containsKey(docID);
    }

    public boolean existsDocument(String titleName, String authorName) {
        return documentsID.containsKey(new Pair<String,String>(titleName, authorName));
    }
    
    public Integer getDocumentID(String titleName, String authorName) {
        return documentsID.get(new Pair<String,String>(titleName, authorName));
    }

    public Document getDocument(String titleName, String authorName) {
        return documents.get(documentsID.get(new Pair<String,String>(titleName, authorName)));
    }

    public Document getDocument(int docID) {
        Document doc = documents.get(docID);
        return doc;
    }

    public ArrayList<Integer> getAllDocsIDs() {
        return new ArrayList<Integer>(documents.keySet());
    }

    public ArrayList<Pair<String,String>> getAllDocsTitlesAuthors() {
        return new ArrayList<Pair<String, String>>(documentsID.keySet());
    }

    public ArrayList<Document> getAllDocuments() {
        return new ArrayList<Document>(documents.values());
    }

    public ArrayList<Document> getDocuments(Set<Integer> docsID) {
        ArrayList<Document> docs = new ArrayList<Document>();
        for (Integer id : docsID) {
            docs.add(documents.get(id));
        }
        return docs;
    }

    /**
     * Setters
    **/

    public boolean addDocument(Document doc) {
        Title t = doc.getTitle();
        Author a = doc.getAuthor();
        ++usedID;
        doc.setDocumentID(usedID);
        documentsID.put(new Pair<String,String>(t.getTitleName(), a.getAuthorName()), usedID);
        documents.put(usedID, doc);
        return true;
    }

    public boolean deleteDocument(String titleName, String authorName){
        if(existsDocument(titleName, authorName)){
            Pair<String, String> p = new Pair<String, String>(titleName, authorName);
            documents.remove(getDocumentID(titleName, authorName));
            documentsID.remove(p);
            return true;
        }
        return false;
    }

}


