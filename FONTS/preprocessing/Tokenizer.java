package preprocessing;

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
     * @return an array of phrases (in String form) of the text
     */
    public static String[] splitPhrases(String text) {
        return text.split(".");
    }
}
