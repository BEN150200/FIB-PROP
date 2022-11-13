package src.domain.core;

import src.domain.controllers.AuthorCtrl;

public class Author extends Sentence{
    //Set<Integer> documents = new HashSet<Integer>();
    
    public Author(String a) {
        this.sentence = a;
        AuthorCtrl.getInstance().addAuthor(this);
    }

    public void delete() {
        AuthorCtrl.getInstance().deleteAuthor(this.toString());
    }

}
