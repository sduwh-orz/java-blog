package cn.edu.sdu.orz.po;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "author_name", nullable = false, length = 32)
    private String authorName;

    @Column(name = "email", nullable = false, length = 200)
    @Email(regexp = "[a-zA-Z0-9_\\-]+@[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)+")
    private String email;

    @Column(name = "ip", nullable = false, length = 64)
    @Pattern(regexp = "^(\\d{1,3}\\.){3}\\d{1,3}$")
    private String ip;

    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @NotNull
    @Column(name = "like_num", nullable = false)
    private Integer likeNum;

    @OneToMany(mappedBy = "parent")
    private Set<Comment> comments = new LinkedHashSet<>();


    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}