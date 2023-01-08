package cn.edu.sdu.orz.api;

import java.time.Instant;

public class CommentInfo {

    private Integer id;
    private Integer author;
    private Integer parent;
    private String authorName;
    private String email;
    private String ip;
    private String content;
    private Instant created;
    private Instant modified;
    private Integer likeNum;

    public CommentInfo() {
    }

    public CommentInfo(Integer id, Integer author, Integer parent, String authorName, String email, String ip, String content, Instant created, Instant modified, Integer likeNum) {
        this.id = id;
        this.author = author;
        this.parent = parent;
        this.authorName = authorName;
        this.email = email;
        this.ip = ip;
        this.content = content;
        this.created = created;
        this.modified = modified;
        this.likeNum = likeNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }
}