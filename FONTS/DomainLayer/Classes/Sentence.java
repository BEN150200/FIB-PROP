package DomainLayer.Classes;

import java.util.ArrayList;

public class Sentence {
    private String sentence;
    private ArrayList<Word> words = new ArrayList<Word>();

    
    
    public Sentence(String p) {
        sentence = p;
    }

    public String getString() {
        return sentence;
    }

}
