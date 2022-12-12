package cn.edu.sdu.orz.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.DigestUtils;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Entity
@Table(name = "user")
@DynamicUpdate
@DynamicInsert
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
    private String email;

    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Column(name = "create_date")
    private Instant createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perm_group", nullable = false)
    private PermGroup permGroup;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRawPassword(String raw) {
        this.password = getHashedPassword(raw);
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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public PermGroup getPermGroup() {
        return permGroup;
    }

    public void setPermGroup(PermGroup permGroup) {
        this.permGroup = permGroup;
    }


    public static String getHashedPassword(String raw) {
        return DigestUtils.md5DigestAsHex((salt + raw + salt).getBytes(StandardCharsets.UTF_8));
    }
}