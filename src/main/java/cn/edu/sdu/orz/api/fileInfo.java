package cn.edu.sdu.orz.api;

import java.net.URI;
import java.net.URL;

public class fileInfo {

    private String name;

    private String type;

    private String uploadUserName;

    private URL path;

    public fileInfo(String name, String type, String uploadUserName, URL path) {
        this.name = name;
        this.type = type;
        this.uploadUserName = uploadUserName;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public URL getPath() {
        return path;
    }

    public void setPath(URL path) {
        this.path = path;
    }
}
