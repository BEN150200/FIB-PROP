package DomainLayer.Classes;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

public class Sentence {
    private String sentence;
    //private ArrayList<String> words = new ArrayList<String>();
    private Set<Integer> documents = new HashSet<Integer>();

    
    
    public Sentence(String p) {
        sentence = p;
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    @Override
    public String toString() {
        return sentence;
    }

    public Set<Integer> getAllDocsID() {
        return documents;
    }

    public boolean belongsToDoc(Integer docID) {
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
        if(belongsToDoc(docID)){
            documents.remove(docID);
            return true;
        }
        return false;
    }

    // cridar tokenize


}
