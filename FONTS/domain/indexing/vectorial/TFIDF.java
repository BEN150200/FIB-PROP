package domain.indexing.vectorial;


import helpers.Maths;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;

public class TFIDF {

    /**
     * 
     * @param termFrequencies
     * @param maxFrequency
     * @param documentsCount
     * @return a normalized tfidf weight vector, 
     */
    public static Option<HashMap<String, Double>> computeTFIDF(HashMap<String, Integer> termFrequencies, long maxFrequency, long documentsCount, HashMap<String, Integer> documentFrequencies) {
        var v = termFrequencies.map((term, frequency) -> Tuple.of(
            term, 
            (frequency / (double) maxFrequency) * Maths.log2(documentsCount / (double) documentFrequencies.get(term).get())
        ));

        return Option.of(Maths.normalized(v));
    }
}
