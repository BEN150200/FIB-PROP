package DataLayer.Classes;

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import DomainLayer.temp.Pair;

import DomainLayer.Classes.Word;
import DomainLayer.Classes.Author;
import DomainLayer.Classes.Title;
import DomainLayer.Classes.Document;

import DomainLayer.Interfaces.WordController;
import DomainLayer.Interfaces.AuthorController;
import DomainLayer.Interfaces.TitleController;
import DomainLayer.Interfaces.DocumentController;

// Singleton class
public class DataBase implements WordController, AuthorController, TitleController, DocumentController{
    private static DataBase instance = null;

    private HashMap<String, Word> words = new HashMap<String, Word>();
    private HashMap<String, Author> authors = new HashMap<String, Author>();
    private HashMap<String, Title> titles = new HashMap<String, Title>();
    private HashMap<Pair<String, String>, Document> documents = new HashMap<Pair<String, String>, Document>();

    //private HashMap<> booleanExpressions = new HashMap<>();


    public static DataBase getInstance(){
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    // -----------------  Words  ----------------------------------------------------

    public Boolean existsWord(String word){
        return words.containsKey(word);
    }

    public Word getWord(String word){
        return words.get(word);
    }

    public void addWord(Word word){
        words.put(word.getWord(), word);
    }

    public void deleteWord(String word){
        words.remove(word);
    }

    public Set<Word> getAllWords(){
        return new HashSet<Word>(words.values());
    }

    // ------------------------------------------------------------------------------

    // -----------------  Authors  --------------------------------------------------

    public Boolean existsAuthor(String authorName){
        return authors.containsKey(authorName);
    }

    public Author getAuthor(String authorName){
        return authors.get(authorName);
    }

    public void addAuthor(Author author){
        authors.put(author.getAuthor(), author);
    }

    public void deleteAuthor(String authorName){
        authors.remove(authorName);
    }

    public Set<Author> getAllAuthors(){
        return new HashSet<Author>(authors.values());
    }

    // ------------------------------------------------------------------------------

    // -----------------  Titles  ---------------------------------------------------

    public Boolean existsTitle(String titleName){
        return titles.containsKey(titleName);
    }

    public Title getTitle(String titleName){
        return titles.get(titleName);
    }

    public void addTitle(Title title){
        titles.put(title.getTitle(), title);
    }

    public void deleteTitle(String titleName){
        titles.remove(titleName);
    }

    public Set<Title> getAllTitles(){
        return new HashSet<Title>(titles.values());
    }

    // ------------------------------------------------------------------------------

    // -----------------  Documents  ------------------------------------------------

    public Boolean existsDocument(String titleName, String authorName){
        Pair<String, String> p = new Pair<String, String>(titleName, authorName);
        return documents.containsKey(p);
    }

    public Document getDocument(String titleName, String authorName){
        Pair<String, String> p = new Pair<String, String>(titleName, authorName);
        return documents.get(p);
    }

    public void addDocument(Document document){
        Pair<String, String> p = new Pair<String, String>(document.getTitle(), document.getAuthor());
        documents.put(p, document);
    }

    public void deleteDocument(String titleName, String authorName){
        Pair<String, String> p = new Pair<String, String>(titleName, authorName);
        documents.remove(p);
    }

    public Set<Document> getAllDocuments(){
        return new HashSet<Document>(documents.values());
    }

    // ------------------------------------------------------------------------------

    // -----------------  Boolean Expressions  --------------------------------------


    
    // ------------------------------------------------------------------------------
}
