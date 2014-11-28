/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lille.iagl.entity;

import lille.iagl.xmlcreator.XMLCreator;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Alexandre FRANCOIS <alexandre1.francois@etudiant-lille1.fr>
 */
public class Reponse {
    private String score;
    private String text;
    
    public Reponse(String score, String text) {
        this.score = score;
        this.text = StringEscapeUtils.unescapeHtml(text);
    }
    public void toXml(XMLCreator writer) {
        writer.createElement("Score", this.score);
        writer.createElement("text", this.text);
    }
}
