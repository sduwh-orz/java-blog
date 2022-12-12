package cn.edu.sdu.orz.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import cn.edu.sdu.orz.po.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}