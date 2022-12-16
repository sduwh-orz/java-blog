package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.sdu.orz.po.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;

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
            // Check if exists a user whose username equals input and type equals "deleted"
            User prevUser = userRepo.findByUsername(username);
            if(prevUser != null) {
                if(prevUser.getType().equals("deleted")) {
                    userRepo.deleteById(prevUser.getId());
                }
                else {
                    return false;
                }
            }
            // create
            User user = new User();
            user.setUsername(username);
            user.setRawPassword(password);
            user.setNickname(nickname);
            user.setEmail(email);
            user.setType("user");
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteUser(String username) {
        try {
            User user = userRepo.findByUsername(username);
            if(user != null) {
                if(user.getType().equals("deleted")) {
                    return false;
                }
                userRepo.updateType("deleted", user.getId());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
