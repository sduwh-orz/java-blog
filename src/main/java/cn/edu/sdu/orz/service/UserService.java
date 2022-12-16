package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.User;

public interface UserService {
    User getUser(Integer id);
    User getUser(String username);
    User getUser(String username, String password);
    Boolean createUser(String username, String password, String nickname, String email);
    Boolean deleteUser(String username);
}
