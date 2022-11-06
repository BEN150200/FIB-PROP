package domain.controllers;

import domain.core.Word;

import java.util.ArrayList;
import java.util.HashMap;

public class WordCtrl {
    //public static String invalidID = "";
    private static WordCtrl instance = null;

    private HashMap<String, Word> words = new HashMap<String, Word>();

    public static WordCtrl getInstance() {
        if (instance == null) {
            instance = new WordCtrl();
        }

        return instance;
    }

    /**
     * Public Functions
    **/

    /**
     * Getters
    **/

    public Boolean existsWord(String word){
        return words.containsKey(word);
    }

    public Word getWord(String word){
        return words.get(word);
    }

    public ArrayList<Word> getAllWords(){
        return new ArrayList<Word>(words.values());
    }
    
    /**
     * Setters
    **/

    public boolean addWord(Word w) {
        if (!existsWord(w.getWord())){
            words.put(w.getWord(), w);
            return true;
        }
        return false;
    }

    public Boolean deleteSentence(String word){
        if (existsWord(word)){
            words.remove(word);
            return true;
        }
        return false;
    }

}
