package src.domain.core;

import src.domain.controllers.TitleCtrl;

public class Title extends Sentence {
    
    public Title(String t) {
        this.sentence = t;
        TitleCtrl.getInstance().addTitle(this);
    }

    public void delete() {
        TitleCtrl.getInstance().deleteTitle(this.toString());
    }
}
