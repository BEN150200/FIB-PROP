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

public class DocumentImporterTXT implements DocumentImporter{
    /*public String getAuthor(String path){
        String author = "";
        try{
            File myObj = new File(path);
            Scanner scanner = new Scanner(myObj);
            author = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {}
        return author;
    }

    public String getTitle(String path){
        String title = "";
        try{
            File myObj = new File(path);
            Scanner scanner = new Scanner(myObj);
            scanner.nextLine();
            title = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {}
        return title;
    }

    public ArrayList<String> getContent(String path){
        ArrayList<String> content = new ArrayList<>();
        try{
            File myObj = new File(path);
            Scanner scanner = new Scanner(myObj);
            scanner.nextLine();
            scanner.nextLine();
            while(scanner.hasNextLine()){
                content.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {}
        return content;
    }
    */
    public DocumentInfo importFromFile(String path){
        DocumentInfo docInfo = null;
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String author = scanner.nextLine();
            String title = scanner.nextLine();
            ArrayList<String> content = new ArrayList<String>();
            while(scanner.hasNextLine()){
                content.add(scanner.nextLine());
            }
            scanner.close();
            FileTime fileCreation = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            FileTime fileModification = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");
            docInfo = new DocumentInfo(0, title, author, LocalDateTime.ofInstant(fileCreation.toInstant(), ZoneId.systemDefault()), LocalDateTime.ofInstant(fileModification.toInstant(), ZoneId.systemDefault()), content, path, Format.TXT, file.getName());
        } catch (IOException ignored) {}
        return docInfo;
    }
}
