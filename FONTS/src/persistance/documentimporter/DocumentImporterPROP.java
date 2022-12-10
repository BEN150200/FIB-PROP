package src.persistance.documentimporter;

import src.persistance.documentimporter.DocumentImporter;
import src.domain.core.DocumentInfo;
import src.enums.Format;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;
import java.util.ArrayList;

public class DocumentImporterPROP implements DocumentImporter{
    public DocumentInfo importFromFile(String path) {
        DocumentInfo docInfo = null;
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);

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
            FileTime fileCreation = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            FileTime fileModification = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");

            docInfo = new DocumentInfo(0, title, author, LocalDateTime.ofInstant(fileCreation.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(fileModification.toInstant(), ZoneId.systemDefault()), content, path, Format.PROP, file.getName());
        } catch (IOException ignored) {}
        return docInfo;
    }
}
