package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.HashMap;

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
    **/

    /**
     * Getters
    **/
    public boolean existsTitle(String titleName) {
        return titles.containsKey(titleName);
    }

    public Title getTitle(String titleName) {
        return titles.get(titleName);
    }

    public ArrayList<Title> getAllTitles() {
        return new ArrayList<Title>(titles.values());
    }

    public ArrayList<String> getAllTitlesNames() {
        return new ArrayList<String>(titles.keySet());
    }

    public ArrayList<String> getTitlesNamesPrefix(String prefix) {
        ArrayList<String> titlesWithPrefix = new ArrayList<String>();
        for (String title : titles.keySet()) {
            if (title.startsWith(prefix)) {
                titlesWithPrefix.add(title);
            }
        }
        return titlesWithPrefix;
    }

    public ArrayList<Title> getTitlesPrefix(String prefix) {
        ArrayList<Title> titlesWithPrefix = new ArrayList<Title>();
        for (String title : titles.keySet()) {
            if (title.startsWith(prefix)) {
                titlesWithPrefix.add(titles.get(title));
            }
        }
        return titlesWithPrefix;
    }

    /**
     * Setters
    **/

    public boolean addTitle(Title t) {
        if (!existsTitle(t.getTitleName())){
            titles.put(t.getTitleName(),t);
            return true;
        }
        return false;
    }

    public boolean deleteTitle(String titleName) {
        if (existsTitle(titleName)){
            titles.remove(titleName);
            return true;
        }
        return false;
    }
}
