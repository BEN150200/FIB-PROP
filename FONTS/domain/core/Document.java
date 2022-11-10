package domain.core;

import domain.DocumentInfo;
import domain.controllers.SearchCtrl;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Document {

    private Integer docID;
    private Author  author;
    private Title  title;
    private String filePath;
    //private String fileName;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    private ArrayList<Sentence> sentences = new ArrayList<Sentence>();
    
    //private enum Format {TXT, XML, PROP}
    //private Format format;

    /*
    public Document(int id) {
        docID = id;
    }

    public Document(int id, String t, String a, String p) {
        docID = id;
        author = new Author(a);
        title = new Title(t);
        filePath = p;
    }

    public Document(String titleName, String authorName, String path) {
        author = new Author(authorName);
        title = new Title(titleName);
    }
    */

    public Document(Title t, Author a) {
        creationDate = LocalDateTime.now();
        modificationDate = creationDate;
        title = t;
        author = a;
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public Author getAuthor() {
        return author;
    }

    public Title getTitle() {
        return title;
    }

    public String getPath() {
        return filePath;
    }

    public Integer getID() {
        return docID;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public DocumentInfo getInfo() {
        return new DocumentInfo(docID, title.toString(), author.toString(), creationDate, modificationDate);
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Setters
     */
    public boolean setPath(String path) {
        if (filePath == null) {
            filePath = path;
            return true;
        }
        return false;
    }

    public boolean setDocumentID(Integer id) {
        if (docID == null) {
            docID = id;
            return true;
        }
        return false;
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }


    public void updateModificationDate() {
        modificationDate = LocalDateTime.now();
    }

    public void deleteContent() {
        sentences.clear();
    }

    public void compute() {
        //HashMap<String,Integer> ocurrences = new HashMap<>();
        ArrayList<String> tokens = new ArrayList<>();
        for (Sentence sentence : sentences) {
            tokens.addAll(sentence.getTokens());
            /*
            for (String string : tokens) {
                if (ocurrences.containsKey(string)) {
                    ocurrences.put(string, ocurrences.get(string) + 1);
                }
                else ocurrences.put(string, 1);
            } 
            */
        }
        if (!tokens.isEmpty()) SearchCtrl.getInstance().addDocument(this.docID, tokens);
    }

    
}
