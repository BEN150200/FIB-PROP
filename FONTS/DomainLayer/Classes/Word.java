package DomainLayer.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Word {

    /**
     * Attributes
     */

    private String word;
    private HashMap<Integer, Integer> ocurrences = new HashMap<Integer, Integer>();
     
    /**
     * Constructor / Destructor
    **/

    public Word() {
    }

    public Word(String w) {
        word = w;
    }

    /*
     * Getters / Setters
    **/

    public String getWord() {
        return word;
    }

    public Integer getOcurrencesDoc(Integer docID) {
        return ocurrences.get(docID);
    }

}
