package src.domain.controllers;

import src.domain.core.Title;

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
    /**
     * Fucntion to check if there is a Title identified by titleName
     * @param titleName Name of the title that whant to know if exists
     * @return Return ture if the title identified by titleName, else return false
     */
    public boolean existsTitle(String titleName) {
        return titles.containsKey(titleName);
    }

    /**
     * Fucntion to get the title identified by titleName
     * @param titleName
     * @return Returns the instance of the Title identified by titleName, if it don't exists, return NULL
     */
    public Title getTitle(String titleName) {
        return titles.get(titleName);
    }

    /**
     * Function to get all the titles in the System
     * @return Return an ArrayList of Titles that contains all the Titles in the System
     */
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
        if (!existsTitle(t.toString())){
            titles.put(t.toString(),t);
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

    public void clear(){
        titles.clear();
    }
}
