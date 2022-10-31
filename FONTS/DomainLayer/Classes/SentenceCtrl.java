package DomainLayer.Classes;

public class SentenceCtrl {

    private static SentenceCtrl instance = null;
    
    public static SentenceCtrl getInstance() {
        if (instance == null) {
            instance = new SentenceCtrl();
        }
        return instance;
    }

    
    
}
