package src.domain.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import src.domain.controllers.SearchCtrl;
import src.domain.controllers.SentenceCtrl;
import src.domain.preprocessing.TokenFilter;
import src.domain.preprocessing.Tokenizer;

public class Sentence {
    protected String sentence;
    //private ArrayList<String> words = new ArrayList<String>();
    private Set<Integer> documents = new HashSet<Integer>();
    private ArrayList<String> tokens = new ArrayList<>();
    private Integer id;

    
    
    public Sentence(String p) {
        sentence = p;
        SentenceCtrl.getInstance().addSentence(this);
        compute();
    }

    public Sentence() {
    }

    public void delete() {
        SentenceCtrl.getInstance().deleteSentence(sentence);
        SearchCtrl.getInstance().removeSentence(id);
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    @Override
    public String toString() {
        return sentence;
    }

    public int id() {
        return this.id;
    }


    public Set<Integer> getAllDocsID() {
        return documents;
    }

    public boolean belongsToDoc(Integer docID) {
        return documents.contains(docID);
    }

    public int getNbDocuments(){
        return documents.size();
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    /**
     * Setters
    **/
    public boolean addDoc(Integer docID) {
        return documents.add(docID);
    }

    public boolean deleteDocument(Integer docID){
        if(belongsToDoc(docID)){
            documents.remove(docID);
            return true;
        }
        return false;
    }

    protected void setSentence(String s) {
        sentence = s;
    }

    public void setID(Integer sentID) {
        id = sentID;
    }

    

    /**
     * Compute
    **/
    public void compute() {
        filter();
        addToBooleanModel();
    }

    private void filter() {
        String[] splitedSentence = Tokenizer.tokenize(sentence);
        ArrayList<String> tok = new ArrayList<>();
        for (String string : splitedSentence) {
            tok.add(string);
        }
        /*
        for (String s : tok) {
            Optional<String> token = TokenFilter.filter(s);
            if (token.isPresent()) {
                tokens.add(token.get());
            }
        }
        */
        tokens = tok;

    }

    private void addToBooleanModel() {
        if (!tokens.isEmpty()) SearchCtrl.getInstance().addSentence(this.id, tokens);
    }
}
