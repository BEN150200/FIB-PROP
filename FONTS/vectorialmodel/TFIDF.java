package vectorialmodel;

import helpers.Maths;
import io.vavr.collection.HashMap;

public class TFIDF {

    public static HashMap<String, Double> computeTFIDF(HashMap<String, Integer> termFrequencies, long maxFrequency, long documentsCount) {
        return termFrequencies.mapValues(
            frequency -> (frequency / maxFrequency) * Maths.log2(documentsCount / maxFrequency)
        );
    }
}
