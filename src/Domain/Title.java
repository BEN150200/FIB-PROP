package Domain;

import java.util.HashSet;
import java.util.Set;

public class Title extends Phrase {
    //Set<Document> documents = new HashSet<Document>();
    
    public Title(String t) {
        super(t);
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public String getTitle() {
        return super.getString();
    }

    /**
     * Setters
     */
}
