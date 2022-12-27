package cn.edu.sdu.orz.po;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@IdClass(LikeHistoryId.class)
@Table(name = "like_history")
public class LikeHistory {

    @NotNull
    @Id
    @Column(name = "article", nullable = false)
    private Integer article;

    @Id
    @Size(max = 64)
    @NotNull
    @Column(name = "ip", nullable = false, length = 64)
    @Pattern(regexp = "^(\\d{1,3}\\.){3}\\d{1,3}$")
    private String ip;

    @NotNull
    @Column(name = "time", nullable = false)
    private Instant time;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

//    public LikeHistory() {}
//
//    public LikeHistory(Integer article, String ip) {
//        this.article = article;
//        this.ip = ip;
//    }
}