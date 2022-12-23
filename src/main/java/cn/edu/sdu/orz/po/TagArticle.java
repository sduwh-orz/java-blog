package cn.edu.sdu.orz.po;

import javax.persistence.*;

@Entity
@Table(name = "tag_article")
@IdClass(TagArticleId.class)
public class TagArticle {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag", nullable = false)
    private Tag tag;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article", nullable = false)
    private Article article;

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