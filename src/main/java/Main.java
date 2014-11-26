
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
    public static String StackoverflowFile = "/gfs/lefer/stackexchange/Posts.xml";
    public static String DatasetFile = "Python_dataset.xml";
    
    public static void main(String[] args) {
        
         StackoverflowParser parser = 
               new StackoverflowParser(StackoverflowFile, DatasetFile);
        
        PythonBucketter pb = new PythonBucketter(DatasetFile);
    }
}
