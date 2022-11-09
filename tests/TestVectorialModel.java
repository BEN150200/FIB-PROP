package tests;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import org.junit.Test;

import domain.indexing.core.Index;
import domain.indexing.vectorial.VectorialModel;
import helpers.Parsing;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Try;

public class TestVectorialModel {

    
    @Test
    public void testNews() {
        var folderPath = "..\\pracs-caim\\s1\\data\\raw\\20_newsgroups";
        
        var files = Parsing.parseFolder(folderPath);

        Map<String, Iterable<String>> corpus = HashMap.ofEntries(files);

        var start = Instant.now();
        var model = VectorialModel.of(corpus.toJavaMap());
        var end = Instant.now();

        System.out.println("Created in " + Duration.between(start, end).toMillis() + "ms");

        var query = java.util.Map.of(
            "god", 1.0d
        );

        var results = Stream.ofAll(model.querySimilars(query).entrySet())
            .sortBy(e -> e.getValue())
            .reverse()
            .take(5);

        results.forEach(entry ->
            System.out.println(
                "Doc " + entry.getKey() + " (score=" + entry.getValue() + ")\n" +
                "-----------------------------------\n" +
                Try.of(() -> Files.readString(Paths.get(entry.getKey()), Charset.forName("ISO-8859-1"))).get() +
                "\n-----------------------------------\n"
            )
        );
    }

    static final Map<String, Iterable<String>> corpus = HashMap.of(
            "d1", "0 0 1 0 1 0",
            "d2", "0 0 0 0 1 2",
            "d3", "3 1 1 0 1 0",
            "d4", "0 0 1 2 1 4",
            "d5", "0 3 0 1 1 0",
            "d6", "0 0 0 2 3 0",
            "d7", "1 1 0 0 0 0"
        )
        .mapValues(s -> s.split(" "))
        .mapValues(List::of)
        .mapValues(l -> l.map(Integer::parseInt))
        .mapValues(l -> l.zip(
            List.of("five four one six three two".split(" "))
        ))
        .mapValues(l -> l.flatMap(
            freqTerm -> List.fill(freqTerm._1, freqTerm::_2)
        ));

    @Test
    public void testConstructionMethods() {
        var index = Index.of(corpus);
        var expected = VectorialModel.of(index);
        var actual = VectorialModel.of(corpus.toJavaMap());

        assertEquals(expected, actual);
    }

    @Test
    public void testSimilarity() {
        var index = Index.of(corpus);
        System.out.println(Index.print(index));
        var model = VectorialModel.of(index);

        for(var docId : corpus.keySet()) {
            System.out.println(docId + " = " + model.tfidfVector(docId));
        }

        var similars = model.querySimilars("d3").get();

        assertEquals(
            0.035,
            similars.get("d4"),
            0.001 // up to 3 decimal places
        );

        for(var entry : similars.entrySet()) {
            var docId = entry.getKey();
            var similarity = entry.getValue();
            System.out.println(docId + ": " + similarity);
        }
    }
}
