/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lille.iagl.pythonstacktracerecognizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Alexandre FRANCOIS <alexandre1.francois@etudiant-lille1.fr>
 */
public class PythonStackTraceRecognizer {
    private static final String header = "(Traceback \\(most recent call last\\):)";
    private static final String causes = "(\\s+File\\s+\"[^\"]+\",\\s+line\\s+\\d+,\\s+in[^\\n]+\\n[^\\n]+\\n)+";
    private static final String exception = "\\s*\\w+:[^\\n]+";
    private static final String pythonStackTrace = header + causes + exception;
    private static Pattern pythonStacktracePattern = Pattern.compile(pythonStackTrace);
    
    public static int stacktrack = 0;
    public static int question = 0;
    public static int reponse = 0;
    public static int stackSansReponse = 0;    
    public static int stackEtReponse = 0;
    public static int pythonSansReponse = 0;

    
    private FileReader fileReader;
    private XMLInputFactory xmlIF;
    private XMLEventReader xmlSR;

    public PythonStackTraceRecognizer(String pathFile) {
        try {
            this.fileReader = new FileReader(pathFile);
            this.xmlIF = XMLInputFactory.newInstance();
            this.fileReader.skip(3);
            this.xmlSR = this.xmlIF.createXMLEventReader(this.fileReader);
            this.scanFile();
        } catch (FileNotFoundException ex) {
            System.err.println("Impossible d'ouvrir le fichier");
            System.exit(1);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scanFile() throws XMLStreamException {
        int eventType;
        XMLEvent event;
        
        Attribute body,tags,acceptedAnswer;
        QName q_body     = new QName("Body");
        QName q_tags     = new QName("Tags");
        QName q_accepted = new QName("AcceptedAnswerId");
                
        while (this.xmlSR.hasNext()) {
            event = this.xmlSR.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_DOCUMENT :
                    System.out.println(">>>>> Debut du fichier");
                    break;
                case XMLEvent.END_DOCUMENT :
                    System.out.println("<<<<< Fin du fichier");
                    break;
                case XMLEvent.START_ELEMENT :
                    boolean stack = false;
                    boolean python = false;
                    boolean rep = false;
                        
                    body = event.asStartElement().getAttributeByName(q_body);
                    stack = this.findStackTrace(body.getValue());
                    if (stack) {
                        stacktrack++;                                
                    }
                
                    tags = event.asStartElement().getAttributeByName(q_tags);
                    if (tags.getValue().toLowerCase().contains("python")) {
                        question++;                                
                    } 
                    
                    acceptedAnswer = event.asStartElement().getAttributeByName(q_accepted); 
                    if (acceptedAnswer != null){
                        rep = true;
                    }
                    
                    
                    if (python && !stack && !rep) {
                        pythonSansReponse++;
                    }
                    if (stack && !rep) {
                        stackSansReponse++;
                    }
                    if (stack && rep) {
                        stackEtReponse++;
                    } else if (python && rep) {
                        reponse++;
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
  
    private boolean findStackTrace(String textUnescap) {
        String text = StringEscapeUtils.unescapeHtml(textUnescap);
        Matcher stackTraceMatcher = pythonStacktracePattern.matcher(text);
        return stackTraceMatcher.find();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PythonStackTraceRecognizer test = new PythonStackTraceRecognizer("L:\\Posts.xml");
        System.out.println("Question: " + PythonStackTraceRecognizer.question);
        System.out.println("StackTrace: " + PythonStackTraceRecognizer.stacktrack);
        System.out.println("QuestionSansStackSansReponse: " + PythonStackTraceRecognizer.pythonSansReponse);
        System.out.println("QuestionSansStackAvecReponse: " + PythonStackTraceRecognizer.reponse);
        System.out.println("StackEtReponse: " + PythonStackTraceRecognizer.stackEtReponse);
        System.out.println("StackSansReponse: " + PythonStackTraceRecognizer.stackSansReponse);
       

//        PythonBucketCreator  bucket = new PythonBucketCreator();
//        bucket.makeBucket("/home/m2iagl/lefer/Downloads/StackTrace_Python.xml");
//        
        }
    
}
