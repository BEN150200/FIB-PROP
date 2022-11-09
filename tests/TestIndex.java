package tests;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import domain.indexing.booleanmodel.BooleanModel;
import domain.indexing.core.Index;
import helpers.Parsing;
import io.vavr.collection.HashMap;

public class TestIndex {
    @Test
    public void testPrint() {
        var index = Index.of(HashMap.of(
            1, List.of("hello", "there", "hello"),
            2, List.of("there", "after")
        ));

        System.out.println(Index.print(index));
    }

    @Test
    public void testBoolean() {
        var model = BooleanModel.of(Map.of(
            1, Arrays.asList("hello how are you ma man".split(" ")),
            2, Arrays.asList("very well how are you man".split(" "))
        ));

        assertEquals(model.all(), Set.of(1, 2));

        assertEquals(model.querySequence(List.of("how", "are", "you")), Set.of(1, 2));
        assertEquals(model.querySequence(List.of("how", "are", "you", "ma")), Set.of(1));
    }

    @Test
    public void testConstruction() {
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
