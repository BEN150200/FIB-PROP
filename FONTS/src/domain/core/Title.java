package src.domain.core;

import src.domain.controllers.TitleCtrl;

public class Title extends Sentence {
    
    public Title(String t) {
        this.sentence = t;
        TitleCtrl.getInstance().addTitle(this);
    }

    @Override
    public void delete() {
        if (getNbDocuments() == 0) TitleCtrl.getInstance().deleteTitle(this.toString());
    }

    @Override
    public boolean deleteDocument(Integer docID) {
        super.deleteDocument(docID);
        return true;
    }
}
