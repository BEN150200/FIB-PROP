package domain.controllers;

import domain.indexing.core.IndexingController;

public class SearchCtrl {

    private static SearchCtrl instance = null;

    private IndexingController indexingCtrl = new IndexingController<>();

    /**
     * Constructor
    **/
    public SearchCtrl() {}

    public static SearchCtrl getInstance() {
        if (instance == null) {
            instance = new SearchCtrl();
        }
        return instance;
    }

    
    
}
