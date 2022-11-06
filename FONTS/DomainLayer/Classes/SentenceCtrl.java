package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;

public class SentenceCtrl {

    private static SentenceCtrl instance = null;
    private HashMap<String, Sentence> sentences = new HashMap<String, Sentence>();
    private HashMap<String, Integer> sentencesID = new HashMap<String, Integer>();
    
    public static SentenceCtrl getInstance() {
        if (instance == null) {
            instance = new SentenceCtrl();
        }
        return instance;
    }

    /**
     * Public Functions
    **/

    /**
     * Getters
    **/

    public Boolean existsSentence(String sentence){
        return sentences.containsKey(sentence);
    }

    public Sentence getSentence(String sentence){
        return sentences.get(sentence);
    }

    public ArrayList<Sentence> getAllSentences(){
        return new ArrayList<Sentence>(sentences.values());
    }
    
    /**
     * Setters
    **/

    public boolean addSentence(Sentence s) {
        if (!existsSentence(s.toString())){
            sentences.put(s.toString(), s);
            return true;
        }
        return false;
    }

    public Boolean deleteSentence(String sentence){
        if (existsSentence(sentence)){
            sentences.remove(sentence);
            return true;
        }
        return false;
    }
}
