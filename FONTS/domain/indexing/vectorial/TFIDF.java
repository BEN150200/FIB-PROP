package domain.indexing.vectorial;


import helpers.Maths;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class TFIDF {

    /**
     * 
     * @param termFrequencies
     * @param maxFrequency
     * @param documentsCount
     * @return a normalized tfidf weight vector, if any term is present, else empty
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, Double> computeTFIDF(HashMap<String, Integer> termFrequencies, long maxFrequency, long documentsCount, HashMap<String, Integer> documentFrequencies) {
        return Try.of(() -> termFrequencies.map((term, frequency) -> Tuple.of(
            term, 
            (frequency / (double) maxFrequency) * Maths.log2(documentsCount / (double) documentFrequencies.get(term).get())
        )))
        .getOrElse(HashMap::empty);
    }
}
