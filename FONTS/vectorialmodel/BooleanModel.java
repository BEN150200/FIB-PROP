package vectorialmodel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BooleanModel<SentenceId> {

    Index<SentenceId> index;

    /**
     * 
     * @param term term to search
     * @return ids of sentences containing term
     */
    public Set<SentenceId> queryTerm(String term) {
        return index.invertedIndex.get(term).keySet();
    }
    
    // TODO
    /**
     * 
     * @param set collection of terms to search
     * @return ids of sentences containing all the terms in set
     */
    public Set<SentenceId> querySet(Collection<String> set) {
        return null;
    }

    // TODO
    /**
     * 
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    public Set<SentenceId> querySequence(List<String> sequence) {
        return null;
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public Set<SentenceId> all() {
        return index.directIndex.keySet();
    }
    
}
