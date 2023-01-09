package cn.edu.sdu.orz.po;

import org.springframework.util.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    private static final String salt = "neR>:Uum|-H+TPGN";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "email", nullable = false, length = 64)
    @Email(regexp = "^([a-zA-z0-9_\\.-]+)@([\\\\da-zA-Z0-9\\.-]+)\\.([a-z\\.]{2,6})$")
    private String email;

    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created")
    private Instant created;

    @OneToMany(mappedBy = "author")
    private Set<Article> articles = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setRawPassword(String raw) {
        this.password = getHashedPassword(raw);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public static String getHashedPassword(String raw) {
        return DigestUtils.md5DigestAsHex((salt + raw + salt).getBytes(StandardCharsets.UTF_8));
    }
}
