/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.pythonstacktracerecognizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.xml.stream.XMLStreamException;
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
    private static Pattern pythonStacktracePattern = Pattern.compile(pythonStackTrace);

    private Post _post;

    private FileReader fileReader;
    private XMLInputFactory xmlIF;
    private XMLEventReader xmlSR;

    public PythonStackTraceRecognizer(String pathFile) {
        try {
            this.fileReader = new FileReader(pathFile);
            this.xmlIF = XMLInputFactory.newFactory();

            InputStream xmlInputStream = null;
            try {
                xmlInputStream = new FileInputStream(pathFile);
            } catch (FileNotFoundException e) {
                if (xmlInputStream != null) {
                    try {
                        xmlInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.exit(3);
            }

            this.xmlSR = this.xmlIF.createXMLEventReader(xmlInputStream);
            this.scanFile();
        } catch (FileNotFoundException ex) {
            System.err.println("Impossible d'ouvrir le fichier");
            System.exit(1);
        } catch (XMLStreamException ex) {
            Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scanFile() throws XMLStreamException {

        int eventType;
        XMLEvent event;

        Attribute body, tags, acceptedAnswer, idpost;
        QName q_body = new QName("Body");
        QName q_idpost = new QName("Id");

        while (this.xmlSR.hasNext()) {
            this._post = new Post();
            event = this.xmlSR.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {

                case XMLEvent.START_ELEMENT:
                    if (!event.asStartElement().getName().toString().equals("row")) {
                        break;
                    }
                                        
                    // Récupération de l'ID + url
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
    }

    private void findStackTrace(String textUnescap) {
        String text = StringEscapeUtils.unescapeHtml(textUnescap);
        StackTrace st = new StackTrace();
        Matcher stackTraceMatcher = pythonStacktracePattern.matcher(text);
        while (stackTraceMatcher.find()) {
            st.setException(stackTraceMatcher.group(2), stackTraceMatcher.group(3));
            st.setFrame(stackTraceMatcher.group());
            this._post.setStacktrace(st);
            System.out.println("---------------------------");
            System.out.println(stackTraceMatcher.group());
            System.out.println("---------------------------");
            this.stacktraceWriter();
        }
    }
    
    private void stacktraceWriter() {
        System.out.println(this._post.toXml());
            System.out.println("#########################");
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

        PythonStackTraceRecognizer test = new PythonStackTraceRecognizer("/gfs/lefer/stackexchange/Posts.xml");

    }

}
