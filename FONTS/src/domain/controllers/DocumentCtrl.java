package src.domain.controllers;

import src.domain.core.Author;
import src.domain.core.Document;
import src.domain.core.Title;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import io.vavr.Tuple2;

public class DocumentCtrl {
    private static DocumentCtrl instance = null;
    private int usedID;
    
    private HashMap<Tuple2<String,String>, Integer> documentsID = new HashMap<Tuple2<String,String>, Integer>();
    private HashMap<Integer, Document> documents = new HashMap<Integer, Document>();

    /**
     * Constructor
     */
    private DocumentCtrl() {
        usedID = 0;
    }

    /**
     * Function to get the instance of the class
     * @return the instance of the class
     */
    public static DocumentCtrl getInstance() {
        if (instance == null) {
            instance = new DocumentCtrl();
        }
        return instance;
    }
    

    /**
     * Public Functions
     */

    /**
     * Getters
     */

    /**
     * 
     * @param docID ID of the document that 
     * @return
     */
    public boolean existsDocument(Integer docID) {
        return documents.containsKey(docID);
    }

    public boolean existsDocument(String titleName, String authorName) {
        return documentsID.containsKey(new Tuple2<String,String>(titleName, authorName));
    }
    
    public Integer getDocumentID(String titleName, String authorName) {
        return documentsID.get(new Tuple2<String,String>(titleName, authorName));
    }

    public Document getDocument(String titleName, String authorName) {
        return documents.get(documentsID.get(new Tuple2<String,String>(titleName, authorName)));
    }

    public Document getDocument(int docID) {
        Document doc = documents.get(docID);
        return doc;
    }

    public ArrayList<Integer> getAllDocsIDs() {
        return new ArrayList<Integer>(documents.keySet());
    }

    /*
    public ArrayList<Tuple2<String,String>> getAllDocsTitlesAuthors() {
        return new ArrayList<Tuple2<String,String>>(documentsID.keySet());
    }
    */

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
    public Integer addDocument(Document doc) {
        Title t = doc.getTitle();
        Author a = doc.getAuthor();
        ++usedID;
        documentsID.put(new Tuple2<String,String>(t.toString(), a.toString()), usedID);
        documents.put(usedID, doc);
        return usedID;
    }

    public boolean deleteDocument(String titleName, String authorName){
        if(existsDocument(titleName, authorName)){
            Tuple2<String,String> p = new Tuple2<String,String>(titleName, authorName);
            documents.remove(getDocumentID(titleName, authorName));
            documentsID.remove(p);
            return true;
        }
        return false;
    }

}


