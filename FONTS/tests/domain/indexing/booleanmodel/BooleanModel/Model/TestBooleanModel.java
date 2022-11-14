package domain.indexing.booleanmodel.BooleanModel.Model;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.core.Index;

public class TestBooleanModel {

    /*private HashMap<String, Iterable<String>> corpus = Parsing.makeCollection(
        "d1", "and the earth was without form and void",
        "d2", "and darkness was upon the face of the deep",
        "d3", "and the spirit of god moved upon the face of the waters"
    );*/
    // should be equivalent to Index.of(corpus), but it's a stub!
    private BooleanModel<String> model = BooleanModel.of(
        new Index<String>(
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
        )
    );

    // BooleanModel::queryTerm
    @Test
    public void nonExistingTerm() {
        assertEquals(
            HashSet.empty(),
            model.queryTerm("divine")
        );
    }

    @Test
    public void inAllTerm() {
        assertEquals(
            HashSet.of("d1", "d2", "d3"),
            model.queryTerm("the")
        );
    }

    @Test
    public void termEmpty() {
        assertEquals(
            HashSet.empty(),
            BooleanModel.empty().queryTerm("")
        );

        assertEquals(
            HashSet.empty(),
            BooleanModel.empty().queryTerm("as")
        );
    }

    // BooleanModel::querySet
    @Test
    public void nonExistingSet() {
        assertEquals(
            HashSet.empty(),
            model.querySet(Arrays.asList("is this the region, this is the soil, the clime".split(" ")))
        );
    }

    @Test
    public void almostButNoSet() {
        assertEquals(
            HashSet.empty(),
            model.querySet(Arrays.asList("and the earth moved".split(" ")))
        );
    }

    @Test
    public void allSet() {
        assertEquals(
            HashSet.of("d1", "d2", "d3"),
            model.querySet(List.of("and", "the"))
        );
    }

    // BooleanModel::querySequence
    @Test
    public void nonExistingSequence() {
        assertEquals(
            HashSet.empty(),
            model.querySequence(Arrays.asList("Said then the lost Arch-Angel, this the seat".split(" ")))
        );
    }

    @Test
    public void orderMatters() {
        assertEquals(
            HashSet.empty(),
            model.querySequence(Arrays.asList("and the earth was without void and form".split(" ")))
        );

        assertEquals(
            HashSet.of("d1"),
            model.querySequence(Arrays.asList("and the earth was without form and void".split(" ")))
        );
    }

    @Test
    public void matchSequence() {
        assertEquals(
            HashSet.of("d2", "d3"),
            model.querySequence(List.of("the", "face"))
        );
    }

    // BooleanModel::all
    @Test
    public void allDocIds() {
        assertEquals(
            HashSet.of("d1", "d2", "d3"),
            model.all()
        );
    }
}
