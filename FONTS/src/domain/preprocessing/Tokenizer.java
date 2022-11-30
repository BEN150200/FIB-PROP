package src.domain.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    /**
     * @param sentence a sentence (doesn't contain '.', '...', etc.)
     * @return an array of tokens of the text
     */
    public static String[] tokenize(String sentence) {
        return sentence.split(" ");

    }
    

    /**
     * @param text
     * @return an array of sentences (in String form) of the text
     */
    public static ArrayList<String> splitSentences(String text) {
        String[] enterSplit = text.split("((?=\\n))"); //split the text by enter but keeping the enter after the string

        ArrayList<String> result = new ArrayList<>();
        for (String s: enterSplit) {
            result.addAll(Arrays.asList(s.split("((?<=\\.))")));
        }
        return result;
    }
}
