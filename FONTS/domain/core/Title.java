package domain.core;

import domain.controllers.TitleCtrl;

public class Title extends Sentence {
    
    public Title(String t) {
        this.sentence = t;
        TitleCtrl.getInstance().addTitle(this);
    }

    public void delete() {
        TitleCtrl.getInstance().deleteTitle(this.toString());
    }
}
