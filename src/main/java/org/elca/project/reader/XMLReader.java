package org.elca.project.reader;

import org.elca.project.core.TemplateReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class XMLReader extends TemplateReader {
  @Override
  protected void printInfo() {
    System.out.println("Process by XML reader...");
  }

  @Override
  protected void getItemsFromSmallFile(File file, List<String> items) throws IOException {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    try {
      builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document doc = builder.parse(file);
      doc.getDocumentElement().normalize();

      NodeList companies = doc.getElementsByTagName("company");
      for (int i = 0; i < companies.getLength(); i++) {
        Node company = companies.item(i);

        Element element = (Element) company;

        String name = element.getAttribute(NAME_HEADER);
        String country = element.getAttribute(COUNTRY_HEADER);
        String isHeadQuater = element.getAttribute(IS_HEADQUATER_HEADER);

        if (country.equals(FILTER_COUNTRY) && isHeadQuater.equals("1")) {
          items.add(name);
        }
      }
    } catch (ParserConfigurationException e) {
      System.err.println("Can not read XML file in secure");
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void getItemsFromLargeFile(File file, List<String> items) throws IOException {
    FileInputStream fin = new FileInputStream(file);
    try {
      XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fin);
      while (xmlStreamReader.hasNext()) {
        int eventCode = xmlStreamReader.next();

        if ((eventCode == XMLStreamConstants.START_ELEMENT)
            && (xmlStreamReader.getLocalName().equalsIgnoreCase("companies"))) {
          while (xmlStreamReader.hasNext()) {
            eventCode = xmlStreamReader.next();

            if ((eventCode == XMLStreamConstants.END_ELEMENT)
                && (xmlStreamReader.getLocalName().equalsIgnoreCase("companies"))) {
              break;
            } else {
              if ((eventCode == XMLStreamConstants.START_ELEMENT)
                  && (xmlStreamReader.getLocalName().equalsIgnoreCase("company"))) {
                int attrCount = xmlStreamReader.getAttributeCount();

                for (int attrIndex = 0; attrIndex < attrCount; attrIndex++) {
                  String name = xmlStreamReader.getAttributeValue(null, NAME_HEADER);
                  String country = xmlStreamReader.getAttributeValue(null, COUNTRY_HEADER);
                  String isHeadQuater =
                      xmlStreamReader.getAttributeValue(null, IS_HEADQUATER_HEADER);

                  if (country.equals(FILTER_COUNTRY) && isHeadQuater.equals("1")) {
                    items.add(name);
                  }
                }
              }
            }
          }
        }
      }
    } catch (XMLStreamException e) {
      System.err.println("Cannot read XML stream");
      e.printStackTrace();
    }
  }
}