package cn.edu.sdu.orz.po;

import org.springframework.util.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Entity
@Table(name = "file")
public class File {

    private static final String salt = "kBn?$>~&Opw$]v:f";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Lob
    @Column(name = "path", nullable = false)
    private String path;

    @Lob
    @Column(name = "md5", nullable = false)
    private String md5;

    @Column(name = "created")
    private Instant created;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.md5 = getHashedName(name);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public static String getHashedName(String name) {
        return DigestUtils.md5DigestAsHex((salt + name + salt).getBytes(StandardCharsets.UTF_8));
    }
}