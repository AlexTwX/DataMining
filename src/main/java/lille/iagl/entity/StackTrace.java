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
import lille.iagl.xmlCreator.XMLCreator;

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
        this.exception = new StackTraceException();
    }

    public void setException(String type, String message) {
        this.exception = new StackTraceException(type, message);
    }

    public void setType(String t) {
        this.exception.setType(t);
    }

    public void setMessage(String m) {
        this.exception.setMessage(m);
    }

    public String getType() {
        return this.exception.getType();
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
        sb.append("<Type>" + this.exception.getType().trim() + "</Type>");
        sb.append("<Message>" + this.exception.getMessage().trim() + "</Message>");
        for (Frame frame : this.frames) {
            sb.append("<Frame>");
            sb.append("<File>" + frame.getFile() + "</File>");
            sb.append("<Line>" + frame.getLine() + "</Line>");
            sb.append("<Method>" + frame.getMethod() + "</Method>");
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

    public void toXml(XMLCreator writer) {
        writer.startElement("Stack");
        writer.createElement("Type", this.exception.getType().trim());
        writer.createElement("Message", this.exception.getMessage().trim());
        for (Frame frame : this.frames) {
            writer.startElement("Frame");
            writer.createElement("File", frame.getFile().trim());
            writer.createElement("Line", frame.getLine().trim());
            writer.createElement("Method", frame.getMethod().trim());
            writer.endElement();
        }
        writer.endElement();
    }

    public void addFrame(lille.iagl.entity.Frame _frame) {
        this.frames.add(_frame);
    }

}
