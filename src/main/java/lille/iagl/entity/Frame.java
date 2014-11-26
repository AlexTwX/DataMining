/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.entity;

/**
 *
 * @author lefer
 */
public class Frame {

    private String file;
    private String line;
    private String method;

    public Frame(){
        line = file = method = null;
    }
    
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

    public void setFile(String file) {
        this.file = file;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    
}
