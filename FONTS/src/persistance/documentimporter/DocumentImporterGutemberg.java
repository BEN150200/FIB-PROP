package src.persistance.documentimporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Scanner;

import src.domain.core.DocumentInfo;
import src.enums.Format;

public class DocumentImporterGutemberg {
    public static DocumentInfo importFile(String path) {
        DocumentInfo docInfo = null;
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String title = "untitled";
            String author = "na";

            ArrayList<String> content = new ArrayList<String>();
            while(scanner.hasNextLine()){
                var line = scanner.nextLine();
                if(line.startsWith("Title:"))
                    title = line.substring("Title:".length()+1);
                if(line.startsWith("Author:"))
                    author = line.substring("Author:".length()+1);
                else content.add(line + "\n");
            }
            scanner.close();
            FileTime fileCreation = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            FileTime fileModification = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");
            docInfo = new DocumentInfo(0, title, author, LocalDateTime.ofInstant(fileCreation.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(fileModification.toInstant(), ZoneId.systemDefault()), content, path, Format.TXT, file.getName());
        } catch (IOException ignored) {}
        return docInfo;
    }
}