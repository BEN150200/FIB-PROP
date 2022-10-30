package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TitleCtrl {
    private static TitleCtrl instance = null;
    HashMap<String, Title> titles = new HashMap<String, Title>();
    
    public static TitleCtrl getInstance() {
        if (instance == null) {
            instance = new TitleCtrl();
        }
        return instance;
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public boolean existsTitle(String titleName) {
        return titles.containsKey(titleName);
    }

    public Title getTitle(String titleName) {
        return titles.get(titleName);
    }

    public Set<Title> getAllTitles() {
        return new HashSet(titles.values());
    }

    public Set<String> getAllTitlesNames() {
        return titles.keySet();
    }

    /**
     * Setters
     */
    public boolean addTitle(String titleName) {
        boolean exists = titles.containsKey(titleName);
        if (!exists) {
            Title title = new Title(titleName);
            titles.put(titleName, title);
        }
        return exists;
    }

    public boolean deleteTitle(String titleName) {
        return false;
    }
}
