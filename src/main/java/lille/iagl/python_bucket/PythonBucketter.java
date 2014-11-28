/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.python_bucket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import lille.iagl.entity.Post;
import lille.iagl.entity.StackTrace;
import lille.iagl.entity.Frame;
import lille.iagl.pythonstacktracerecognizer.PythonStackTraceRecognizer;
import lille.iagl.xmlcreator.XMLCreator;

/**
 *
 * @author naryushi
 */
public class PythonBucketter {

    private FileReader fileReader;
    private XMLInputFactory xmlIF;
    private XMLEventReader xmlSR;

    private Map<String, List<Post>> _posts;
    private Post _post;
    private StackTrace _stack;
    private Frame _frame;

    public PythonBucketter(String pathFileInput) {
        try {

            XMLInputFactory xmlIF = XMLInputFactory.newFactory();
            InputStream inputStream = new FileInputStream(pathFileInput);
            this.xmlSR = xmlIF.createXMLEventReader(inputStream);
            this.file_to_list();
            this.makeBucket();
        } catch (XMLStreamException | FileNotFoundException ex) {
            Logger.getLogger(PythonStackTraceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Ajoute le post dans la map
    private void insert_post() {
        String keyForMap;
        for (StackTrace st : this._post.getStacktraces()) {
            keyForMap = st.getType();
            if (this._posts.get(keyForMap) == null) {
                this._posts.put(keyForMap, new ArrayList<Post>());
            }
            this._posts.get(keyForMap).add(new Post(this._post));
        }
    }

    public void file_to_list() throws XMLStreamException {
        int eventType;
        XMLEvent event;

        this._posts = new HashMap<>();

        this._post = null;

        while (this.xmlSR.hasNext()) {
            event = this.xmlSR.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:

                    switch (event.asStartElement().getName().toString().toLowerCase()) {
                        // Si on tombe sur un post, on sauvegarde l'ancien et on instancie un nouveau
                        case "post":

                            // reinitialisation des variables
                            this._post = new Post();
                            this._stack = null;
                            this._frame = null;
                            break;

                        case "postid":
                            event = this.xmlSR.nextEvent();
                            this._post.setPostId(event.asCharacters().getData());
                            break;
                        case "url":
                            event = this.xmlSR.nextEvent();
                            this._post.setUrl(event.asCharacters().getData());
                            break;
                        case "stack":
                            this._stack = new StackTrace();
                            break;
                        case "type":
                            event = this.xmlSR.nextEvent();
                            this._stack.setType(event.asCharacters().getData());
                            break;
                        case "message":
                            event = this.xmlSR.nextEvent();
                            if (event.isEndElement()) {
                                this._stack.setMessage("");
                            } else {
                                this._stack.setMessage(event.asCharacters().getData());
                            }
                            break;

                        case "frame":

                            this._frame = new Frame();
                            break;
                        case "file":
                            event = this.xmlSR.nextEvent();
                            this._frame.setFile(event.asCharacters().getData());
                            break;
                        case "line":
                            event = this.xmlSR.nextEvent();
                            this._frame.setLine(event.asCharacters().getData());
                            break;
                        case "method":
                            event = this.xmlSR.nextEvent();
                            this._frame.setMethod(event.asCharacters().getData());
                            break;
                    }

                    break;

                case XMLEvent.END_ELEMENT:
                    switch (event.asEndElement().getName().toString().toLowerCase()) {
                        case "stack":
                            if (this._stack != null) {
                                this._post.addStackTrace(this._stack);
                                insert_post();
                                this._post.getStacktraces().clear();
                                //this._post.addStackTrace(this._stack);
                            }
                            break;
                        case "frame":
                            if (this._frame != null) {
                                this._stack.addFrame(this._frame);
                            }
                            break;
//                        case "post":
//                            if (this._post != null) {
//                                insert_post();
//                            }
//                            break;
                    }

                case XMLEvent.END_DOCUMENT:

                default:
                    break;
            }
        }
    }

    public void makeBucket() {

        // Directory Creation
        File theDir = new File("buckets");
        boolean directory_exist = true;

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: buckets");

            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                directory_exist = false;
            }
        }

        // Ecriture des buckets
        if (directory_exist) {
            List<Post> posts;
            XMLCreator xmlCreator;
            // Parcours de l'ensemble des buckets
            System.out.println(this._posts.entrySet().size());
            for (Entry<String, List<Post>> e : this._posts.entrySet()) {

                //System.out.println(e.getKey());
                posts = (List<Post>) e.getValue();
                xmlCreator = new XMLCreator("buckets/" + e.getKey() + ".xml");
                xmlCreator.startElement("Posts");
                for (Post p : posts) {
                    p.toXml(xmlCreator);
                }
                xmlCreator.endElement();

                xmlCreator.close();
            }

        }


    }

}
