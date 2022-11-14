package tests.domain.indexing.core.parsing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import src.domain.indexing.core.Parsing;

public class TestParsing {
    @Test
    public void emptyQuery() {
        assertEquals(
            Either.left("Unexpected empty query"),
            Parsing.weightedQuery("")
        );
    }

    @Test
    public void invalidBoost() {
        assertEquals(
            Either.left("Unexpected incomplete '^' found in ^"),
            Parsing.weightedQuery("^")
        );

        assertEquals(
            Either.left("Unexpected incomplete '^' found in ^^"),
            Parsing.weightedQuery("^^")
        );

        assertEquals(
            Either.left("Unexpected incomplete '^' found in ^3"),
            Parsing.weightedQuery("hello^2 hey ^3")
        );
    }

    @Test
    public void nonAlphaNumeric() {
        var expected = Either.left("Unexpected non-alphanumeric token ',.-'");

        assertEquals(
            expected,
            Parsing.weightedQuery(",.-")
        );

        assertEquals(
            expected,
            Parsing.weightedQuery(",.-^2")
        );
    }

    @Test
    public void manyBoosts() {
        assertEquals(
            Either.left("Only one '^' expected in hello^^4"),
            Parsing.weightedQuery("bye^1 hello^^4")
        );

        assertEquals(
            Either.left("Only one '^' expected in hello^^4"),
            Parsing.weightedQuery("bye^1 hello^^4")
        );
    }
}
