package src.persistance.documentexporter;

import java.util.ArrayList;

@FunctionalInterface
public interface DocumentExporter {
    public boolean exportToFile(String titleName, String authorName, ArrayList<String> content, String path);
}
