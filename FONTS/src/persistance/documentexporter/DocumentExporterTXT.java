package src.persistance.documentexporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import src.persistance.documentexporter.DocumentExporter;
import java.util.ArrayList;

public class DocumentExporterTXT implements DocumentExporter{
    public void exportToFile(String titleName, String authorName, ArrayList<String> content, String path) throws IOException, Exception{
        File doc = new File(path);
        doc.createNewFile();
        FileWriter myWriter = new FileWriter(doc, false);
        String text = authorName + "\n" + titleName + "\n";
        for(String sentence: content){
            //text = text + sentence + "\n";
            text = text + sentence;
        }
        myWriter.write(text);
        myWriter.close();
    }
}
