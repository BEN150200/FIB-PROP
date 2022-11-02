package preprocessing;

import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     * 
     * @param tokens
     * @return all tokens filtered, may return less tokens that were inputted
     */
    public static Stream<String> filter(Stream<String> tokens) {
        return tokens.map(TokenFilter::filter)
            .filter(Optional::isPresent)
            .map(Optional::get);
    }
}
