/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.stackoverflowparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import lille.iagl.entity.Post;
import lille.iagl.entity.Question;
import lille.iagl.entity.Reponse;
import lille.iagl.entity.StackTrace;
import lille.iagl.pythonstacktracerecognizer.PythonStackTraceRecognizer;
import lille.iagl.xmlcreator.XMLCreator;

/**
 *
 * @author francois
 */
public class StackoverflowParser {
    private XMLEventReader reader;
    private XMLCreator writer;
    private PythonStackTraceRecognizer recognizer;

    private Post _post;

    public StackoverflowParser(String filename) {
        try {
            XMLInputFactory xmlIF = XMLInputFactory.newFactory();
            InputStream inputStream = new FileInputStream(filename);
            this.reader = xmlIF.createXMLEventReader(inputStream);
            this.recognizer = new PythonStackTraceRecognizer();
        } catch (FileNotFoundException | XMLStreamException ex) {
            System.err.println("Erreur lors de l'ouverture dataset stackoverflow");
            Logger.getLogger(StackoverflowParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createPythonDataSet(String writer) throws XMLStreamException {
        int eventType;
        XMLEvent event;

        this.writer = new XMLCreator(writer);
        this.writer.startElement("Posts");
        while (this.reader.hasNext()) {
            this._post = new Post();
            event = this.reader.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    getStacktrack(event);
                    break;
                default:
                    break;
            }
        }
        this.writer.endElement();
        this.writer.close();
    }
    private void getStacktrack(XMLEvent event) {
        Attribute body, idpost;
        QName q_body = new QName("Body");
        QName q_idpost = new QName("Id");

        if (!event.asStartElement().getName().toString().equals("row")) {
            return;
        }
        idpost = event.asStartElement().getAttributeByName(q_idpost);
        this._post.setPostId(idpost.getValue());
        body = event.asStartElement().getAttributeByName(q_body);
        if (body != null) {
            List<StackTrace> stacktraces = this.recognizer.getStackTrace(body.getValue());
            this._post.setStacktrace(stacktraces);
            if (!stacktraces.isEmpty()) {
                this._post.toXml(writer);
            }
        }
    }
    
    public void createQuestionStacktraceDataset(String writer) throws XMLStreamException {
        int eventType;
        XMLEvent event;
        List<Question> questions = new LinkedList<Question>();

         System.out.println("Creation d'un dataset contenant des questions python avec stacktrace");
        System.out.println("Debut de la lecture du fichier");
        while (this.reader.hasNext()) {
            this._post = new Post();
            event = this.reader.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    getQuestionWithStacktrace(event, questions);
                    break;
                default:
                    break;
            }
        }
        System.out.println("Fin de la lecture du fichier");
        System.out.println("Debut de l'écriture du fichier");
        this.writer = new XMLCreator(writer);
        this.writer.startElement("Posts");
        for (Question question : questions) {
            question.toXml(this.writer);
        }
        this.writer.endElement();
        this.writer.close();
        System.out.println("Fin de l'écriture du fichier");
    }

    private void getQuestionWithStacktrace(XMLEvent event, List<Question> questions) {
        Attribute body, idPost, acceptedAnswer, typePost, score, parentId;
        QName q_body = new QName("Body");
        QName q_idPost = new QName("Id");
        QName q_score = new QName("Score");
        QName q_parentId = new QName("ParentId");
        QName q_postType = new QName("PostTypeId");
        QName q_acceptedAnswer = new QName("AcceptedAnswerId");
        StartElement element = event.asStartElement();
        
        if (!element.getName().toString().equals("row")) {
            return;
        }
        typePost = element.getAttributeByName(q_postType);
        body = element.getAttributeByName(q_body);
        idPost = element.getAttributeByName(q_idPost);
        score = element.getAttributeByName(q_score);
        parentId = element.getAttributeByName(q_parentId);
        acceptedAnswer = element.getAttributeByName(q_acceptedAnswer);
        if (typePost.getValue().equals("1")) { //Question
            if (body != null) {
                List<StackTrace> stacktraces = this.recognizer.getStackTrace(body.getValue());
                if (!stacktraces.isEmpty()) {
                    Question question = new Question(idPost.getValue(), 
                            (acceptedAnswer == null?null:acceptedAnswer.getValue()), stacktraces);
                    questions.add(0, question);
                }
            }
        } else if (typePost.getValue().equals("2")) { //Response
            for (Question tmp : questions) {
                if (tmp.getId().equals(parentId.getValue())) {
                    Reponse reponse = new Reponse(score.getValue(), this.recognizer.getStackTrace(body.getValue()));
                    if (tmp.getAcceptedAnswerId() != null && tmp.getAcceptedAnswerId().equals(idPost.getValue())) {
                        tmp.setAcceptedAnswer(reponse);
                    } else {
                        tmp.addReponse(reponse);
                    }
                }
                if (Integer.valueOf(parentId.getValue()) > Integer.valueOf(tmp.getId())) {
                    break;
                }
            }
        }
    }
    
    public void createQuestionWithoutStacktraceDataset(String writer) throws XMLStreamException {
        int eventType;
        XMLEvent event;
        List<Question> questions = new LinkedList<Question>();

        System.out.println("Creation d'un dataset contenant des questions python sans stacktrace");
        System.out.println("Debut de la lecture du fichier");
        while (this.reader.hasNext()) {
            this._post = new Post();
            event = this.reader.nextEvent();
            eventType = event.getEventType();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    getQuestionWithoutStacktrace(event, questions);
                    break;
                default:
                    break;
            }
        }
        System.out.println("Fin de la lecture du fichier");
        System.out.println("Debut de l'écriture du fichier");
        this.writer = new XMLCreator(writer);
        this.writer.startElement("Posts");
        for (Question question : questions) {
            question.toXml(this.writer);
        }
        this.writer.endElement();
        this.writer.close();
        System.out.println("Fin de l'écriture du fichier");
    }

    private void getQuestionWithoutStacktrace(XMLEvent event, List<Question> questions) {
        Attribute body, idPost, acceptedAnswer, typePost, score, parentId, tags;
        QName q_body = new QName("Body");
        QName q_idPost = new QName("Id");
        QName q_score = new QName("Score");
        QName q_parentId = new QName("ParentId");
        QName q_postType = new QName("PostTypeId");
        QName q_acceptedAnswer = new QName("AcceptedAnswerId");
        QName q_tags = new QName("Tags");
        StartElement element = event.asStartElement();
        
        if (!element.getName().toString().equals("row")) {
            return;
        }
        typePost = element.getAttributeByName(q_postType);
        body = element.getAttributeByName(q_body);
        idPost = element.getAttributeByName(q_idPost);
        score = element.getAttributeByName(q_score);
        parentId = element.getAttributeByName(q_parentId);
        acceptedAnswer = element.getAttributeByName(q_acceptedAnswer);
        tags = element.getAttributeByName(q_tags);
        if (typePost.getValue().equals("1")) { //Question
            if (body != null && tags.getValue().contains("python")) {
                List<StackTrace> stacktraces = this.recognizer.getStackTrace(body.getValue());
                if (stacktraces.isEmpty()) {
                    Question question = new Question(idPost.getValue(), 
                            (acceptedAnswer == null?null:acceptedAnswer.getValue()), stacktraces);
                    questions.add(0, question);
                }
            }
        } else if (typePost.getValue().equals("2")) { //Response
            for (Question tmp : questions) {
                if (tmp.getId().equals(parentId.getValue())) {
                    Reponse reponse = new Reponse(score.getValue(), this.recognizer.getStackTrace(body.getValue()));
                    if (tmp.getAcceptedAnswerId() != null && tmp.getAcceptedAnswerId().equals(idPost.getValue())) {
                        tmp.setAcceptedAnswer(reponse);
                    } else {
                        tmp.addReponse(reponse);
                    }
                    break;
                }
                if (Integer.valueOf(parentId.getValue()) > Integer.valueOf(tmp.getId())) {
                    break;
                }
            }
        }
    }
}
