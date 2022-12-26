package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);

    @Transactional
    @Modifying
    @Query("update Tag t set t.status = ?1 where t.id = ?2")
    int updateStatus(String status, Integer id);

    @Query(value = "select article from tag_article where tag = ?1", nativeQuery = true)
    List<Integer> getArticles(Integer tag);

    @Transactional
    @Modifying
    @Query(value = "delete from tag_article where tag = ?1", nativeQuery = true)
    int deleteByTag(Integer tag);

    @Transactional
    @Modifying
    @Query(value = "delete from tag where id = ?1", nativeQuery = true)
    void deleteById(Integer id);
}
