package src.domain.core;

import src.domain.controllers.AuthorCtrl;

public class Author extends Sentence{
    
    public Author(String a) {
        this.sentence = a;
        AuthorCtrl.getInstance().addAuthor(this);
    }

    @Override
    public void delete() {
        if (getNbDocuments() == 0) AuthorCtrl.getInstance().deleteAuthor(this.toString());
    }

    @Override
    public boolean deleteDocument(Integer docID) {
        super.deleteDocument(docID);
        return true;
    }

}
