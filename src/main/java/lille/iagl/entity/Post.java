/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.entity;

import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import lille.iagl.xmlcreator.XMLCreator;

/**
 *
 * @author francois
 */
public class Post {
    private String postId;
    private String url;
    private List<StackTrace> stacktraces;
    
    public Post() {
        this.stacktraces = null;
    }
    public Boolean hasStackTrace() {
        return this.stacktraces != null;
    }
    public void setPostId(String postId) {
        this.postId = postId;
        this.url = "http://stackoverflow.com/questions/" + postId;
    }
    public void setStacktrace(List<StackTrace> stacktraces) {
        this.stacktraces = stacktraces;
    }

    public String getPostId() {
        return postId;
    }

    public String getUrl() {
        return url;
    }
    
    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Post>");
        sb.append("<PostId>"+this.getPostId()+"</PostId>");
        sb.append("<url>"+this.getUrl()+"</url>");
        for (StackTrace st : this.stacktraces) {
            st.toXml();
        }
        sb.append("</Post>");
        return sb.toString();
    }

    public void toXml(XMLStreamWriter xmlSW) throws XMLStreamException {
        xmlSW.writeStartElement("Post");
        xmlSW.writeStartElement("PostId");
        xmlSW.writeCharacters(this.getPostId());
        xmlSW.writeEndElement();
        xmlSW.writeStartElement("Url");
        xmlSW.writeCharacters(this.getUrl());
        xmlSW.writeEndElement();
        for (StackTrace st : this.stacktraces) {
            st.toXml(xmlSW);
        }
        xmlSW.writeEndElement();

    }
    public void toXml(XMLCreator writer) {
        writer.startElement("Post");
        writer.createElement("PostId", this.getPostId());
        writer.createElement("Url", this.getUrl());
        for (StackTrace st : this.stacktraces) {
            st.toXml(writer);
        }
        writer.endElement();
    }
}
