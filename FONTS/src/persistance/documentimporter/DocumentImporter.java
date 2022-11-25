package src.persistance.documentimporter;

import java.util.ArrayList;

import src.domain.core.DocumentInfo;

public interface DocumentImporter {
    /*public String getAuthor(String path);
    public String getTitle(String path);
    public ArrayList<String> getContent(String path);*/
    public DocumentInfo importFromFile(String path);
}
