package tests.domain.indexing.core.Index;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import domain.indexing.core.Index;
import helpers.Parsing;
import helpers.Strings;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;

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
    public void onlyTerm() {
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
    public void massive() {
        var folderPath = "..\\pracs-caim\\s1\\data\\raw\\20_newsgroups";
        var files = Parsing.parseFolder(folderPath);

        var corpus = HashMap.ofEntries(files);

        var start = Instant.now();
        var foldedIndex = corpus.foldLeft(
            Index.<String>empty(),
            Index::insert
        );
        var mid = Instant.now();
        var constructedIndex = Index.of(corpus);
        var end = Instant.now();

        var timeFolded = Duration.between(start, mid);
        var timeConstructed = Duration.between(mid, end);

        System.out.println(
            "Folded took " + timeFolded.toMillis() + "ms\n" +
            "Constructed took " + timeConstructed.toMillis() + "ms"
        );
    }
}
