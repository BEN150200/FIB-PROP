package src.persistance.documentexporter;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import src.persistance.documentexporter.DocumentExporter;

public class DocumentExporterPROP implements DocumentExporter{
    public boolean exportToFile(String titleName, String authorName, ArrayList<String> content, String path) {
        try{
            String titleHeader = "[Title]";
            String authorHeader = "[Author]";
            String contentHeader = "[Content]";
            File doc = new File(path);
            doc.createNewFile();
            FileWriter myWriter = new FileWriter(doc, false);
            String text = titleHeader + "\n" + titleName + "\n" + authorHeader + "\n" + authorName + "\n" + contentHeader + "\n";
            for(String sentence: content){
                text = text + sentence;
            }
            myWriter.write(text);
            myWriter.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}
