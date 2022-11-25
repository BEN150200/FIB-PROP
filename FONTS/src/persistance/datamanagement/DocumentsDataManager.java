package src.persistance.datamanagement;

import java.util.ArrayList;
import java.time.LocalDateTime;

import src.domain.core.DocumentInfo;
import src.enums.Format;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;

public class DocumentsDataManager {
    private static final String DATAPATH = "\\Fonts\\data\\DocumentsData.xml";

    public boolean saveData(ArrayList<DocumentInfo> docsInfo){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
            for(DocumentInfo documentInfo: docsInfo){
                Element documElement = doc.createElement("Document");
                rootElement.appendChild(documElement);
                documElement.setAttribute("title", documentInfo.getTitle());
                documElement.setAttribute("author", documentInfo.getAuthor());
                String content = "";
                for(String sentence: documentInfo.getContent()){
                    content = content + sentence + "\n";
                }
                documElement.setAttribute("content", content);
                documElement.setAttribute("creationDate", documentInfo.getCreationDate().toString());
                documElement.setAttribute("modificationDate", documentInfo.getModificationDate().toString());
                documElement.setAttribute("filePath", documentInfo.getPath());
                documElement.setAttribute("fileFormat", documentInfo.getFormat().toString());
            }
            FileOutputStream output = new FileOutputStream(DATAPATH);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public ArrayList<DocumentInfo> loadData(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<DocumentInfo> docsInfos = new ArrayList<DocumentInfo>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("Document");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                String title = element.getAttribute("title");
                String author = element.getAttribute("author");
                LocalDateTime creationDate = LocalDateTime.parse(element.getAttribute("creationDate"));
                LocalDateTime modificationDate = LocalDateTime.parse(element.getAttribute("modificationDate"));
                ArrayList<String> content = new ArrayList<String>();
                String[] sentences = element.getAttribute("content").split("\\r?\\n");
                Collections.addAll(content, sentences);
                String path = element.getAttribute("filePath");
                Format format = Format.valueOf(element.getAttribute("fileFormat")); 
                DocumentInfo docInfoTemp = new DocumentInfo(0, title, author, creationDate, modificationDate, content, path, format);
                docsInfos.add(docInfoTemp);
            }
        } catch(Exception e){}
        return docsInfos;
    }


    /*public boolean saveData(ArrayList<String> titles, ArrayList<String> authors, ArrayList<ArrayList<String>> contents){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
            for(int i = 0; i < titles.size(); i++){
                Element documElement = doc.createElement("Document");
                rootElement.appendChild(documElement);
                documElement.setAttribute("title", titles.get(i));
                documElement.setAttribute("author", authors.get(i));
                String content = "";
                for(String sentence: contents.get(i)){
                    content = content + sentence + "\n";
                }
                documElement.setAttribute("content", content);
            }
            FileOutputStream output = new FileOutputStream(DATAPATH);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public ArrayList<String> loadTitles(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> titles = new ArrayList<String>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("Document");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                titles.add(element.getAttribute("title"));
            }
        } catch(Exception e){}
        return titles;
    }

    public ArrayList<String> loadAuthors(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> authors = new ArrayList<String>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("Document");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                authors.add(element.getAttribute("author"));
            }
        } catch(Exception e){}
        return authors;
    }

    public ArrayList<ArrayList<String>> loadContents(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("Document");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                ArrayList<String> content = new ArrayList<String>();
                String[] sentences = element.getAttribute("content").split("\\r?\\n");
                Collections.addAll(content, sentences);
                contents.add(content);
            }
        } catch(Exception e){}
        return contents;
    }
    */
}
