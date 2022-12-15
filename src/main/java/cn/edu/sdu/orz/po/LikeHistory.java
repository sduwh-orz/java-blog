package cn.edu.sdu.orz.po;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "like_history")
public class LikeHistory {
    @EmbeddedId
    private LikeHistoryId id;

    @MapsId("article")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article", nullable = false)
    private Article article;

    @Column(name = "time")
    private Instant time;

    public LikeHistoryId getId() {
        return id;
    }

    public void setId(LikeHistoryId id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

}