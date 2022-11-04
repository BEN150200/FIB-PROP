package vectorialmodel;

import helpers.Maths;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;

public class TFIDF {

    /**
     * 
     * @param termFrequencies
     * @param maxFrequency
     * @param documentsCount
     * @return a normalized tfidf weight vector
     */
    public static HashMap<String, Double> computeTFIDF(HashMap<String, Integer> termFrequencies, long maxFrequency, long documentsCount, HashMap<String, Integer> documentFrequencies) {
        var v = termFrequencies.map((term, frequency) -> Tuple.of(
            term, 
            (frequency / (double) maxFrequency) * Maths.log2(documentsCount / (double) documentFrequencies.get(term).get())
        ));

        var norm = Math.sqrt(
            v.values().map(Maths::square).sum().doubleValue()
        );

        return v.mapValues(Maths.divideBy(norm)); // normalized v
    }
}
