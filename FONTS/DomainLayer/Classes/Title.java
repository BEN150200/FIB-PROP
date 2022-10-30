package DomainLayer.Classes;

import java.util.HashSet;
import java.util.Set;

public class Title extends Phrase {
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

    /**
     * Setters
     */
    public boolean addDoc(Integer docID) {
        return documents.add(docID);
    }
}
