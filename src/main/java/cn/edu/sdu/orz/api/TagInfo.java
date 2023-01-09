package cn.edu.sdu.orz.api;

import java.time.Instant;

public class TagInfo {
    private Integer id;
    private String name;
    private String description;
    private Instant created;

    public TagInfo(Integer id, String name, String description, Instant created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
