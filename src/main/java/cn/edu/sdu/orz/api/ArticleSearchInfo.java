package cn.edu.sdu.orz.api;

import java.time.Instant;

public class ArticleSearchInfo {
    private Integer id;
    private String title;
    private String authorName;
    private Instant modified;
    private Integer view;
    private String summary;

    public ArticleSearchInfo() {
    }

    public ArticleSearchInfo(Integer id, String title, String authorName, Instant modified, Integer view, String summary) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.modified = modified;
        this.view = view;
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "ArticleSearchInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", modified=" + modified +
                ", view=" + view +
                ", summary='" + summary + '\'' +
                '}';
    }
}
