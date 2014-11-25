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
    }
    public void setStacktrace(StackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPostId() {
        return postId;
    }
    public StackTrace getStacktrace() {
        return stacktrace;
    }
    public String getUrl() {
        return url;
    }
}
