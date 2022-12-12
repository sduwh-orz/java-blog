package cn.edu.sdu.orz.api;

public class UserInfo {
    private String username;
    private String nickname;
    private String email;
    private String PermGroup;

    public UserInfo(String username, String nickname, String email, String permGroup) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        PermGroup = permGroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermGroup() {
        return PermGroup;
    }

    public void setPermGroup(String permGroup) {
        PermGroup = permGroup;
    }
}
