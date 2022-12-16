package src.domain.indexing.core;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import io.vavr.control.Try;
import src.domain.preprocessing.Tokenizer;
import src.helpers.Strings;

public class Parsing {

    /**
     * 
     * @param idContents
     * @return
     */
    public static HashMap<String, Iterable<String>> makeCollection(String... idContents) {
        return Stream.of(idContents).grouped(2).foldLeft(
            HashMap.<String, String>empty(),
            (map, idContent) -> map.put(idContent.get(0), idContent.get(1))
        )
        .mapValues(Tokenizer::tokenize)
        .mapValues(Arrays::asList);
    }


    static public Tuple2<String, ? extends Iterable<String>> parseDocument(Path filepath) {
        return Try.of(() -> Files.readString(filepath, Charset.forName("ISO-8859-1")))
            .map(Tokenizer::tokenize)
            .map(Stream::of)
            .map(s -> Tuple.of(filepath.toString(), s))
            .get();
    }

    static public List<Tuple2<String, ? extends Iterable<String>>> parseFolder(String folderPath) {
        return Try.of(() ->
            Files.walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .map(Parsing::parseDocument)
        )
        .get().collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    private static Either<String, Tuple2<String, Double>> parseWeightedTerm(String term) {
        var splited = term.split("\\^");
        if(splited.length > 0 && splited[0].isEmpty() || splited.length < 2 && term.contains("^"))
            return Either.left("Unexpected incomplete '^' found in " + term);
        else if(!Strings.isAlphaNumeric(splited[0])) {
            return Either.left("Unexpected non-alphanumeric token '" + splited[0] + "'");
        }
        else if(splited.length > 2)
            return Either.left("Only one '^' expected in " + term);
        else if(splited.length == 1)
            return Either.right(Tuple.of(splited[0], 1.0));
        else
            return Try.of(() -> Double.parseDouble(splited[1]))
                .map(weight -> Tuple.of(splited[0], weight))
                .toEither("Invalid weight '" + splited[1] + "'");
    }

    public static Either<String, HashMap<String, Double>> weightedQuery(String query) {
        if(query.isEmpty())
            return Either.left("Empty query");
        
        var terms = Stream.of(
            query.strip().split(" ")
        );

        return
            Either.sequence(
                terms.map(Parsing::parseWeightedTerm)
            )
            .map(HashMap::ofEntries)
            .mapLeft(
                errors -> String.join("\n", errors)
            );
    }
}
