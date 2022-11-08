package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import domain.indexing.core.Index;
import domain.indexing.vectorial.VectorialModel;
import helpers.Maths;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TestVectorialModel {

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

        var similars = model.querySimilars("d3");

        assertEquals(
            0.035,
            similars.get("d4"),
            0.001 // up to 3 decimal places
        );

        for(var entry : similars.entrySet()) {
            var docId = entry.getKey();
            var similarity = entry.getValue();
            System.out.println("%s: %f".formatted(docId, similarity));
        }
    }
}
