package src.persistance.documentexporter;
import src.persistance.documentexporter.DocumentExporter;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocumentExporterXML implements DocumentExporter{
    public void exportToFile(String titleName, String authorName, ArrayList<String> content, String path) throws IOException, Exception{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element root = doc.createElement("DOCCONFIGURATION");
        doc.appendChild(root);

        Element title = doc.createElement("titol");
        title.setTextContent(titleName);
        root.appendChild(title);

        Element author = doc.createElement("autor");
        author.setTextContent(authorName);
        root.appendChild(author);

        String text = "";
        for(String sentence: content){
            //text = text + sentence + "\n";
            text = text + sentence;
        }

        Element contentElement = doc.createElement("contingut");
        contentElement.setTextContent(text);
        root.appendChild(contentElement);

        File docFile = new File(path);
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
    }
}
