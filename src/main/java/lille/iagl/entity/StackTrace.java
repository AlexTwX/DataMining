/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author francois
 */
public class StackTrace {
    private static final String causes = "File\\s+\"([^\"]+)\",\\s+line\\s+(\\d+),\\s+in\\s+([^\\n]+)\\n";
    private static Pattern causesPattern = Pattern.compile(causes);

    private List<Frame> frames;
    private StackTraceException exception;

    public StackTrace() {
        this.frames = new LinkedList<>();
    }

    public void setException(String type, String message) {
        this.exception = new StackTraceException(type, message);
    }
    
    private String escapeUnusedChar(String text) {
        return text.replace('<', ' ').replace('>', ' ').replace('\n', ' ').trim();
    }
    public void setFrame(String frames) {
        Matcher causeMatcher = causesPattern.matcher(frames);
        while (causeMatcher.find()) {
            this.frames.add(
                new Frame(escapeUnusedChar(causeMatcher.group(1)),
                    causeMatcher.group(2),
                    escapeUnusedChar(causeMatcher.group(3)))
            );
        }
    }

    public List<Frame> getFrames() {
        return frames;
    }
    
    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Stack>");
        sb.append("<Type>"+this.exception.getType().trim()+"</Type>");
        sb.append("<Message>"+this.exception.getMessage().trim()+"</Message>");
        for (Frame frame : this.frames) {
            sb.append("<Frame>");
            sb.append("<File>"+frame.getFile()+"</File>");
            sb.append("<Line>"+frame.getLine()+"</Line>");
            sb.append("<Method>"+frame.getMethod()+"</Method>");
            sb.append("</Frame>");
        }
        sb.append("</Stack>");
        return sb.toString();
    }

    void toXml(XMLStreamWriter xmlSW) throws XMLStreamException {
        xmlSW.writeStartElement("Stack");
        xmlSW.writeStartElement("Type");
        xmlSW.writeCharacters(this.exception.getType().trim());
        xmlSW.writeEndElement();
        xmlSW.writeStartElement("Message");
        xmlSW.writeCharacters(this.exception.getMessage().trim());
        xmlSW.writeEndElement();
        for (Frame frame : this.frames) {
            xmlSW.writeStartElement("Frame");
            xmlSW.writeStartElement("File");
            xmlSW.writeCharacters(frame.getFile().trim());
            xmlSW.writeEndElement();
            xmlSW.writeStartElement("Line");
            xmlSW.writeCharacters(frame.getLine().trim());
            xmlSW.writeEndElement();
            xmlSW.writeStartElement("Method");
            xmlSW.writeCharacters(frame.getMethod().trim());
            xmlSW.writeEndElement();
            xmlSW.writeEndElement();
        }
        xmlSW.writeEndElement();
    }
    
    
    public class StackTraceException {
        private String type;
        private String message;
        
        public StackTraceException(String type, String message) {
            this.type = type;
            this.message = message;
        }
        public String getType() {
            return type;
        }
        public String getMessage() {
            return message;
        }
    }
    private class Frame {
        private String file;
        private String line;
        private String method;
        
        public Frame(String file, String line, String method) {
            this.line = line;
            this.file = file;
            this.method = method;
        }
        public String getFile() {
            return file;
        }
        public String getLine() {
            return line;
        }
        public String getMethod() {
            return method;
        }
    }
}
