package src.domain.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

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
    public static List<String> splitSentences(String text) {
        return Arrays.asList(
            text.split("\n")
        );
    }
}
