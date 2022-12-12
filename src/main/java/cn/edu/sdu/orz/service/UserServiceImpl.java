package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.sdu.orz.dao.PermGroupRepository;
import cn.edu.sdu.orz.po.PermGroup;
import cn.edu.sdu.orz.po.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PermGroupRepository permGroupRepo;

    private PermGroup getDefaultGroup() {
        return permGroupRepo.findById(1).orElseThrow();
    }

    @Override
    public User getUser(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User getUser(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, User.getHashedPassword(password));
    }

    @Override
    public Boolean createUser(String username, String password, String nickname, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setRawPassword(password);
            user.setNickname(nickname);
            user.setEmail(email);
            user.setPermGroup(getDefaultGroup());
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
