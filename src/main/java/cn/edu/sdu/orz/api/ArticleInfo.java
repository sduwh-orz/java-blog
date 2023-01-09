package cn.edu.sdu.orz.api;

import cn.edu.sdu.orz.po.Tag;
import cn.edu.sdu.orz.po.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ArticleInfo {
    private Integer id;
    private String title;
    private String authorName;
    private Instant modified;
    private Integer view;
    private String summary;
    private String content;
    private Set<String> tagNames;
    @JsonProperty("recommendArticleId")
    private List<Integer> recommendArticleIdList;

    public ArticleInfo(Integer id, String title, String authorName, Instant modified, Integer view,
                       String summary, String content, Set<String> tagNames, List<Integer> recommendArticleIdList) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.modified = modified;
        this.view = view;
        this.summary = summary;
        this.content = content;
        this.tagNames = tagNames;
        this.recommendArticleIdList = recommendArticleIdList;
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

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }

    public List<Integer> getRecommendArticleIdList() {
        return recommendArticleIdList;
    }

    public void setRecommendArticleIdList(List<Integer> recommendArticleIdList) {
        this.recommendArticleIdList = recommendArticleIdList;
    }
}
