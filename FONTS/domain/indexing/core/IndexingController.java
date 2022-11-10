package domain.indexing.core;

import java.util.Map;

import domain.core.ExpressionTreeNode;
import domain.indexing.booleanmodel.BooleanModel;
import domain.indexing.booleanmodel.ExpressionTree;
import domain.indexing.vectorial.VectorialModel;
import helpers.Maths;
import helpers.Parsing;
import helpers.Strings;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Stream;
import io.vavr.control.Either;

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

    public java.util.HashMap<DocId, Double> weightedQuery(Map<String, Double> termsWeights) {
        return this.vectorialModel.querySimilars(HashMap.ofAll(termsWeights)).toJavaMap();
    }

    @SuppressWarnings("deprecation")
    public Either<String, java.util.HashMap<DocId, Double>> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.querySimilars(docId).map(HashMap::toJavaMap).toEither("DocId " + docId + " does not exist");
    }


    private HashSet<SentenceId> solveQuery(ExpressionTreeNode root) {
        var value = root.getValue();
        switch (value.charAt(0)) {
            case '&':
                return solveQuery(root.getLeft()).intersect(solveQuery(root.getRight()));
            case '|':
                break;
        }
    }

    /* 
    public HashSet<SentenceId> booleanQuery(ExpressionTreeNode expressionTree) {
        String value=expressionTree.getValue();
        if(value.charAt(0)=='&') return intersection(booleanQuery(expressionTree.getLeft()),booleanQuery(expressionTree.getRight()));
        else if(value.charAt(0)=='|') return union(booleanQuery(expressionTree.getLeft()),booleanQuery(expressionTree.getRight()));
        else if(value.charAt(0)=='!') return negate(booleanQuery(expressionTree.getRight()));
        //sino vol dir que és node final
        //String aux = eraseSpacesValue();
        if(value.charAt(0)=='{'){//llista de paraules
            String aux=value.substring(1,value.length()-1);//treiem els {}
            String[] list=aux.split(" ");
            //Set<String> SetList = new HashSet<String>();
            return new HashSet<SentenceId> (booleanModel.querySet(Arrays.asList(list)));

        }
        else if(value.charAt(0)=='"'){//cadena de paraules
            String aux=value.substring(1,value.length()-1);//treiem els ""
            String[] list=aux.split(" ");

            return new HashSet<SentenceId> (booleanModel.querySequence(Arrays.asList(list))); //ha de tornar totes les frases que tenen la frase.................................

        }
        else return new HashSet<SentenceId> (booleanModel.queryTerm(value));//.........................................................
    }
    */
    
    // TODO
    public Either<String, Iterable<SentenceId>> booleanQuery(ExpressionTreeNode root) {
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
            .map(vectorialModel::querySimilars)
            .mapLeft(Strings::joinEndLine);
    }
}
