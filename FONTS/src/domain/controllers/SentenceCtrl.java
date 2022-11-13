package src.domain.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import src.domain.core.Sentence;

public class SentenceCtrl {

    private static SentenceCtrl instance = null;
    private HashMap<String, Sentence> sentencesByKey = new HashMap<String, Sentence>();
    private HashMap<Integer, Sentence> sentencesById = new HashMap<Integer, Sentence>();
    private int usedID;

    public static SentenceCtrl getInstance() {
        if (instance == null) {
            instance = new SentenceCtrl();
        }
        return instance;
    }

    public SentenceCtrl() {
        usedID = 0;
    }

    /**
     * Public Functions
    **/

    /**
     * Getters
    **/

    public Boolean existsSentence(String sentence){
        return sentencesByKey.containsKey(sentence);
    }

    public boolean existsSentence(int sentenceId){
        return sentencesById.containsKey(sentenceId);
    }

    public Sentence getSentence(String sentence){
        return sentencesByKey.get(sentence);
    }

    public Sentence sentenceById(int sentenceId) {
        return this.sentencesById.get(sentenceId);
    }

    public ArrayList<Sentence> getAllSentences(){
        return new ArrayList<Sentence>(sentencesByKey.values());
    }
    
    /**
     * Setters
    **/

    public boolean addSentence(Sentence s) {
        if (!existsSentence(s.toString())){
            ++usedID;
            s.setID(usedID);
            sentencesByKey.put(s.toString(), s);
            sentencesById.put(usedID, s);
            return true;
        }
        return false;
    }

    public Boolean deleteSentence(String sentence){
        if (existsSentence(sentence)){
            var id = sentencesByKey.get(sentence).id();
            sentencesByKey.remove(sentence);
            sentencesById.remove(id);
            return true;
        }
        return false;
    }
}