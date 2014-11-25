/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.pythonstacktracerecognizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import lille.iagl.entity.Post;
import lille.iagl.entity.StackTrace;
import lille.iagl.entity.StackTrace.StackTraceException;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Alexandre FRANCOIS <alexandre1.francois@etudiant-lille1.fr>
 */
public class PythonStackTraceRecognizer {

    private static final String header = "Traceback \\(most recent call last\\):";
    private static final String causes = "(\\s+File\\s+\"[^\"]+\",\\s+line\\s+\\d+,\\s+in[^\\n]+\\n[^\\n]+\\n)+";
    private static final String exception = "\\s*(\\w+):([^\\n]+)";
    private static final String pythonStackTrace = header + causes + exception;
    private static final Pattern pythonStacktracePattern = Pattern.compile(pythonStackTrace);

    private Post _post;

    private XMLEventReader xmlSR;
    private XMLStreamWriter xmlSW;

    public PythonStackTraceRecognizer(String pathFileInput, String pathFileOutput) {
        try {
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            FileOutputStream outputStream = new FileOutputStream(pathFileOutput);
            this.xmlSW = output.createXMLStreamWriter(outputStream, "UTF-8");

            XMLInputFactory xmlIF = XMLInputFactory.newFactory();
            InputStream inputStream = new FileInputStream(pathFileInput);
            this.xmlSR = xmlIF.createXMLEventReader(inputStream);
            this.scanFile();
        } catch (XMLStreamException | FileNotFoundException ex) {
            Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scanFile() throws XMLStreamException {
        int eventType;
        XMLEvent event;
        Attribute body, tags, acceptedAnswer, idpost;
        QName q_body = new QName("Body");
        QName q_idpost = new QName("Id");

        this.xmlSW.writeStartDocument("UTF-8", "1.0");
        this.xmlSW.writeStartElement("Posts");
        while (this.xmlSR.hasNext()) {
            this._post = new Post();
            event = this.xmlSR.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    if (!event.asStartElement().getName().toString().equals("row")) {
                        break;
                    }
                    idpost = event.asStartElement().getAttributeByName(q_idpost);
                    this._post.setPostId(idpost.getValue());
                    body = event.asStartElement().getAttributeByName(q_body);
                    if (body != null) {
                        this.findStackTrace(body.getValue());
                   }
                    break;
                default:
                    break;
            }
        }
        this.xmlSW.writeEndElement();
        this.xmlSW.flush();
        this.xmlSW.close();
    }

    private void findStackTrace(String textUnescap) {
        String text = StringEscapeUtils.unescapeHtml(textUnescap);
        StackTrace st = new StackTrace();
        Matcher stackTraceMatcher = pythonStacktracePattern.matcher(text);
        while (stackTraceMatcher.find()) {
            st.setException(stackTraceMatcher.group(2), stackTraceMatcher.group(3));
            st.setFrame(stackTraceMatcher.group());
            this._post.setStacktrace(st);
            this.stacktraceWriter();
        }
    }
    
    private void stacktraceWriter() {
        try {
            this._post.toXml(this.xmlSW);
        } catch (XMLStreamException ex) {
            Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        PythonStackTraceRecognizer test = new PythonStackTraceRecognizer("/gfs/lefer/stackexchange/Posts.xml");
//        System.out.println("Question: " + PythonStackTraceRecognizer.question);
//        System.out.println("StackTrace: " + PythonStackTraceRecognizer.stacktrack);
//        System.out.println("QuestionSansStackSansReponse: " + PythonStackTraceRecognizer.pythonSansReponse);
//        System.out.println("QuestionSansStackAvecReponse: " + PythonStackTraceRecognizer.reponse);
//        System.out.println("StackEtReponse: " + PythonStackTraceRecognizer.stackEtReponse);
//        System.out.println("StackSansReponse: " + PythonStackTraceRecognizer.stackSansReponse);

        PythonStackTraceRecognizer test = new PythonStackTraceRecognizer("L:\\Posts.xml", "PythonDataset.xml");

    }

}
