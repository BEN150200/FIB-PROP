package src.persistance.datamanagement;

import java.util.ArrayList;

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

public class BEDataManager {

    private static final String DATAPATH = "./data/booleanExpressionsData.xml";

    public void saveData(ArrayList<String> expressionNames, ArrayList<String> expressions) throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Expressions");
        doc.appendChild(rootElement);
        for(int i = 0; i < expressionNames.size(); i++){
            Element expression = doc.createElement("BooleanExpression");
            rootElement.appendChild(expression);
            expression.setAttribute("name", expressionNames.get(i));
            expression.setAttribute("expression", expressions.get(i));
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
    }

    public ArrayList<String> loadExpressionNames() throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> names = new ArrayList<String>();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(DATAPATH));
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("BooleanExpression");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            Element element = (Element) node;
            names.add(element.getAttribute("name"));
        }
        return names;
    }

    public ArrayList<String> loadExpressions() throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> expressions = new ArrayList<String>();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(DATAPATH));
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("BooleanExpression");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            Element element = (Element) node;
            expressions.add(element.getAttribute("expression"));
        }
        return expressions;
    }

    // Cleans boolean expressions data by overwriting the current content with nothing.
    public void clearData() throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException{
        saveData(new ArrayList<String>(), new ArrayList<String>());
    }
}
