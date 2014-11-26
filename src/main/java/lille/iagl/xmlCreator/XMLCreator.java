/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.xmlCreator;

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
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            FileOutputStream outputStream = new FileOutputStream(filename);
            this.writer = output.createXMLStreamWriter(outputStream, "UTF-8");
            this.writer.writeStartDocument("UTF-8", "1.0");
        } catch (FileNotFoundException | XMLStreamException ex) {
            System.err.println("Erreur lors de l'ouverture du fichier XML");
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void createElement(String name, String body) {
        try {
            this.writer.writeStartElement(name);
            this.writer.writeCharacters(body);
            this.writer.writeEndElement();
        } catch (XMLStreamException ex) {
            System.err.println("Erreur lors de l'ecriture dans le document XML");
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void startElement(String name) {
        try {        
            this.writer.writeStartElement(name);
        } catch (XMLStreamException ex) {
            System.err.println("Erreur lors de l'ecriture dans le document XML");
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void endElement() {
        try {
            this.writer.writeEndElement();
        } catch (XMLStreamException ex) {
            System.err.println("Erreur lors de l'ecriture dans le document XML");
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void close() {
        try {
            this.writer.flush();
            this.writer.close();
        } catch (XMLStreamException ex) {
            System.err.println("Erreur lors de la fermeture du document XML");
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
