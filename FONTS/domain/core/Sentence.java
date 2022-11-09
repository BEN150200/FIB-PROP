package domain.core;

import domain.controllers.SearchCtrl;
import domain.preprocessing.TokenFilter;
import domain.preprocessing.Tokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Sentence {
    private String sentence;
    //private ArrayList<String> words = new ArrayList<String>();
    private Set<Integer> documents = new HashSet<Integer>();
    private ArrayList<String> tokens = new ArrayList<>();
    private Integer id;

    
    
    public Sentence(String p) {
        sentence = p;
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
        String[] tok = Tokenizer.tokenize(sentence);
        for (String s : tok) {
            Optional<String> token = TokenFilter.filter(s);
            if (token.isPresent()) {
                tokens.add(token.get());
            }
        }
    }

    private void addToBooleanModel() {
        if (!tokens.isEmpty()) SearchCtrl.getInstance().addSentence(this.id, tokens);
    }
}