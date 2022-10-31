package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
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

    public boolean existsDocument(Document doc) {
        return documents.containsValue(doc);
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

    public Set<Integer> getAllDocsIDs() {
        return documents.keySet();
    }

    public Set<Pair<String,String>> getAllDocsTitlesAuthors() {
        return documentsID.keySet();
    }

    public Set<Document> getAllDocuments() {
        return new HashSet<Document>(documents.values());
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

}


