package src.persistance.documentimporter;

import java.io.FileNotFoundException;
import java.io.IOException;

import src.domain.core.DocumentInfo;

public interface DocumentImporter {
    /*public String getAuthor(String path);
    public String getTitle(String path);
    public ArrayList<String> getContent(String path);*/
    public DocumentInfo importFromFile(String path) throws FileNotFoundException, IOException, Exception;
}
