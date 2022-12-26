package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.TagArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagArticleRepository extends JpaRepository<TagArticle, Integer> {
    @Query(value = "select tag from tag_article where article = ?1", nativeQuery = true)
    List<Integer> getTagsByArticle(Integer article);

    @Query(value = "select article from tag_article where tag = ?1", nativeQuery = true)
    List<Integer> getArticlesByTag(Integer tag);
}
