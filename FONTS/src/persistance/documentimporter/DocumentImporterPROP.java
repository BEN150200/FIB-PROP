package src.persistance.documentimporter;

import src.persistance.documentimporter.DocumentImporter;
import src.domain.core.DocumentInfo;
import src.enums.Format;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class DocumentImporterPROP implements DocumentImporter{
    public DocumentInfo importFromFile(String path) {
        DocumentInfo docInfo = null;
        try{
            File myObj = new File(path);
            Scanner scanner = new Scanner(myObj);

            scanner.nextLine();
            String title = scanner.nextLine();

            scanner.nextLine();
            String author = scanner.nextLine();
            
            scanner.nextLine();
            ArrayList<String> content = new ArrayList<String>();
            while(scanner.hasNextLine()){
                content.add(scanner.nextLine());
            }
            scanner.close();
            docInfo = new DocumentInfo(0, title, author, null, null, content, path, Format.PROP);
        } catch (FileNotFoundException e) {}
        return docInfo;
    }
}
