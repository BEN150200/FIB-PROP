package src.domain.preprocessing;

import java.util.Optional;

public class TokenFilter {

    /**
     * Default filter:
     *  1. lowercase
     *  2. remove stopwords
     *  3. stem
     * @param token
     * @return maybe, the filtered token
     */
    public static Optional<String> filter(String token) {
        return Optional.of(token)
            .map(String::toLowerCase)
            .filter(Stopwords::filter)
            .map(Stemmer::stem);
    }
}
