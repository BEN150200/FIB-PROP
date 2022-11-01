package vectorialmodel;

import java.util.List;
import java.util.Map;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;

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
            HashMap.empty(),
            HashMap.empty()
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
        Index<DocId> index = Index.of(
            io.vavr.collection.HashMap.ofAll(collection)
        );

        // map documents to their max frequencies
        var hashedCollection = HashMap.ofAll(collection);

        HashMap<DocId, Long> maxFrequencies = hashedCollection.mapValues(Lists::maxFrequency);

        // map documents to their tfidf vectors
        HashMap<DocId, HashMap<String, Double>> tfidfs = hashedCollection.map(
            (docId, __) -> Tuple.of(docId,
            TFIDF.computeTFIDF(
                index.termsFrequencies(docId),
                maxFrequencies.get(docId).get(),
                collection.size()    
            ))
        );

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    /**
     * 
     * @param docId id of the document to be inserted
     * @param content content of the document to be inserted
     * @return a new VectorialModel with the document inserted. If it previously existed, it is replaced
     */
    public VectorialModel<DocId> insert(DocId docId, Iterable<String> content) {
        var newIndex = this.index.insert(docId, content);
        var newMaxFrequencies = this.maxFrequencies; // TODO
        var newTfidfVectors = this.tfidfVectors; // TODO
        return new VectorialModel<DocId>(newIndex, newMaxFrequencies, newTfidfVectors);
    }
    
    /**
     * 
     * @param docId
     * @return a new VectorialModel with docId (and its term occurrences) removed (if it existed)
     */
    public VectorialModel<DocId> remove(DocId docId) {
        var newIndex = this.index.remove(docId);
        var newMaxFrequencies = this.maxFrequencies; // TODO
        var newTfidfVectors = this.tfidfVectors; // TODO
        return new VectorialModel<DocId>(newIndex, newMaxFrequencies, newTfidfVectors);
    }

    /**
     * 
     * @param docId
     * @return mapping String -> tf-idf weight for all the Strings of the document docId
     */
    public java.util.HashMap<String, Double> tfidfVector(DocId docId) {
        return tfidfVectors.get(docId).map(HashMap::toJavaMap).get();
    }

    /**
     * @param docId
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with docId
     */
    public java.util.HashMap<DocId, Double> querySimilars(DocId docId) {
        var similarities = new java.util.HashMap<DocId, Double>();

        for(var tw : this.tfidfVector(docId).entrySet()) {
            var term = tw.getKey();
            var weight = tw.getValue();
            
            for(var posting : this.index.postingList(term)) {
                var otherId = posting._1;
                var otherWeight = this.tfidfVector(otherId).get(term);
                similarities.merge(docId, weight*otherWeight, Maps.add);
            }
        }

        return similarities;
    }
}
