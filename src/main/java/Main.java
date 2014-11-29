
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import lille.iagl.python_bucket.PythonBucketter;
import lille.iagl.stackoverflowparser.StackoverflowParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author francois
 */ 
public class Main {
    public static String StackoverflowFile = "L:\\Posts.xml";
    public static String StacktrackDataset = "StacktrackDataset.xml";
    public static String QuestionStacktrackDataset = "QuestionStacktrackDataset.xml";
    public static String QuestionWithoutStacktracDataset = "QuestionWithoutStacktrackDataset.xml";
    
    public static void main(String[] args) {
        StackoverflowParser parser = new StackoverflowParser(StackoverflowFile);

        //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        try {
            //parser.createPythonDataSet(DatasetFile);
            //parser.createQuestionStacktraceDataset(QuestionStacktrackDataset);
            parser.createQuestionWithoutStacktraceDataset(QuestionWithoutStacktracDataset);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //PythonBucketter pb = new PythonBucketter(DatasetFile);
    }
}
