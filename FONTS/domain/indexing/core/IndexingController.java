package domain.indexing.core;

import java.util.Map;

import domain.indexing.booleanmodel.BooleanModel;
import domain.indexing.booleanmodel.ExpressionTree;
import domain.indexing.vectorial.VectorialModel;

import java.util.HashMap;

public class IndexingController<DocId, SentenceId> {
    private VectorialModel<DocId> vectorialModel;
    private BooleanModel<SentenceId> booleanModel;

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

    public HashMap<DocId, Double> weightedQuery(Map<String, Double> termsWeights) {
        return this.vectorialModel.querySimilars(termsWeights);
    }

    public HashMap<DocId, Double> querySimilarDocuments(DocId docId) {
        return this.vectorialModel.querySimilars(docId);
    }

    // TODO
    public Iterable<SentenceId> booleanQuery(ExpressionTree expressionTree) {
        return null;
    }
}
