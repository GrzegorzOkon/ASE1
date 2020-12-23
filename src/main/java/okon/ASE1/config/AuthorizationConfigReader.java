package okon.ASE1.config;

import okon.ASE1.Authorization;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import okon.ASE1.exception.ConfigurationException;
import java.util.ArrayList;

import static okon.ASE1.security.HexDecryptor.convert;

public class AuthorizationConfigReader {
    public static ArrayList<Authorization> readParams(File file) {
        Element config = parseXml(file);
        ArrayList<Authorization> result = new ArrayList<>();
        NodeList authorizations = config.getElementsByTagName("authorization");
        if (authorizations != null && authorizations.getLength() > 0) {
            for (int i = 0; i < authorizations.getLength(); i++) {
                Node authorization = authorizations.item(i);
                if (authorization.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) authorization;
                    String authorizationInterface = element.getElementsByTagName("auth_interface").item(0).getTextContent();
                    String user = element.getElementsByTagName("user").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    result.add(new Authorization(authorizationInterface, convert(user), convert(password)));
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
