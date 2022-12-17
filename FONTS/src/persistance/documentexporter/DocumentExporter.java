package src.persistance.documentexporter;

import java.io.IOException;
import java.util.ArrayList;

public interface DocumentExporter {
    public void exportToFile(String titleName, String authorName, ArrayList<String> content, String path) throws IOException, Exception;
}
