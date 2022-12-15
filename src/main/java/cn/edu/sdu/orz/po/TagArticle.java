package cn.edu.sdu.orz.po;

import javax.persistence.*;

@Entity
@Table(name = "tag_article")
public class TagArticle {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private TagArticleId id;

    @MapsId("tag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag", nullable = false)
    private Tag tag;

    @MapsId("article")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article", nullable = false)
    private Article article;

    public TagArticleId getId() {
        return id;
    }

    public void setId(TagArticleId id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}