package Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Word {

    /**
     * Attributes
     */

    private String word;
    private Map<Document, Integer> docs;
     
    /**
     * Constructor / Destructor
     */

    public Word() {
    }

    public Word(String w) {
        this.word = w;
    }

    /*
     * Getters / Setters
     */

    public String getWord() {
        return word;
    }

    public void setWord() {
        
    }

}
