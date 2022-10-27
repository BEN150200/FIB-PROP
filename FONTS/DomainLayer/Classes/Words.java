package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordCtrl {
    public static String invalidID = "";
    private static WordCtrl instance = null;

    private HashMap<String, Word> words;

    private WordCtrl() {
        words = new HashMap<String, Word>(0);
    }

    public static WordCtrl getInstance() {
        if (instance == null) {
            instance = new WordCtrl();
        }

        return instance;
    }

    public boolean addWord(String w) {
        Word selWord = words.get(w);
        if (selWord == null) {
            words.put(w, new Word(w));
            return true;
        }
        return false;
    }

}
