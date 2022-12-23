package cn.edu.sdu.orz.api;

import cn.edu.sdu.orz.po.User;

import java.time.Instant;

public class ArticleInfo {
    private String title;
    private String authorName;
    private Instant modified;
    private Integer view;
    private String summary;
    private String content;

    public ArticleInfo() {
    }

    public ArticleInfo(String title, String authorName, Instant modified, Integer view,
                       String summary, String content) {
        this.title = title;
        this.authorName = authorName;
        this.modified = modified;
        this.view = view;
        this.summary = summary;
        this.content = content;
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
}
