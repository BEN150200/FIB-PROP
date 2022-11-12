package tests.domain.indexing.vectorial.TFIDF;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import domain.indexing.vectorial.TFIDF;
import helpers.Maths;
import io.vavr.collection.HashMap;

public class TestTFIDF {
    @Test
    public void empty() {
        var actual = TFIDF.computeTFIDF(
            HashMap.empty(),
            0, 0,
            HashMap.empty()
        );

        assertEquals(HashMap.empty(), actual);
    }

    @Test
    public void termInAll() {
        var actual = TFIDF.computeTFIDF(
            HashMap.of("all", 10),
            10, 2,
            HashMap.of("all", 2)
        );

        var expected = HashMap.of("all", 0.0d);

        assertEquals(expected, actual);
    }

    @Test
    public void singleTerms() {
        var frequencies = HashMap.of(
            "order", 10,
            "chaos", 10
        );

        var actual = TFIDF.computeTFIDF(
            frequencies,
            10, 2,
            HashMap.of("order", 1, "chaos", 1)
        );

        assertEquals(
            Math.sqrt(2)/2.0d,
            actual.get("order").get(),
            0.01d
        );

        assertEquals(
            Math.sqrt(2)/2.0d,
            actual.get("chaos").get(),
            0.01d
        );
    }

    @Test
    // see https://www.cs.upc.edu/~caim/slides/2ir_models.pdf for computations
    public void smallCorpus() {
        var docFrequencies = HashMap.of(
            "five", 2,  "four", 3,
            "one", 3,   "six", 3,
            "three", 6, "two", 2
        );

        var freqsDoc3 = HashMap.of(
            "five", 3,  "four", 1,
            "one", 1,   "three", 1
        );

        var tfidf3 = TFIDF.computeTFIDF(
            freqsDoc3,
            3, 7,
            docFrequencies
        );

        var expected3 = Maths.normalized(HashMap.of(
            "five", 1.81,  "four", 0.41,
            "one", 0.41,   "three", 0.07
        ));

        expected3.forEach((t, expectedWeight) -> {
            var w = tfidf3.getOrElse(t, 0.0);
            assertEquals(
                String.format(
                    "w(%s) = %f, expected %f",
                    t, w, expectedWeight
                ),
                expectedWeight, w,
                0.01d
            );
        });

        var freqsDoc4 = HashMap.of(
            "one", 1,   "six", 2,
            "three", 1,  "two", 4
        );

        var tfidf4 = TFIDF.computeTFIDF(
            freqsDoc4,
            4, 7,
            docFrequencies
        );

        var expected4 = Maths.normalized(HashMap.of(
            "one", 0.61,   "six", 1.22,
            "three", 0.11,  "two", 3.61
        ));

        expected4.forEach((t, w) -> assertEquals(
            w, tfidf4.getOrElse(t, 0.0),
            0.01d
        ));
    }
}
