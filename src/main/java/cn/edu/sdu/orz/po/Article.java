package cn.edu.sdu.orz.po;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "summary", nullable = false, length = 1024)
    private String summary;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "view", nullable = false)
    private Integer view;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @ManyToMany
    @JoinTable(name = "tag_article",
            joinColumns = @JoinColumn(name = "article"),
            inverseJoinColumns = @JoinColumn(name = "tag"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article")
    private Set<Comment> comments = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

}