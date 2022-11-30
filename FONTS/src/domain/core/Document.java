package src.domain.core;

import src.domain.controllers.DocumentCtrl;
import src.domain.controllers.SearchCtrl;
import src.enums.Format;

import java.time.*;
import java.util.ArrayList;

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
    private Format format;

    public Document(Title t, Author a) {
        creationDate = LocalDateTime.now();
        modificationDate = creationDate;
        title = t;
        author = a;
        setDocumentID(DocumentCtrl.getInstance().addDocument(this));
        title.addDoc(docID);
        author.addDoc(docID);
    }

    public Document(Title t, Author a, LocalDateTime creationDate, LocalDateTime modificationDate, String filePath, Format format) {
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.filePath = filePath;
        this.format = format;
        title = t;
        author = a;
        setDocumentID(DocumentCtrl.getInstance().addDocument(this));
        title.addDoc(docID);
        author.addDoc(docID);
    }

    public void delete() {
        for (Sentence sentence : sentences) {
            sentence.deleteDocument(docID);
        }
        sentences.clear();
        title.deleteDocument(docID);
        author.deleteDocument(docID);
        DocumentCtrl.getInstance().deleteDocument(title.toString(), author.toString());
        SearchCtrl.getInstance().removeDocument(docID);
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
    
    public Format getFormat(){
        return format;
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
        ArrayList<String> content = new ArrayList<String>();
        for(Sentence sentence: sentences){
            content.add(sentence.toString());
        }
        return new DocumentInfo(docID, title.toString(), author.toString(), creationDate, modificationDate, content, filePath, format);
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Setters
     */
    
    public void setPath(String path) {
        filePath = path;
    }
    
    public void setFormat(Format fileFormat){
        format = fileFormat;
    }

    private boolean setDocumentID(Integer id) {
        if (docID == null) {
            docID = id;
            return true;
        }
        return false;
    }

    public void updateDocumentContent(ArrayList<Sentence> newContent) {
        
        for (Sentence sentence : sentences) {
            if (!newContent.contains(sentence)) {
                sentence.deleteDocument(docID);
            }
        }
        for (Sentence sentence : newContent) {
            if (!sentences.contains(sentence)) {
                sentence.addDoc(docID);
            }
        }
        sentences = newContent;
        compute();
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
        sentence.addDoc(docID);
    }

    public void setCreationDate(LocalDateTime date){
        creationDate = date;
    }

    public void setModificationDate(LocalDateTime date){
        modificationDate = date;
    }

    public void updateModificationDate() {
        modificationDate = LocalDateTime.now();
    }

    public void deleteContent() {
        for (Sentence sentence : sentences) {
            sentence.deleteDocument(docID);
        }
        sentences.clear();
    }

    public void compute() {
        //HashMap<String,Integer> ocurrences = new HashMap<>();
        ArrayList<String> tokens = new ArrayList<>();
        for (Sentence sentence : sentences) {
            tokens.addAll(sentence.getTokens());
        }
        if (!tokens.isEmpty()) SearchCtrl.getInstance().addDocument(this.docID, tokens);
    }

    
}
