package domain.core;

import java.util.HashMap;

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
     * Public Functions
    **/

    /*
     * Getters
    **/
    public String getWord() {
        return word;
    }

    public Integer getOcurrencesDoc(Integer docID) {
        return ocurrences.get(docID);
    }

    /*
     * Setters
    **/
    public Integer addOcurrencesDoc(Integer docID, Integer num) {
        if (ocurrences.containsKey(docID)) {
            num += ocurrences.get(docID);
            ocurrences.put(docID, num);
        }
        else {
            ocurrences.put(docID, num);
        }
        return num;
    }
}
