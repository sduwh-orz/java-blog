package cn.edu.sdu.orz.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import cn.edu.sdu.orz.po.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.id = ?2")
    int updatePasswordById(String password, Integer id);
    @Transactional
    @Modifying
    @Query("update User u set u.nickname = ?1 where u.id = ?2")
    int updateNicknameById(String nickname, Integer id);
    @Transactional
    @Modifying
    @Query("update User u set u.type = ?1 where u.id = ?2")
    int updateType(String type, Integer id);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}