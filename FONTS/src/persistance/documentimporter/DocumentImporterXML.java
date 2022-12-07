package src.persistance.documentimporter;

import src.persistance.documentimporter.DocumentImporter;
import src.enums.Format;
import src.domain.core.DocumentInfo;

import java.io.File; 
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Document;   
import org.w3c.dom.NodeList;  
import java.util.ArrayList;

import java.util.Collections;

public class DocumentImporterXML implements DocumentImporter{
    /*
    public String getAuthor(String path){
        String author = "";
        try{
            File file = new File(path);  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            NodeList nodeList = doc.getElementsByTagName("autor");  
            for (int i = 0; i < nodeList.getLength(); i++){
                author = nodeList.item(i).getTextContent(); 
            }  
        } catch(Exception e){}
        return author;
    }

    public String getTitle(String path){
        String title = "";
        try{
            File file = new File(path);  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            NodeList nodeList = doc.getElementsByTagName("títol");  
            for (int i = 0; i < nodeList.getLength(); i++){
                title = nodeList.item(i).getTextContent(); 
            }  
        } catch(Exception e){}
        return title;
    }

    public ArrayList<String> getContent(String path){
        ArrayList<String> content = new ArrayList<String>();
        try{
            File file = new File(path);  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            NodeList nodeList = doc.getElementsByTagName("contingut");  
            String text = "";
            for (int i = 0; i < nodeList.getLength(); i++){
                text = nodeList.item(i).getTextContent(); 
            }  
            String[] sentences = text.split("\\r?\\n");
            Collections.addAll(content, sentences);
        } catch(Exception e){}
        return content;
    }
    */

    public DocumentInfo importFromFile(String path){
        DocumentInfo docInfo = null;
        try{
            File file = new File(path);  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse(file);  
            doc.getDocumentElement().normalize();  
            String title = "";
            NodeList nodeList = doc.getElementsByTagName("títol");  
            for (int i = 0; i < nodeList.getLength(); i++){
                title = nodeList.item(i).getTextContent(); 
            }
            String author = "";
            nodeList = doc.getElementsByTagName("autor");  
            for (int i = 0; i < nodeList.getLength(); i++){
                author = nodeList.item(i).getTextContent(); 
            }  
            nodeList = doc.getElementsByTagName("contingut");  
            ArrayList<String> content = new ArrayList<String>();
            String text = "";
            for (int i = 0; i < nodeList.getLength(); i++){
                text = nodeList.item(i).getTextContent(); 
            }  
            String[] sentences = text.split("\\r?\\n");
            Collections.addAll(content, sentences);
            docInfo = new DocumentInfo(0, title, author, null, null, content, path, Format.XML, file.getName());
        } catch (Exception e) {}
        return docInfo;
    }
}
