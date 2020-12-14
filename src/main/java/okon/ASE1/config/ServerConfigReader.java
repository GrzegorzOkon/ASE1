package okon.ASE1.config;

import okon.ASE1.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.okon.ASE1.exception.ConfigurationException;
import java.util.ArrayList;

public class ServerConfigReader {
    public static ArrayList<Server> readParams(File file) {
        Element config = parseXml(file);
        ArrayList<Server> result = new ArrayList<>();
        NodeList servers = config.getElementsByTagName("server");
        if (servers != null && servers.getLength() > 0) {
            for (int i = 0; i < servers.getLength(); i++) {
                Node server = servers.item(i);
                if (server.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) server;
                    String databaseAlias = element.getElementsByTagName("db_alias").item(0).getTextContent();
                    String databaseIp = element.getElementsByTagName("db_ip").item(0).getTextContent();
                    Integer databasePort = element.getElementsByTagName("db_port").item(0).getTextContent().equals("") ? null :
                            Integer.valueOf(element.getElementsByTagName("db_port").item(0).getTextContent());
                    String databaseVendor = element.getElementsByTagName("db_vendor").item(0).getTextContent();
                    String authorizationInterface = element.getElementsByTagName("auth_interface").item(0).getTextContent();
                    result.add(new Server(databaseAlias, databaseIp, databasePort, databaseVendor, authorizationInterface));
                }
            }
        }
        return result;
    }

    private static Element parseXml(File file) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            return document.getDocumentElement();
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage());
        }
    }
}
