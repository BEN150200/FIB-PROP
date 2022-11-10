package domain.indexing.vectorial;

import domain.indexing.core.Index;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import helpers.Lists;
import helpers.Maths;

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
        var freqsTfidfs = index.directIndex.map(
            (docId, termsVector) -> {
                long maxFreq = termsVector.mapValues(HashSet::size).values().max().getOrElse(0);
                var tfidf = VectorialModel.computeTfidf(index, docId, maxFreq);
                return Tuple.of(
                    docId,
                    Tuple.of(maxFreq, tfidf)
                );
            }
        );

        var maxFrequencies = freqsTfidfs.<Long>mapValues(Tuple2::_1);
        var tfidfs = freqsTfidfs.<HashMap<String, Double>>mapValues(Tuple2::_2);

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    /**
     * @param <String> Type used to represent Strings
     * @param <DocId> Type used to represent document IDs
     * @param collection Mapping docId -> Document forall documents of the collection
     * @return a vectorial model representation of the collection
     */
    public static <DocId> VectorialModel<DocId> of(java.util.Map<DocId, Iterable<String>> collection) {
        
        // map documents to their max frequencies
        var hashedCollection = HashMap.ofAll(collection);

        // construct index
        Index<DocId> index = Index.of(hashedCollection);

        HashMap<DocId, Long> maxFrequencies = hashedCollection.mapValues(Lists::maxFrequency);

        // map documents to their tfidf vectors
        HashMap<DocId, HashMap<String, Double>> tfidfs = hashedCollection.map(
            (docId, __) -> Tuple.of(
                docId,
                VectorialModel.computeTfidf(index, docId, maxFrequencies.get(docId).get())
            )
        );

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    private static <DocId> HashMap<String, Double> computeTfidf(Index<DocId> index, DocId docId, Long maxFrequency) {
        return TFIDF.computeTFIDF(
                    index.termsFrequencies(docId).getOrElse(HashMap::empty),
                    maxFrequency,
                    index.documentsCount(),
                    index.termsDocumentFrequencies(docId).getOrElse(HashMap::empty)
                );
    }

    private static <DocId> HashMap<String, Double> computeTfidf(Index<DocId> index, DocId docId) {
        return computeTfidf(index, docId, Lists.maxFrequency(index.termsFrequencies(docId)));
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
        var oldTerms = this.index.terms(docId).getOrElse(HashSet::empty);
        var newTerms = newIndex.terms(docId).getOrElse(HashSet::empty);
        
        var removedTerms = oldTerms.diff(newTerms);
        var addedTerms = newTerms.diff(oldTerms);

        var newMaxFrequency = Lists.maxFrequency(newTerms);
        var newMaxFrequencies = this.maxFrequencies.put(docId, newMaxFrequency);

        HashMap<String, Double> newTfidf = VectorialModel.computeTfidf(newIndex, docId, newMaxFrequency);
        
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

        var removedTerms = this.index.terms(docId).getOrElse(HashSet::empty);
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
    public Option<HashMap<String, Double>> tfidfVector(DocId docId) {
        return tfidfVectors.get(docId);
    }

    /**
     * @param docId
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with docId
     */
    public Option<HashMap<DocId, Double>> querySimilars(DocId docId) {
        return this.tfidfVector(docId).map(this::querySimilars);
    }

    /**
     * 
     * @param termsWeights
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with the given (term, tfidf weight) vector
     */
    @SuppressWarnings("deprecation")
    public HashMap<DocId, Double> querySimilars(Map<String, Double> termsWeights) {
        return termsWeights.map(
            (term, weight) -> Tuple.of(term, (HashMap<DocId, Double>) this.index.documents(term).toMap(
                docId -> docId,
                docId -> this.tfidfVector(docId).get().get(term).get()*weight
            ))
        )
        .foldLeft(
            HashMap.<DocId, Double>empty(),
            (acc, cur) -> acc.merge(cur._2, Maths::add)
        );
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
