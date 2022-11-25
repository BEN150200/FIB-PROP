package src.persistance.documentimporter;

import src.persistance.documentimporter.DocumentImporter;
import src.domain.core.DocumentInfo;
import src.enums.Format;

import java.io.File;
import java.io.FileNotFoundException;
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
            File myObj = new File(path);
            Scanner scanner = new Scanner(myObj);
            String author = scanner.nextLine();
            String title = scanner.nextLine();
            ArrayList<String> content = new ArrayList<String>();
            while(scanner.hasNextLine()){
                content.add(scanner.nextLine());
            }
            scanner.close();
            docInfo = new DocumentInfo(0, title, author, null, null, content, path, Format.TXT);
        } catch (FileNotFoundException e) {}
        return docInfo;
    }
}
