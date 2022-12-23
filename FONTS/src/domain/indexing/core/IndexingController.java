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

    /**
     * adds a document to the models
     */
    public void addDocument(DocId docId, Iterable<String> content) {
        this.vectorialModel = this.vectorialModel.thenApplyAsync(model -> model.insert(docId, content));
    }
    
    /**
     * adds a sentence to the models
     * @param sentenceId
     * @param content
     */
    public void addSentence(SentenceId sentenceId, Iterable<String> content) {
        this.booleanModel = this.booleanModel.thenApplyAsync(model -> model.insert(sentenceId, content));
    }
    
    /**
     * removes a document from the models
     * @param docId
     */
    public void removeDocument(DocId docId) {
        this.vectorialModel = this.vectorialModel.thenApplyAsync(model -> model.remove(docId));
    }

    /**
     * removes a sentence from the models
     * @param sentenceId
     */
    public void removeSentence(SentenceId sentenceId) {
        this.booleanModel = this.booleanModel.thenApplyAsync(model -> model.remove(sentenceId));
    }   

    /**
     * 
     * @param docId
     * @return a future of the document ids similar to that with the given id, or an error if the document doesn't exist
     */
    @SuppressWarnings("deprecation")
    public CompletableFuture<Either<String, HashMap<DocId, Double>>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.thenApply(model -> model.querySimilars(docId).toEither("DocId " + docId + " does not exist"));
    }

    /**
     * 
     * @param root
     * @return a set of the sentence ids matching the expression
     */
    public HashSet<SentenceId> booleanQuery(ExpressionTreeNode root) {
        return booleanModel.join().query(root);
    }

    /**
     * 
     * @param query
     * @return a future of the document ids similar to the query, or an error if sintactically incorrect
     */
    public CompletableFuture<Either<String, HashMap<DocId, Double>>> weightedQuery(String query) {
        var parsedQuery = Parsing.weightedQuery(query);
        if(parsedQuery.isLeft())
            return CompletableFuture.supplyAsync(() -> Either.left(parsedQuery.getLeft()));
        
        return vectorialModel.thenApply(model -> Either.right(model.querySimilars(parsedQuery.get())));
    }
}
