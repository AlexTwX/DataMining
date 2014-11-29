/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lille.iagl.entity;

import java.util.List;
import lille.iagl.xmlcreator.XMLCreator;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Alexandre FRANCOIS <alexandre1.francois@etudiant-lille1.fr>
 */
public class Reponse {
    private String score;
//    private String text;
    private List<StackTrace> stacktraces = null;
    
    public Reponse(String score,  List<StackTrace> stacktraces) {
        this.score = score;
//        this.text = StringEscapeUtils.unescapeHtml(text);
        this.stacktraces = stacktraces;
    }
    public void toXml(XMLCreator writer) {
        writer.createElement("Score", this.score);
//        writer.createElement("text", this.text);
        writer.startElement("Stacktraces");
        for (StackTrace stacktrace : this.stacktraces) {
            stacktrace.toXml(writer);
        }
        writer.endElement();
    }
}
