package helpers;

import io.vavr.collection.Stream;

public class Strings {
    public static boolean isLowerCase(char c) {
        return (c >= 'a' && c <= 'z');
    }
    
    public static boolean isUpperCase(char c) {
        return (c >= 'A' && c <= 'Z');
        
    }

    public static boolean isAlpha(char c) {
        return isLowerCase(c) || isUpperCase(c);
    }
    
    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    public static boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    @SuppressWarnings("deprecation")
    public static boolean isAlphaNumeric(String string) {
        return Stream.ofAll(string.toCharArray())
            .forAll(Strings::isAlphaNumeric);
    }

    public static String joinEndLine(Iterable<String> strings) {
        return String.join("\n", strings);
    }
}
