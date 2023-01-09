package cn.edu.sdu.orz.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ArticleDetailedInfo {
    private Integer id;
    private String title;
    private String authorName;
    private Instant modified;
    private Instant created;
    private Integer view;
    private String summary;
    private String content;

    public ArticleDetailedInfo(Integer id, String title, String authorName, Instant modified, Instant created, Integer view,
                               String summary, String content) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.modified = modified;
        this.created = created;
        this.view = view;
        this.summary = summary;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
