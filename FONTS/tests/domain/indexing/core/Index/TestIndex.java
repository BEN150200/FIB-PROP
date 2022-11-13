package tests.domain.indexing.core.Index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import helpers.Parsing;
import helpers.Strings;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import src.domain.indexing.core.Index;

public class TestIndex {
    @Test
    public void empty() {
        var actual = Index.<String>of(HashMap.empty());
        var expected = new Index<String>(
            HashMap.empty(),
            HashMap.empty()
        );

        assertEquals(expected, actual);
    }

    @Test
    public void repeatedTerms() {
       var collection = Parsing.makeCollection(
            "roses", "rose rose rose rose rose rose",
            "ares", "are are are",
            "reds", "red red red red"
        );
        

        var actual = Index.of(collection);
        var expected = new Index<String>(
            HashMap.of(
                "rose", HashMap.of("roses", HashSet.of(0, 1, 2, 3, 4, 5)),
                "are", HashMap.of("ares", HashSet.of(0, 1, 2)),
                "red", HashMap.of("reds", HashSet.of(0, 1, 2, 3))
            ),
            HashMap.of(
                "roses", HashMap.of("rose", HashSet.of(0, 1, 2, 3, 4, 5)),
                "ares", HashMap.of("are", HashSet.of(0, 1, 2)),
                "reds", HashMap.of("red", HashSet.of(0, 1, 2, 3))
            )
        );

        assertEquals(expected, actual);
    }

    @Test
    public void singleTerm() {
        var collection = Parsing.makeCollection(
            "the worst design pattern", "singleton",
            "oh, not more!", "singleton singleton",
            "damnnnn", "singleton ".repeat(10)
        );

        var actual = Index.of(collection);
        var expected = new Index<String>(
            HashMap.of(
                "singleton", HashMap.of(
                    "the worst design pattern", HashSet.of(0),
                    "oh, not more!", HashSet.of(0, 1),
                    "damnnnn", HashSet.ofAll(IntStream.range(0, 10).toArray())
                )
            ),
            HashMap.of(
                "the worst design pattern", HashMap.of("singleton", HashSet.of(0)),
                "oh, not more!", HashMap.of("singleton", HashSet.of(0, 1)),
                "damnnnn", HashMap.of("singleton", HashSet.ofAll(IntStream.range(0, 10).toArray()))
            )
        );

        System.out.println(Index.print(actual));
        assertEquals(expected, actual);
    }

    @Test
    public void insert() {
        var collection = Parsing.makeCollection(
            "d1", "and the earth was without form and void",
            "d2", "and darkness was upon the face of the deep"
        );

        var index = Index.of(collection)
            .insert("d3", "and the evening and the morning were the first day".split(" "));
            
        var expected = new Index<String>(
            HashMap.of(
                "and", HashMap.of("d1", HashSet.of(0, 6), "d2", HashSet.of(0), "d3", HashSet.of(0, 3)),
                "the", HashMap.of("d1", HashSet.of(1), "d2", HashSet.of(4, 7), "d3", HashSet.of(1, 4, 7)),
                "earth", HashMap.of("d1", HashSet.of(2)),
                "was", HashMap.of("d1", HashSet.of(3), "d2", HashSet.of(2)),
                "without", HashMap.of("d1", HashSet.of(4)),
                "form", HashMap.of("d1", HashSet.of(5)),
                "void", HashMap.of("d1", HashSet.of(7)),
                "darkness", HashMap.of("d2", HashSet.of(1)),
                "upon", HashMap.of("d2", HashSet.of(3)),
                "face", HashMap.of("d2", HashSet.of(5))
            ).merge(HashMap.of(
                "of", HashMap.of("d2", HashSet.of(6)),
                "deep", HashMap.of("d2", HashSet.of(8)),
                "evening", HashMap.of("d3", HashSet.of(2)),
                "morning", HashMap.of("d3", HashSet.of(5)),
                "were", HashMap.of("d3", HashSet.of(6)),
                "first", HashMap.of("d3", HashSet.of(8)),            
                "day", HashMap.of("d3", HashSet.of(9))
            )),
            HashMap.of(
                "d1", HashMap.of("form", HashSet.of(5), "without", HashSet.of(4), "was", HashSet.of(3), "earth", HashSet.of(2), "the", HashSet.of(1), "void", HashSet.of(7), "and", HashSet.of(0, 6)),
                "d2", HashMap.of("was", HashSet.of(2), "deep", HashSet.of(8), "darkness", HashSet.of(1), "the", HashSet.of(4, 7), "of", HashSet.of(6), "and", HashSet.of(0), "upon", HashSet.of(3), "face", HashSet.of(5)),
                "d3", HashMap.of("were", HashSet.of(6), "morning", HashSet.of(5), "evening", HashSet.of(2), "first", HashSet.of(8), "the", HashSet.of(1, 4, 7), "and", HashSet.of(0, 3), "day", HashSet.of(9))
            )
        );
            
        assertEquals(expected, index);

        var replaced = index.insert("d3", "and the spirit of god moved upon the face of the waters".split(" "));

        // removed "and the evening and the morning were the first day"
        var replacedExpected = new Index<String>(
            HashMap.of(
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
            )),
            HashMap.of(
                "d1", HashMap.of("form", HashSet.of(5), "without", HashSet.of(4), "was", HashSet.of(3), "earth", HashSet.of(2), "the", HashSet.of(1), "void", HashSet.of(7), "and", HashSet.of(0, 6)),
                "d2", HashMap.of("was", HashSet.of(2), "deep", HashSet.of(8), "darkness", HashSet.of(1), "the", HashSet.of(4, 7), "of", HashSet.of(6), "and", HashSet.of(0), "upon", HashSet.of(3), "face", HashSet.of(5)),
                "d3", HashMap.of("and", HashSet.of(0), "the", HashSet.of(1, 7, 10), "spirit", HashSet.of(2), "of", HashSet.of(3, 9), "god", HashSet.of(4), "moved", HashSet.of(5), "upon", HashSet.of(6), "face", HashSet.of(8), "waters", HashSet.of(11))
            )
        );

        assertEquals(replacedExpected, replaced);
    }

    @Test
    public void insertSame() {
        var collection = Parsing.makeCollection(
            "d1", "and the earth was without form and void",
            "d2", "and darkness was upon the face of the deep",
            "d3", "and the spirit of god moved upon the face of the waters"
        );

        var index = Index.of(collection);

        assertEquals(
            index,
            index.insert("d3", "and the spirit of god moved upon the face of the waters".split(" "))
        );
    }

    @Test
    public void remove() {
        var collection = Parsing.makeCollection(
            "d1", "and the earth was without form and void",
            "d2", "and darkness was upon the face of the deep",
            "d3", "and the spirit of god moved upon the face of the waters"
        );

        var actual = Index.of(collection).remove("d3");

        var expected = new Index<String>(
            HashMap.of(
                "and", HashMap.of("d1", HashSet.of(0, 6), "d2", HashSet.of(0)),
                "the", HashMap.of("d1", HashSet.of(1), "d2", HashSet.of(4, 7)),
                "earth", HashMap.of("d1", HashSet.of(2)),
                "was", HashMap.of("d1", HashSet.of(3), "d2", HashSet.of(2)),
                "without", HashMap.of("d1", HashSet.of(4)),
                "form", HashMap.of("d1", HashSet.of(5)),
                "void", HashMap.of("d1", HashSet.of(7)),
                "darkness", HashMap.of("d2", HashSet.of(1)),
                "upon", HashMap.of("d2", HashSet.of(3)),
                "face", HashMap.of("d2", HashSet.of(5))
            ).merge(HashMap.of(
                "of", HashMap.of("d2", HashSet.of(6)),
                "deep", HashMap.of("d2", HashSet.of(8))
            )),
            HashMap.of(
                "d1", HashMap.of("form", HashSet.of(5), "without", HashSet.of(4), "was", HashSet.of(3), "earth", HashSet.of(2), "the", HashSet.of(1), "void", HashSet.of(7), "and", HashSet.of(0, 6)),
                "d2", HashMap.of("was", HashSet.of(2), "deep", HashSet.of(8), "darkness", HashSet.of(1), "the", HashSet.of(4, 7), "of", HashSet.of(6), "and", HashSet.of(0), "upon", HashSet.of(3), "face", HashSet.of(5))
            )
        );

        assertEquals(expected, actual);
    }

    @Test
    public void removeInexisting() {
        var collection = Parsing.makeCollection(
            "d1", "and the earth was without form and void",
            "d2", "and darkness was upon the face of the deep",
            "d3", "and the spirit of god moved upon the face of the waters"
        );

        var index = Index.of(collection);

        assertEquals(index, index.remove("d4"));
    }


    @Test
    public void massive() {
        var folderPath = "..\\pracs-caim\\s1\\data\\raw\\20_newsgroups";
        var files = Parsing.parseFolder(folderPath);
        var corpus = HashMap.ofEntries(files);


        var start = Instant.now();
        var index = Index.of(corpus);
        var end = Instant.now();

        var time = Duration.between(start, end);

        System.out.println("Constructed in " + time.toMillis() + "ms");
    }
}
