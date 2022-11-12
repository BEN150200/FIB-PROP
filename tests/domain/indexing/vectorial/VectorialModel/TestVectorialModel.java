package tests.domain.indexing.vectorial.VectorialModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

import domain.indexing.core.Index;
import domain.indexing.vectorial.VectorialModel;
import helpers.Parsing;
import helpers.Strings;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;

public class TestVectorialModel {

    /*private HashMap<String, Iterable<String>> corpus = Parsing.makeCollection(
        "d1", "and the earth was without form and void",
        "d2", "and darkness was upon the face of the deep",
        "d3", "and the spirit of god moved upon the face of the waters"
    );*/

    HashMap<String, HashMap<String, HashSet<Integer>>> invertedIndex = HashMap.of(
        "and", HashMap.of("d1", HashSet.of(0, 6), "d2", HashSet.of(0), "d3", HashSet.of(0)),
        "the", HashMap.of("d1", HashSet.of(1), "d2", HashSet.of(4, 7), "d3", HashSet.of(1, 7, 10)),
        "earth", HashMap.of("d1", HashSet.of(2)),
        "was", HashMap.of("d1", HashSet.of(3), "d2", HashSet.of(2)),
        "without", HashMap.of("d1", HashSet.of(4)),
        "form", HashMap.of("d1", HashSet.of(5)),
        "void", HashMap.of("d1", HashSet.of(7)),
        "darkness", HashMap.of("d2", HashSet.of(1)),
        "upon", HashMap.of("d2", HashSet.of(3), "d3", HashSet.of(6)),
        "face", HashMap.of("d2", HashSet.of(5), "d3", HashSet.of(8))
    ).merge(HashMap.of(
        "of", HashMap.of("d2", HashSet.of(6), "d3", HashSet.of(3, 9)),
        "deep", HashMap.of("d2", HashSet.of(8)),
        "spirit", HashMap.of("d3", HashSet.of(2)),
        "god", HashMap.of("d3", HashSet.of(4)),
        "moved", HashMap.of("d3", HashSet.of(5)),
        "waters", HashMap.of("d3", HashSet.of(11))
    ));

    HashMap<String, HashMap<String, HashSet<Integer>>> directIndex = HashMap.of(
        "d1", HashMap.of("form", HashSet.of(5), "without", HashSet.of(4), "was", HashSet.of(3), "earth", HashSet.of(2), "the", HashSet.of(1), "void", HashSet.of(7), "and", HashSet.of(0, 6)),
        "d2", HashMap.of("was", HashSet.of(2), "deep", HashSet.of(8), "darkness", HashSet.of(1), "the", HashSet.of(4, 7), "of", HashSet.of(6), "and", HashSet.of(0), "upon", HashSet.of(3), "face", HashSet.of(5)),
        "d3", HashMap.of("and", HashSet.of(0), "the", HashSet.of(1, 7, 10), "spirit", HashSet.of(2), "of", HashSet.of(3, 9), "god", HashSet.of(4), "moved", HashSet.of(5), "upon", HashSet.of(6), "face", HashSet.of(8), "waters", HashSet.of(11))
    );

    // should be equivalent to Index.of(corpus), but it's a stub!
    private VectorialModel<String> model = VectorialModel.of(
        new Index<String>(
            invertedIndex,
            directIndex
        )
    );

    // VectorialModel::insert
    @Test
    @SuppressWarnings("deprecation")
    public void insertEmpty() {
        var inserted = model.insert("d4", HashSet.empty());

        assertEquals(
            HashMap.empty(),
            inserted.tfidfVector("d4").getOrNull()
        );
    }

    @Test
    public void insertUnrelated() {
        var inserted = model.insert("d4", Arrays.asList("fish birds creeping beasts".split(" ")));

        assertEquals(
            model.querySimilars("d1"),
            inserted.querySimilars("d1")
        );

        assertEquals(
            HashMap.of("d4", 1.0d),
            inserted.querySimilars("d4").get()
        );
    }

    @Test
    public void insertExistingTerms() {
        var inserted = model.insert("d4", Arrays.asList("then god said let there be light".split(" ")));

        assertNotEquals(
            model.tfidfVector("d3"),
            inserted.tfidfVector("d3")
        );

        assertEquals(
            model.tfidfVector("d1"),
            inserted.tfidfVector("d1")
        );
    }

    @Test
    public void insertAlreadyExisting() {
        assertEquals(
            model,
            model.insert("d3", Arrays.asList("and the spirit of god moved upon the face of the waters".split(" ")))
        );
    }

    // VectorialModel::remove
    @Test
    public void removeNonExisting() {
        assertEquals(
            model,
            model.remove("d4")
        );
    }

    @Test
    public void removeExising() {
        var removed = model.remove("d3");

        assertNotEquals(
            model.querySimilars("d1").get().remove("d3"),
            removed.querySimilars("d1").get()
        );
    }

    // VectorialModel::querySimilars
    @Test
    public void totallyDifferent() {
        assertEquals(
            HashMap.empty(),
            model.querySimilars(HashMap.of(
                "nothing", 1.0d,
                "to", 1.0d,
                "see", 1.0d,
                "here", 1.0d
            ))
        );
    }

    @Test
    public void roughSimilarity() {
        var result = model.querySimilars(HashMap.of(
            "face", 1.0d,
            "of", 1.0d,
            "the", 1.0d,
            "deep", 1.0d
        ));

        var sim1 = result.getOrElse("d1", 0.0d);
        var sim2 = result.getOrElse("d2", 0.0d);
        var sim3 = result.getOrElse("d3", 0.0d);

        assertTrue(sim2 > sim3 && sim3 > sim1);
    }

    @Test
    public void queryEqual() {
        assertEquals(
            1.0d,
            model.querySimilars("d3").get().get("d3").get().doubleValue(),
            0.0d
        );
    }

    @Test
    public void unequalPrortions() {
        var model = VectorialModel.of(Index.of(
            Parsing.makeCollection(
                "d1", "hello there",
                "d2", "hello there there",
                "d3", "other words so df is not equal to the collection size"
            )
        ));

        var sim = model.querySimilars("d1").get().get("d2").get();
        assertTrue(sim < 1.0d);
    }

    @Test
    public void equalProportions() {
        var model = VectorialModel.of(Index.of(
            Parsing.makeCollection(
                "d1", "hello there",
                "d2", "hello there there hello",
                "d3", "other words so df is not equal to the collection size"
            )
        ));

        var sim = model.querySimilars("d1").get().get("d2").get();

        assertEquals(1.0d, sim, 0.00000001d);
    }

    
    @Test
    @SuppressWarnings("deprecation")
    public void testNews() {
        var folderPath = "..\\pracs-caim\\s1\\data\\raw\\20_newsgroups";
        
        var files = Parsing.parseFolder(folderPath);

        HashMap<String, Iterable<String>> corpus = HashMap.ofEntries(files);


        var start = Instant.now();
        var index = Index.of(corpus);
        var end = Instant.now();


        System.out.println("Constructed index in " + Duration.between(start, end).toMillis() + "ms");

        start = Instant.now();
        var model = VectorialModel.of(index);
        end = Instant.now();

        System.out.println("Computed vectorial model in " + Duration.between(start, end).toMillis() + "ms");

        var query = HashMap.of(
            "god", 1.0d
        );

        var results = model.querySimilars(query).toStream()
        .sortBy(Tuple2::_2)
        .reverse()
        .take(5);

        results.forEach(entry ->
            System.out.println(
                "Doc " + entry._1 + " (score=" + entry._2 + ")\n" +
                "-----------------------------------\n" +
                Try.of(() -> Files.readString(Paths.get(entry._1), Charset.forName("ISO-8859-1"))).get() +
                "\n-----------------------------------\n"
            )
        );
    }
    
    //     5 4 1 6 3 2
    // d1: 0 0 1 0 1 0
    // d2: 0 0 0 0 1 2
    // d3: 3 1 1 0 1 0
    // d5: 0 3 0 1 1 0
    // d6: 0 0 0 2 3 0
    // d7: 1 1 0 0 0 0
    // see https://www.cs.upc.edu/~caim/slides/2ir_models.pdf
    @Test
    public void testSimilarity() {
        var index = new Index<String>(
            HashMap.of(
                "five", HashMap.of("d3", HashSet.of(0, 1, 2), "d7", HashSet.of(0)),
                "four", HashMap.of("d3", HashSet.of(3), "d5", HashSet.of(0, 1, 2), "d7", HashSet.of(1)),
                "one", HashMap.of("d1", HashSet.of(0), "d3", HashSet.of(4), "d4", HashSet.of(0)),
                "six", HashMap.of("d4", HashSet.of(1, 2), "d5", HashSet.of(3), "d6", HashSet.of(0, 1)),
                "three", HashMap.of("d1", HashSet.of(1), "d2", HashSet.of(0), "d3", HashSet.of(5), "d4", HashSet.of(3), "d5", HashSet.of(4), "d6", HashSet.of(2, 3, 4)),
                "two", HashMap.of("d2", HashSet.of(1, 2), "d4", HashSet.of(4, 5, 6, 7))
            ),
            HashMap.of(
                "d1", HashMap.of("one", HashSet.of(0), "three", HashSet.of(1)),
                "d2", HashMap.of("two", HashSet.of(1, 2), "three", HashSet.of(0)),
                "d3", HashMap.of("four", HashSet.of(3), "one", HashSet.of(4), "five", HashSet.of(0, 1, 2), "three", HashSet.of(5)),
                "d4", HashMap.of("six", HashSet.of(1, 2), "one", HashSet.of(0), "two", HashSet.of(4, 5, 6, 7), "three", HashSet.of(3)),
                "d5", HashMap.of("six", HashSet.of(3), "four", HashSet.of(0, 1, 2), "three", HashSet.of(4)),
                "d6", HashMap.of("six", HashSet.of(0, 1), "three", HashSet.of(2, 3, 4)),
                "d7", HashMap.of("four", HashSet.of(1), "five", HashSet.of(0))
            )
        );
        
        var model = VectorialModel.of(index);
        var similars = model.querySimilars("d3").get();

        assertEquals(
            0.035,
            similars.get("d4").get(),
            0.001d
        );
    }
}
