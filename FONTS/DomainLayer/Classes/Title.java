package DomainLayer.Classes;

import java.util.HashSet;
import java.util.Set;

public class Title extends Sentence {
    Set<Integer> documents = new HashSet<Integer>();
    
    public Title(String t) {
        super(t);
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public String getTitleName() {
        return super.getString();
    }

    public Set<Integer> getAllDocsID() {
        return documents;
    }

    public boolean isTitleOfDoc(Integer docID) {
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
        if(isTitleOfDoc(docID)){
            documents.remove(docID);
            return true;
        }
        return false;
    }
}
