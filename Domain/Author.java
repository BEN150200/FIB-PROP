package Domain;

import java.util.HashSet;
import java.util.Set;

public class Author extends Phrase{
    Set<Document> documents = new HashSet<Document>();
    
    public Author(String p) {
        super(p);
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public String getAuthor() {
        return super.getString();
    }

    
    
    /**
     * Setters
     */
}
