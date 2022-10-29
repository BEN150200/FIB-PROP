package vectorialmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import helpers.Maps;
import helpers.Lists;

public class VectorialModel<DocId> {

    Index<DocId> index;
    HashMap<DocId, Long> maxFrequencies; // docId -> maxFreq of any String in the document
    HashMap<DocId, HashMap<String, Double>> tfidfVectors;   // docId -> String -> [position]

    private VectorialModel(Index<DocId> index, HashMap<DocId, Long> maxFrequencies, HashMap<DocId, HashMap<String, Double>> tfidfVectors) {
        this.index = index;
        this.maxFrequencies = maxFrequencies;
        this.tfidfVectors = tfidfVectors;
    }

    /**
     * 
     * @param <DocId> document id type
     * @return an empty VectorialModel, i.e., without no terms nor documents
     */
    public static <DocId> VectorialModel<DocId> empty() {
        return new VectorialModel<DocId>(
            Index.empty(),
            new HashMap<DocId, Long>(),
            new HashMap<DocId, HashMap<String, Double>>()
        );
    }

    /**
     * @param <String> Type used to represent Strings
     * @param <DocId> Type used to represent document IDs
     * @param collection Mapping docId -> Document forall documents of the collection
     * @return a vectorial model representation of the collection
     */
    public static <DocId> VectorialModel<DocId> of(Map<DocId, List<String>> collection) {
        // construct index
        Index<DocId> index = Index.of(collection);

        // map documents to their max frequencies
        HashMap<DocId, Long> maxFrequencies = collection.entrySet()
            .stream().parallel()
            .collect(Collectors.toMap(
                    Entry::getKey,
                    e -> Lists.maxFrequency(e.getValue()),
                    Maps::firstKey,
                    HashMap::new)
                );

        // map documents to their tfidf vectors
        HashMap<DocId, HashMap<String, Double>> tfidfs = collection.keySet()
            .stream().parallel()
            .collect(Collectors.toMap(
                Function.identity(),
                docId -> TFIDF.computeTFIDF(
                    index.termsFrequencies(docId),
                    maxFrequencies.get(docId),
                    collection.size()
                ),
                Maps::firstKey,
                HashMap::new)
            );

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    /**
     * 
     * @param docId
     * @return mapping String -> tf-idf weight for all the Strings of the document docId
     */
    public HashMap<String, Double> tfidfVector(DocId docId) {
        return tfidfVectors.get(docId);
    }

    /**
     * @param docId
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with docId
     */
    public HashMap<DocId, Double> querySimilars(DocId docId) {
        var similarities = new HashMap<DocId, Double>();

        for(var tw : this.tfidfVectors.get(docId).entrySet()) {
            var term = tw.getKey();
            var weight = tw.getValue();
            
            for(var dw : this.index.postingList(term).entrySet()) {
                var otherId = dw.getKey();
                var otherWeight = this.tfidfVectors.get(otherId).get(term);
                similarities.merge(docId, weight*otherWeight, Maps.add);
            }
        }

        return similarities;
    }
}
