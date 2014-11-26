/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.stackoverflowparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import lille.iagl.entity.Post;
import lille.iagl.entity.StackTrace;
import lille.iagl.pythonstacktracerecognizer.PythonStackTraceRecognizer;
import lille.iagl.xmlCreator.XMLCreator;

/**
 *
 * @author francois
 */
public class StackoverflowParser {
    private XMLEventReader reader;
    private XMLCreator writer;
    private PythonStackTraceRecognizer recognizer;

    private Post _post;

    public StackoverflowParser(String filename, String writer) {
        try {
            XMLInputFactory xmlIF = XMLInputFactory.newFactory();
            InputStream inputStream = new FileInputStream(filename);
            this.reader = xmlIF.createXMLEventReader(inputStream);
            this.writer = new XMLCreator(writer);
            this.recognizer = new PythonStackTraceRecognizer();
            this.parse();
            this.writer.close();
        } catch (FileNotFoundException | XMLStreamException ex) {
            System.err.println("Erreur lors de l'ouverture dataset stackoverflow");
            Logger.getLogger(StackoverflowParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void parse() throws XMLStreamException {
                int eventType;
        XMLEvent event;

        this.writer.startElement("Posts");
        while (this.reader.hasNext()) {
            this._post = new Post();
            event = this.reader.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    checkElement(event);
                    break;
                default:
                    break;
            }
        }
    }
    private void checkElement(XMLEvent event) {
        Attribute body, tags, acceptedAnswer, idpost;
        QName q_body = new QName("Body");
        QName q_idpost = new QName("Id");

        if (!event.asStartElement().getName().toString().equals("row")) {
            return;
        }
        idpost = event.asStartElement().getAttributeByName(q_idpost);
        this._post.setPostId(idpost.getValue());
        body = event.asStartElement().getAttributeByName(q_body);
        if (body != null) {
            List<StackTrace> stacktraces = this.recognizer.getStackTrace(body.getValue());
            this._post.setStacktrace(stacktraces);
        }
    }
}
