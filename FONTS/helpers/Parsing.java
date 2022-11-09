package helpers;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import domain.preprocessing.Tokenizer;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.control.Try;

public class Parsing {
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
}
