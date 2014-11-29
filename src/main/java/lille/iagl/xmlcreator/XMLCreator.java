/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.xmlcreator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author francois
 */
public class XMLCreator {
    private XMLStreamWriter writer;

    public XMLCreator(String filename) {
        try {
            XMLOutputFactory xmlOF = XMLOutputFactory.newFactory();
            FileOutputStream outputStream = new FileOutputStream(filename);
            this.writer = xmlOF.createXMLStreamWriter(outputStream, "UTF-8");
            this.writer.writeStartDocument("UTF-8", "1.0");
        } catch (FileNotFoundException | XMLStreamException ex) { 
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void startElement(String name) {
        try {
            writer.writeStartElement(name);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void endElement() {
        try {       
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void createElement(String name, String body) {
        try {
            writer.writeStartElement(name);
            writer.writeCharacters(body);       
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            this.writer.flush();
            this.writer.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}