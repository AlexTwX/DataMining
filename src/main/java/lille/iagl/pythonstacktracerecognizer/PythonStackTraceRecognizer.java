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
import java.util.LinkedList;
import java.util.List;
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

    public List<StackTrace> getStackTrace(String textUnescap) {
        String text = StringEscapeUtils.unescapeHtml(textUnescap);
        List<StackTrace> list = new LinkedList<>();
        StackTrace st = new StackTrace();
        
        Matcher stackTraceMatcher = pythonStacktracePattern.matcher(text);
        while (stackTraceMatcher.find()) {
            st.setException(stackTraceMatcher.group(2), stackTraceMatcher.group(3));
            st.setFrame(stackTraceMatcher.group());
            list.add(st);
        }
        return list;
    }
}
