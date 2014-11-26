/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.pythonstacktracerecognizer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lille.iagl.entity.StackTrace;
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
