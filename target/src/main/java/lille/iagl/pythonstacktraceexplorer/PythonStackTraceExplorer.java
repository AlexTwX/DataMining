package lille.iagl.pythonstacktraceexplorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;


public class PythonStackTraceExplorer {
    private static final String exception = "\\s*\\w+:[^\\n]+\\n";
    private static Pattern exceptionPattern = Pattern.compile(exception);
    
    private FileReader fileReader;
    private XMLInputFactory xmlIF;
    private XMLStreamReader xmlSR;
    
    Map<String, Integer> map;
    
    public PythonStackTraceExplorer(String pathFile) {
        try {
            this.map = new HashMap<>();
            this.fileReader = new FileReader(pathFile);
            this.xmlIF = XMLInputFactory.newInstance();
            this.xmlSR = this.xmlIF.createXMLStreamReader(this.fileReader);
            this.explore();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PythonStackTraceExplorer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(PythonStackTraceExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void explore() throws XMLStreamException {
        int eventType;
        while (this.xmlSR.hasNext()) {
            eventType = this.xmlSR.next();
            switch (eventType) {
                case XMLEvent.CHARACTERS :
                    this.findException(this.xmlSR.getText());
                    break;
            }
        }
    }
    
    private void findException(String text) {
        Matcher exceptionMatcher = exceptionPattern.matcher(text);
        while (exceptionMatcher.find()) {
        	System.out.println(exceptionMatcher.group());
        }
    }
    
    public static void main(String[] args) {
    	PythonStackTraceExplorer test = new PythonStackTraceExplorer("StackTrace_Python.xml");
    }

}
