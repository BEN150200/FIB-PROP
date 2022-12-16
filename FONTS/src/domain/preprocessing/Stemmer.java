package src.domain.preprocessing;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;

public class Stemmer {
    @SuppressWarnings("deprecation")
    public static String stem(String token) {
        var match = HashMap.of(
            "ing", "",
            "ally", "",
            "ant", ""
        )
        .filterKeys(token::endsWith)
        .getOrNull();

        if(match == null)
            return token;
        else {
            var suffix = match._1;
            var replacement = match._2;
            return token.substring(0, token.length() - suffix.length())
                 + replacement;
        }
    }
}
