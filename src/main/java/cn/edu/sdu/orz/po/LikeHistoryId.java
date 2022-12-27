package cn.edu.sdu.orz.po;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikeHistoryId implements Serializable {
    private static final long serialVersionUID = -2136143579692668851L;
    @Column(name = "article", nullable = false)
    private Integer article;

    @Column(name = "ip", nullable = false, length = 64)
    @Pattern(regexp = "^(\\d{1,3}\\.){3}\\d{1,3}$")
    private String ip;

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LikeHistoryId entity = (LikeHistoryId) o;
        return Objects.equals(this.ip, entity.ip) &&
                Objects.equals(this.article, entity.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, article);
    }

}