package cn.edu.sdu.orz.po;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TagArticleId implements Serializable {
    private static final long serialVersionUID = -6641225216975364856L;
    @Column(name = "tag", nullable = false)
    private Integer tag;

    @Column(name = "article", nullable = false)
    private Integer article;

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagArticleId entity = (TagArticleId) o;
        return Objects.equals(this.tag, entity.tag) &&
                Objects.equals(this.article, entity.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, article);
    }

}