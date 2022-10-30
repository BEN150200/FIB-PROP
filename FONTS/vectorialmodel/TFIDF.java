package vectorialmodel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import helpers.Maths;

public class TFIDF {

    public static HashMap<String, Double> computeTFIDF(HashMap<String, Integer> StringFrequencies, long maxFrequency, long documentsCount) {
        Function<Integer, Double> tfidf = frequency -> (frequency / maxFrequency) * Maths.log2(documentsCount / maxFrequency);
        
        return StringFrequencies.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                tfidf.compose(Map.Entry::getValue),
                (k1, k2) -> k1,
                HashMap::new
            ));
    }
}
