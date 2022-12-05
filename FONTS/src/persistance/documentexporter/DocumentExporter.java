package src.persistance.documentexporter;

import java.util.ArrayList;

public interface DocumentExporter {
    public boolean exportToFile(String titleName, String authorName, ArrayList<String> content, String path);
}
