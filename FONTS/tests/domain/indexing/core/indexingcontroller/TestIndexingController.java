package domain.indexing.core.indexingcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.control.Either;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.core.Index;
import src.domain.indexing.core.IndexingController;
import src.domain.indexing.core.Parsing;
import src.domain.indexing.vectorial.VectorialModel;

public class TestIndexingController {

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

    // should be equivalent to Index.of(corpus)
    Index<String> index = new Index<String>(invertedIndex, directIndex);

    IndexingController<String, String> indexing = IndexingController.of(
        VectorialModel.of(index),
        BooleanModel.of(index)
    );

    @Test
    // querying a docId should yield similar results to querying its content.
    // (not equal since the model uses tf-idf weights. The query, 1 for all)
    public void queriesEquivalence() {
        var res1 = indexing.querySimilarDocuments("d1").get();
        var res2 = indexing.weightedQuery("and the earth was without form and void").get();
        res2.forEach(
            (doc, sim) -> assertTrue(Math.abs(sim - res1.get(doc)) < 0.2)
        );

    }

    @Test
    public void weights() {
        // d1 contains earth, d2 contains darkness
        var res1 = indexing.weightedQuery("earth^2 darkness").get();
        var res2 = indexing.weightedQuery("earth darkness^2").get();

        assertTrue(res1.get("d1") > res1.get("d2"));
        assertTrue(res2.get("d1") < res2.get("d2"));
    }
}
