package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.vavr.collection.HashMap;
import src.domain.preprocessing.Stemmer;

public class StemmerTest {
    @Test
    public void test() {
        HashMap.of(
            "singing", "sing",
            "exceptionally", "exception",
            "cantant", "cant"
        )
        .forEach((term, expected) ->
            assertEquals(
                expected, Stemmer.stem(term)
            )
        );
    }
}
