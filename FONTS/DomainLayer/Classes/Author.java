package DomainLayer.Classes;

import java.util.HashSet;
import java.util.Set;

public class Author extends Sentence{
    Set<Integer> documents = new HashSet<Integer>();
    
    public Author(String p) {
        super(p);
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public String getAuthorName() {
        return super.getString();
    }

    public Set<Integer> getAllDocsID() {
        return documents;
    }

    public boolean isAuthorOfDoc(Integer docID) {
        return documents.contains(docID);
    }

    public int getNbDocuments(){
        return documents.size();
    }

    /**
     * Setters
     */
    public boolean addDoc(Integer docID) {
        return documents.add(docID);
    }

    public boolean deleteDocument(Integer docID){
        if(isAuthorOfDoc(docID)){
            documents.remove(docID);
            return true;
        }
        return false;
    }
}