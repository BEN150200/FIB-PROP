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

    private static final String DATAPATH = "\\Fonts\\data\\booleanExpressionsData.xml";

    public boolean saveData(ArrayList<String> expressionNames, ArrayList<String> expressions){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try{
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

    public ArrayList<String> loadExpressionNames(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> names = new ArrayList<String>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("BooleanExpression");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                names.add(element.getAttribute("name"));
            }
        } catch(Exception e ){}
        return names;
    }

    public ArrayList<String> loadExpressions(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList<String> expressions = new ArrayList<String>();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(DATAPATH));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("BooleanExpression");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                expressions.add(element.getAttribute("expression"));
            }
        } catch(Exception e ){}
        return expressions;
    }
}
