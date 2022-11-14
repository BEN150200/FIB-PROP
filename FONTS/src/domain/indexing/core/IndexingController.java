package src.domain.indexing.core;

import src.domain.core.ExpressionTreeNode;
import src.domain.indexing.booleanmodel.BooleanModel;
import src.domain.indexing.vectorial.VectorialModel;

import src.helpers.Maths;
import src.helpers.Parsing;
import src.helpers.Strings;

import java.util.Arrays;
import java.util.Map;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Stream;
import io.vavr.control.Either;

public class IndexingController<DocId, SentenceId> {
    private VectorialModel<DocId> vectorialModel;
    private BooleanModel<SentenceId> booleanModel;
    
    // or exception plus add `updateDocument` method?
    public IndexingController() {
        this.vectorialModel = VectorialModel.empty();
        this.booleanModel = BooleanModel.empty();
    }

    public static <DocId, SentenceId> IndexingController<DocId, SentenceId> of(VectorialModel<DocId> vectorialModel, BooleanModel<SentenceId> booleanModel) {
        return new IndexingController<DocId, SentenceId>(vectorialModel, booleanModel);
    }
    
    private IndexingController(VectorialModel<DocId> vectorialModel, BooleanModel<SentenceId> booleanModel) {
        this.vectorialModel = vectorialModel;
        this.booleanModel = booleanModel;
    }


    public void addDocument(DocId docId, Iterable<String> content) {
        this.vectorialModel = this.vectorialModel.insert(docId, content);
    }
    
    public void addSentence(SentenceId sentenceId, Iterable<String> content) {
        this.booleanModel = this.booleanModel.insert(sentenceId, content);
    }
    
    public void removeDocument(DocId docId) {
        this.vectorialModel = this.vectorialModel.remove(docId);
    }

    public void removeSentence(SentenceId sentenceId) {
        this.booleanModel = this.booleanModel.remove(sentenceId);
    }

    public java.util.HashMap<DocId, Double> weightedQuery(Map<String, Double> termsWeights) {
        return this.vectorialModel.querySimilars(HashMap.ofAll(termsWeights)).toJavaMap();
    }

    @SuppressWarnings("deprecation")
    public Either<String, java.util.HashMap<DocId, Double>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.querySimilars(docId).map(HashMap::toJavaMap).toEither("DocId " + docId + " does not exist");
    }

    public HashSet<SentenceId> booleanQuery(ExpressionTreeNode root) {
        return booleanModel.query(root);
    }

    public Either<String, java.util.HashMap<DocId, Double>> weightedQuery(String query) {
        if(query.isEmpty())
            return Either.left("Unexpected empty query");
        
            var terms = Stream.of(
            query.strip().split(" ")
        );

        return
            Either.sequence(
                terms.map(Parsing::parseWeightedTerm)
            )
            .map(HashMap::ofEntries)
            .map(vectorialModel::querySimilars)
            .map(HashMap::toJavaMap)
            .mapLeft(
                errors -> String.join("\n", errors)
            );
    }
}
