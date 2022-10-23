package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Words {
    public static String invalidID = "";
    private static Words instance = null;

    private HashMap<String, Word> words;

    private Words() {
        words = new HashMap<String, Word>(0);
    }

    public static Words getInstance() {
        if (instance == null) {
            instance = new Words();
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
