/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.entity;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author francois
 */
public class StackTrace {
    private List<Frame> frame;
    private Exception exception;

    public StackTrace(String type, String message, List frame) {
        this.exception = new Exception(type, message);
        this.frame = new LinkedList<Frame>();
        while (frame.size() < 0) {
            this.frame.add(new Frame(toto.type, toto.line, toto.message));
        }
    }
    private class Exception {
        private String type;
        private String message;
        
        public Exception(String type, String exception) {
            this.type = type;
            this.message = message;
        }
        public String getType() {
            return type;
        }
        public String getMessage() {
            return message;
        }
    }
    private class Frame {
        private String file;
        private String line;
        private String method;
        
        public Frame(String file, String line, String method) {
            this.line = line;
            this.file = file;
            this.method = method;
        }
        public String getFile() {
            return file;
        }
        public String getLine() {
            return line;
        }
        public String getMethod() {
            return method;
        }
    }
}
