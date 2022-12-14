package src.domain.indexing.core;

import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.vectorial.VectorialModel;
import src.domain.preprocessing.TokenFilter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.control.Either;

public class IndexingController<DocId, SentenceId> {
    private CompletableFuture<VectorialModel<DocId>> vectorialModel;
    private CompletableFuture<BooleanModel<SentenceId>> booleanModel;
    
    // or exception plus add `updateDocument` method?
    public IndexingController() {
        this.vectorialModel = CompletableFuture.completedFuture(VectorialModel.empty(TokenFilter::filter));
        this.booleanModel = CompletableFuture.completedFuture(BooleanModel.empty());
    }

    public static <DocId, SentenceId> IndexingController<DocId, SentenceId> of(VectorialModel<DocId> vectorialModel, BooleanModel<SentenceId> booleanModel) {
        return new IndexingController<DocId, SentenceId>(vectorialModel, booleanModel);
    }
    
    private IndexingController(VectorialModel<DocId> vectorialModel, BooleanModel<SentenceId> booleanModel) {
        this.vectorialModel = CompletableFuture.completedFuture(vectorialModel);
        this.booleanModel = CompletableFuture.completedFuture(booleanModel);
    }

    public void addDocument(DocId docId, Iterable<String> content) {
        this.vectorialModel = this.vectorialModel.thenApplyAsync(model -> model.insert(docId, content));
    }
    
    public void addSentence(SentenceId sentenceId, Iterable<String> content) {
        this.booleanModel = this.booleanModel.thenApplyAsync(model -> model.insert(sentenceId, content));
    }
    
    public void removeDocument(DocId docId) {
        this.vectorialModel = this.vectorialModel.thenApplyAsync(model -> model.remove(docId));
    }

    public void removeSentence(SentenceId sentenceId) {
        this.booleanModel = this.booleanModel.thenApplyAsync(model -> model.remove(sentenceId));
    }

    public java.util.HashMap<DocId, Double> weightedQuery(Map<String, Double> termsWeights) {
        return this.vectorialModel.join().querySimilars(HashMap.ofAll(termsWeights)).toJavaMap();
    }

    @SuppressWarnings("deprecation")
    public Either<String, java.util.HashMap<DocId, Double>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.join().querySimilars(docId).map(HashMap::toJavaMap).toEither("DocId " + docId + " does not exist");
    }

    public HashSet<SentenceId> booleanQuery(ExpressionTreeNode root) {
        return booleanModel.join().query(root);
    }

    public Either<String, HashMap<DocId, Double>> weightedQuery(String query) {
        return  Parsing
            .weightedQuery(query)
            .map(termsWeights -> vectorialModel.join().querySimilars(termsWeights));
    }
}
