package domain.indexing.core;

import java.util.Map;

import domain.indexing.booleanmodel.BooleanModel;
import domain.indexing.booleanmodel.ExpressionTree;
import domain.indexing.vectorial.VectorialModel;
import io.vavr.control.Either;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class IndexingController<DocId, SentenceId> {
    private VectorialModel<DocId> vectorialModel;
    private BooleanModel<SentenceId> booleanModel;
    
    // TODO: return boolean of wheter inserted new or updated,
    // or exception plus add `updateDocument` method?
    public IndexingController() {
        vectorialModel = VectorialModel.empty();
        booleanModel = BooleanModel.empty();
    }
    
    public void addDocument(DocId docId, Iterable<String> content) {
        this.vectorialModel = this.vectorialModel.insert(docId, content);
    }
    
    // TODO: return boolean of wheter inserted new or updated,
    // or exception plus add `updateDocument` method?
    public void addSentence(SentenceId sentenceId, Iterable<String> content) {
        this.booleanModel = this.booleanModel.insert(sentenceId, content);
    }
    
    // TODO: return boolean of whether it was removed, or exception?
    public void removeDocument(DocId docId) {
        this.vectorialModel = this.vectorialModel.remove(docId);
    }

    // TODO: return boolean of whether it was removed, or exception?
    public void removeSentence(SentenceId sentenceId) {
        this.booleanModel = this.booleanModel.remove(sentenceId);
    }

    public HashMap<DocId, Double> weightedQuery(Map<String, Double> termsWeights) {
        return this.vectorialModel.querySimilars(termsWeights);
    }

    @SuppressWarnings("deprecation")
    public Either<Exception, HashMap<DocId, Double>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.querySimilars(docId).toEither(() -> new InvalidParameterException("DocId " + docId + " does not exist"));
    }

    // TODO
    public Either<Exception, Iterable<SentenceId>> booleanQuery(ExpressionTree expressionTree) {
        return Either.left(new UnsupportedOperationException("Not yet implemented"));
    }
}
