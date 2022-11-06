package domain.preprocessing;

import java.nio.file.Files;
import java.nio.file.Paths;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.control.Try;

@SuppressWarnings("deprecation")
public class Stopwords {

    private static Try<HashSet<String>> readFile(String filepath) {
        return  Try.of(() -> Files.readAllLines(Paths.get(filepath)))
            .map(HashSet::ofAll);
    }

    private static List<HashSet<String>> stopwords = List.of(
        "stopwords-ca.txt",
        "stopwords-es.txt",
        "stopwords-en.txt"
    )
    .map(Stopwords::readFile)
    .map(maybeSet -> maybeSet.getOrElse(HashSet::empty));

    /**
     * 
     * @param token
     * @return true iff the token isn't a stopword
     */
    public static boolean filter(String token) {
        return Stopwords.stopwords.forAll(stop -> !stop.contains(token));
    }
}
