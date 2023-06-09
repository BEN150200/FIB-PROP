package src.domain.indexing.vectorial;

import src.domain.indexing.core.Index;

import src.helpers.Maps;
import src.helpers.Maths;
import src.helpers.Sets;

import java.util.Optional;
import java.util.function.Function;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class VectorialModel<DocId> {

    Index<DocId> _index;
    HashMap<DocId, Long> _maxFrequencies; // docId -> maxFreq of any String in the document
    HashMap<DocId, HashMap<String, Double>> _tfidfVectors;   // docId -> String -> [position]
    Function<String, Optional<String>> _tokenFilter;

    private VectorialModel(
        Index<DocId> index, HashMap<DocId, Long> maxFrequencies,
        HashMap<DocId, HashMap<String, Double>> tfidfVectors) 
    {
        _index = index;
        _maxFrequencies = maxFrequencies;
        _tfidfVectors = tfidfVectors;
        _tokenFilter = Optional::of;
    }

    private VectorialModel(
        Index<DocId> index, HashMap<DocId, Long> maxFrequencies,
        HashMap<DocId, HashMap<String, Double>> tfidfVectors,
        Function<String, Optional<String>> tokenFilter) 
    {
        _index = index;
        _maxFrequencies = maxFrequencies;
        _tfidfVectors = tfidfVectors;
        _tokenFilter = tokenFilter;
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
     * 
     * @param <DocId>
     * @param tokenFilter a function to filter all inserted/queried/stored terms (i.e. tokens)
     * @return an empty VectorialModel using tokenFilter
     */
    public static <DocId> VectorialModel<DocId> empty(Function<String, Optional<String>> tokenFilter)
    {
        return new VectorialModel<DocId>(
            Index.empty(),
            HashMap.empty(),
            HashMap.empty(),
            tokenFilter
        );
    }

    /**
     * 
     * @param <DocId>
     * @param index an index 
     * @return
     */
    public static <DocId> VectorialModel<DocId> of(Index<DocId> index)
    {
        var freqsTfidfs = index._directIndex.map(
            (docId, termsVector) -> {
                long maxFreq = termsVector.mapValues(HashSet::size).values().max().getOrElse(0);
                var tfidf = VectorialModel.computeTfidf(index, docId, maxFreq);
                return Tuple.of(
                    docId,
                    Tuple.of(maxFreq, tfidf)
                );
            }
        );

        var maxFrequencies = freqsTfidfs.mapValues(Tuple2::_1);
        var tfidfs = freqsTfidfs.mapValues(Tuple2::_2);

        return new VectorialModel<DocId>(index, maxFrequencies, tfidfs);
    }

    private static <DocId> HashMap<String, Double> computeTfidf(Index<DocId> index, DocId docId, Long maxFrequency) {
        return TFIDF.computeTFIDF(
                    index.frequencies(docId).getOrElse(HashMap::empty),
                    maxFrequency,
                    index.documentsCount(),
                    index.documentFrequencies(docId).getOrElse(HashMap::empty)
                );
    }

    /**
     * 
     * @param docId id of the document to be inserted
     * @param content content of the document to be inserted
     * @return a new VectorialModel with the document inserted. If it previously existed, it is replaced
     */
    public VectorialModel<DocId> insert(DocId docId, Iterable<String> content) {
        var filteredContent = List.ofAll(content).map(_tokenFilter).filter(Optional::isPresent).map(Optional::get);
        var newIndex = _index.insert(docId, filteredContent);
        // terms changed = (new U old) - (new int old)
        var oldTerms = _index.terms(docId).getOrElse(HashSet::empty);
        var newTerms = newIndex.terms(docId).getOrElse(HashSet::empty);
        
        var removedTerms = oldTerms.diff(newTerms);
        var addedTerms = newTerms.diff(oldTerms);

        var newMaxFrequencies = _maxFrequencies.put(
            docId,
            (long) newIndex.maxFrequency(docId)
        );

        var dirtyDocs =
            Sets.unionFold(removedTerms, _index::documents).union(
            Sets.unionFold(addedTerms, newIndex::documents).add(docId)
        );

        var newTfidfVectors = dirtyDocs.foldLeft(
            _tfidfVectors,
            (tfidfs, dirtyDocId) -> tfidfs.put(
                dirtyDocId,
                VectorialModel.computeTfidf(newIndex, dirtyDocId, newMaxFrequencies.get(dirtyDocId).get())
            )
        );

        return new VectorialModel<DocId>(newIndex, newMaxFrequencies, newTfidfVectors, _tokenFilter);
    }
    
    /**
     * 
     * @param docId
     * @return a new VectorialModel with docId (and its term occurrences) removed (if it existed)
     */
    public VectorialModel<DocId> remove(DocId docId)
    {
        var newIndex = _index.remove(docId);
        var newMaxFrequencies = _maxFrequencies.remove(docId);

        var removedTerms = _index.terms(docId).getOrElse(HashSet::empty);
        var dirtyDocuments = removedTerms
            .map(_index::documents)
            .fold(HashSet.empty(), Set::union)
            .remove(docId);

        var newTfidfVectors = dirtyDocuments.foldLeft(
            _tfidfVectors.remove(docId),
            (tfidfs, dirtyDocId) -> tfidfs.put(
                dirtyDocId,
                VectorialModel.computeTfidf(newIndex, dirtyDocId, newMaxFrequencies.get(dirtyDocId).get())
            )
        );

        return new VectorialModel<DocId>(newIndex, newMaxFrequencies, newTfidfVectors, _tokenFilter);
    }

    /**
     * 
     * @param docId
     * @return mapping String -> tf-idf weight for all the Strings of the document docId
     */
    public Option<HashMap<String, Double>> tfidfVector(DocId docId)
    {
        return _tfidfVectors.get(docId);
    }

    /**
     * @param docId
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with docId
     */
    public Option<HashMap<DocId, Double>> querySimilars(DocId docId)
    {
        return this.tfidfVector(docId).map(this::querySimilars);
    }

    /**
     * 
     * @param termsWeights a (not necessarily normalized) terms+weights vector
     * @return mapping doc_id -> cosine_similarity for all documents with similarity > 0 with the given (term, tfidf weight) vector
     */
    public HashMap<DocId, Double> querySimilars(HashMap<String, Double> termsWeights)
    {
        var filteredTerms = termsWeights.mapKeys(_tokenFilter).filterKeys(Optional::isPresent).mapKeys(Optional::get);

        return Maths.normalized(filteredTerms)
            .map(
                (term, weight) -> Tuple.of(term, Maps.fromTraversable(
                    _index.documents(term),
                    docId -> Maps.nestedGet(_tfidfVectors, docId, term)*weight
                ))
            )
            .values()
            .foldLeft(
                HashMap.<DocId, Double>empty(),
                Maps::addingMerge
            );
    }

    @Override
    public String toString() {
        return String.format(
            "Index:\n%s\nMaxFrequencies\n%s\nTFIDF vectors\n%s",
            _index.toString(),
            _maxFrequencies.mkString("\t", "\n\t", ""),
            _tfidfVectors.mkString("\t", "\n\t", "")
        );
    }

    public String printVectors() {
        return _tfidfVectors.mkString("\t", "\n\t", "");
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public boolean equals(Object obj) {
        if(!(this.getClass().isInstance(obj))) return false;
        
        var other = (VectorialModel<DocId>) obj;
        return _index.equals(other._index) &&
                _maxFrequencies.eq(other._maxFrequencies) &&
                _tfidfVectors.eq(other._tfidfVectors);
    }
}
