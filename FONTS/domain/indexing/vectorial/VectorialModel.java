package domain.indexing.vectorial;

import java.util.Map;

import domain.indexing.core.Index;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
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

    public static <DocId> VectorialModel<DocId> of(Index<DocId> index) {
        var maxFrequencies = index.directIndex.map(
            (docId, termsVector) -> Tuple.of(
                docId,
                (long) termsVector.mapValues(HashSet::size).values().max().get()
            )
        );

        var tfidfs = index.directIndex.map(
            (docId, __) -> Tuple.of(docId,
            TFIDF.computeTFIDF(
                index.termsFrequencies(docId),
                maxFrequencies.get(docId).get(),
                index.documentsCount(),
                index.termsDocumentFrequencies(docId)
            ))
        );

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    /**
     * @param <String> Type used to represent Strings
     * @param <DocId> Type used to represent document IDs
     * @param collection Mapping docId -> Document forall documents of the collection
     * @return a vectorial model representation of the collection
     */
    public static <DocId> VectorialModel<DocId> of(Map<DocId, Iterable<String>> collection) {
        
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
                index.documentsCount(),
                index.termsDocumentFrequencies(docId)
            ))
        );

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    private static <DocId> HashMap<String, Double> computeTfidf(Index<DocId> index, DocId docId) {
        return TFIDF.computeTFIDF(
                    index.termsFrequencies(docId),
                    Lists.maxFrequency(index.termsFrequencies(docId)),
                    index.documentsCount(),
                    index.termsDocumentFrequencies(docId)
                );
    }

    /**
     * 
     * @param docId id of the document to be inserted
     * @param content content of the document to be inserted
     * @return a new VectorialModel with the document inserted. If it previously existed, it is replaced
     */
    public VectorialModel<DocId> insert(DocId docId, Iterable<String> content) {
        var newIndex = this.index.insert(docId, content);
        // terms changed = (new U old) - (new int old)
        var oldTerms = this.index.terms(docId);
        var newTerms = newIndex.terms(docId);
        
        var removedTerms = oldTerms.diff(newTerms);
        var addedTerms = newTerms.diff(oldTerms);

        var newMaxFrequency = Lists.maxFrequency(newTerms);
        var newMaxFrequencies = this.maxFrequencies.put(docId, newMaxFrequency);

        var newTfidf = TFIDF.computeTFIDF(
            newIndex.termsFrequencies(docId),
            newMaxFrequency,
            newIndex.documentsCount(),
            newIndex.termsDocumentFrequencies(docId)
        );
        
        var removedDirty = removedTerms.foldLeft(
            HashSet.<DocId>empty(),
            (dirty, removedTerm) -> dirty.union(this.index.documents(removedTerm))
        );

        var addedDirty = addedTerms.foldLeft(
            HashSet.<DocId>empty(),
            (dirty, addedTerm) -> dirty.union(newIndex.documents(addedTerm))
        );

        var newTfidfVectors = removedDirty.union(addedDirty).foldLeft(
            this.tfidfVectors.put(docId, newTfidf),
            (tfidfs, dirtyDocId) -> tfidfs.put(
                dirtyDocId,
                VectorialModel.computeTfidf(newIndex, dirtyDocId)
            )
        );

        return new VectorialModel<DocId>(newIndex, newMaxFrequencies, newTfidfVectors);
    }
    
    /**
     * 
     * @param docId
     * @return a new VectorialModel with docId (and its term occurrences) removed (if it existed)
     */
    public VectorialModel<DocId> remove(DocId docId) {
        var newIndex = this.index.remove(docId);
        var newMaxFrequencies = this.maxFrequencies.remove(docId);

        var removedTerms = this.index.terms(docId);
        var dirtyDocuments = removedTerms
            .map(this.index::documents)
            .fold(HashSet.empty(), Set::union);

        var newTfidfVectors = dirtyDocuments.foldLeft(
            this.tfidfVectors.remove(docId),
            (tfidfs, dirtyDocId) -> tfidfs.put(
                dirtyDocId,
                VectorialModel.computeTfidf(index, dirtyDocId)
            )
        );

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
        return this.querySimilars(this.tfidfVector(docId));
    }

    /**
     * 
     * @param termsWeights
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with the given (term, tfidf weight) vector
     */
    public java.util.HashMap<DocId, Double> querySimilars(Map<String, Double> termsWeights) {
        var similarities = new java.util.HashMap<DocId, Double>();

        for(var tw : termsWeights.entrySet()) {
            var term = tw.getKey();
            var weight = tw.getValue();
            
            for(var posting : this.index.postingList(term)) {
                var docId = posting._1;
                var otherWeight = this.tfidfVector(docId).get(term);
                similarities.merge(docId, weight*otherWeight, Maps.add);
            }
        }

        return similarities;
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public boolean equals(Object obj) {
        if(!(this.getClass().isInstance(obj))) return false;
        
        var other = (VectorialModel<DocId>) obj;
        return this.index.equals(other.index) &&
                this.maxFrequencies.eq(other.maxFrequencies) &&
                this.tfidfVectors.eq(other.tfidfVectors);
    }
}
