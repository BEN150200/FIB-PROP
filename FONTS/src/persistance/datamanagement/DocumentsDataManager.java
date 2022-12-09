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

    private static final String DATAPATH = "./data/DocumentsData.xml";

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
                    //content = content + sentence + "\n";
                    content += sentence;
                }
                documElement.setAttribute("content", content);
                documElement.setAttribute("creationDate", documentInfo.getCreationDate().toString());
                documElement.setAttribute("modificationDate", documentInfo.getModificationDate().toString());
                documElement.setAttribute("filePath", documentInfo.getPath());
                documElement.setAttribute("fileFormat", documentInfo.getFormat().toString());
                documElement.setAttribute("fileName", documentInfo.getFileName());
            }
            File docFile = new File(DATAPATH);
            docFile.createNewFile();
            // Creates a file output stream to write to the file represented by the specified File.
            // Overwrites the file content.
            FileOutputStream output = new FileOutputStream(docFile, false);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
            output.close();
            return true;
        } catch(Exception e){
            e.printStackTrace();
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
                String fileName = element.getAttribute("fileName");
                DocumentInfo docInfoTemp = new DocumentInfo(0, title, author, creationDate, modificationDate, content, path, format, fileName);
                docsInfos.add(docInfoTemp);
            }
        } catch(Exception e){}
        return docsInfos;
    }

    // Cleans documents data by overwriting the current content with nothing.
    public boolean clearData(){
        return saveData(new ArrayList<DocumentInfo>());
    }
}
