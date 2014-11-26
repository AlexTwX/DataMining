
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
    public static String stackoverflowFile = "/gfs/francois/Posts.xml";
    public static String datasetFile = "dataset.xml";
    
    public static void main(String[] args) {
        StackoverflowParser parser = new StackoverflowParser(stackoverflowFile, datasetFile);
    }
}
