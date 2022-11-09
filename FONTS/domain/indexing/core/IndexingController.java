package domain.indexing.core;

import java.util.Map;

import domain.core.Sentence;
import domain.indexing.booleanmodel.BooleanModel;
import domain.indexing.booleanmodel.ExpressionTree;
import domain.indexing.vectorial.VectorialModel;
import helpers.Maps;
import helpers.Maths;
import helpers.Parsing;
import helpers.Strings;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class IndexingController<DocId, SentenceId> {
    private VectorialModel<DocId> vectorialModel;
    private BooleanModel<SentenceId> booleanModel;
    
    // TODO: return boolean of wheter inserted new or updated,
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
    public Either<String, HashMap<DocId, Double>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.querySimilars(docId).toEither("DocId " + docId + " does not exist");
    }
    
    // TODO
    public Either<String, Iterable<SentenceId>> booleanQuery(ExpressionTree expressionTree) {
        return Either.left("Not yet implemented");
    }
    
    public Either<String, HashMap<DocId, Double>> weightedQuery(String query) {
        var queryTerms = query.strip().split(" ");

        if(query.isEmpty())
            return Either.left("Unexpected empty query");

        return Either.sequence(Stream.of(queryTerms).map(Parsing::parseWeightedTerm))
            .map(termsWeights ->
                termsWeights.foldLeft(
                    io.vavr.collection.HashMap.<String, Double>empty(),
                    (queryMap, termWeight) -> queryMap.merge(
                        io.vavr.collection.HashMap.of(termWeight._1, termWeight._2),
                        (a, b) -> a + b
                    )
                )
            )
            .map(Maths::normalized)
            .map(io.vavr.collection.HashMap::toJavaMap)
            .map(vectorialModel::querySimilars)
            .mapLeft(Strings::joinEndLine);
    }
}
