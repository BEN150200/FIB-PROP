package src.persistance.documentexporter;
import src.persistance.documentexporter.DocumentExporter;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocumentExporterXML implements DocumentExporter{
    public boolean exportToFile(String titleName, String authorName, ArrayList<String> content, String path){
        try{
            DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element root = doc.createElement("DOCCONFIGURATION");
            doc.appendChild(root);

            Element title = doc.createElement("títol");
            title.setTextContent(titleName);
            root.appendChild(title);

            Element author = doc.createElement("autor");
            author.setTextContent(authorName);
            root.appendChild(author);

            String text = "";
            for(String sentence: content){
                text = text + sentence + "\n";
            }

            Element contentElement = doc.createElement("contingut");
            contentElement.setTextContent(text);
            root.appendChild(contentElement);


            FileOutputStream output = new FileOutputStream(path);
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
}
