/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lille.iagl.entity;

import java.util.LinkedList;
import java.util.List;
import lille.iagl.xmlcreator.XMLCreator;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Alexandre FRANCOIS <alexandre1.francois@etudiant-lille1.fr>
 */
public class Question {
    private String id = null;
    private String url = null;
//    private String body = null;
    private String acceptedAnswerId = null;
    private List<StackTrace> stacktraces = null;
    private Reponse acceptedAnswer = null;
    private List<Reponse> reponses = null;
    
    public Question(String id, String acceptedAnswerId, List<StackTrace> stacktraces) {
        this.id = id;
        this.url = "http://stackoverflow.com/questions/" + id;
        this.acceptedAnswerId = acceptedAnswerId;
//        this.body = StringEscapeUtils.unescapeHtml(body);
        this.stacktraces = stacktraces;
        this.reponses = new LinkedList<Reponse>();
    }

    public void setAcceptedAnswer(Reponse acceptedAnswer) {
        this.acceptedAnswer = acceptedAnswer;
    }
    public void addReponse(Reponse reponse) {
        this.reponses.add(reponse);
    }
    public String getId() {
        return id;
    }
    public String getAcceptedAnswerId() {
        return acceptedAnswerId;
    }
    public void toXml(XMLCreator writer) {
        writer.startElement("Question");
        writer.createElement("Id", this.id);
        writer.createElement("Url", this.url);
        if (stacktraces != null && !stacktraces.isEmpty()) {
            writer.startElement("Stacktraces");
            for (StackTrace stacktrace : this.stacktraces) {
                stacktrace.toXml(writer);
            }
            writer.endElement();
        }
//        writer.createElement("Body", this.body);
        if (this.acceptedAnswer != null) {
            writer.startElement("AcceptedAnswer");
            this.acceptedAnswer.toXml(writer);
            writer.endElement();
        } else {
            writer.createElement("AcceptedAnswer", "");
        }
        writer.startElement("Reponses");
        for (Reponse reponse : this.reponses) {
            writer.startElement("Reponse");
            reponse.toXml(writer);
            writer.endElement();
        }
        writer.endElement();
        writer.endElement();
    }
}
