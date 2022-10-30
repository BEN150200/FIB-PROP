package DomainLayer.Classes;

import java.util.HashSet;
import java.util.Set;

public class Author extends Phrase{
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

    public Set<Integer> getDocuments() {
        return documents;
    }

    public boolean isAuthorOfDoc(Integer docID) {
        return documents.contains(docID);
    }

    
    
    /**
     * Setters
     */
}
