/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lille.iagl.entity;

/**
 *
 * @author francois
 */
public class Post {
    private String postId;
    private String url;
    private StackTrace stacktrace;
    
    public Post() {
        this.stacktrace = null;
    }
    public Boolean hasStackTrace() {
        return this.stacktrace != null;
    }
    public void setPostId(String postId) {
        this.postId = postId;
        this.url = "http://stackoverflow.com/questions/" + postId;
    }
    public void setStacktrace(StackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getPostId() {
        return postId;
    }

    public String getUrl() {
        return url;
    }
    
    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Post>");
        sb.append("<PostId>"+this.getPostId()+"</PostId>");
        sb.append("<url>"+this.getUrl()+"</url>");
        sb.append(this.stacktrace.toXml());
        sb.append("</Post>");
        return sb.toString();
    }
}
