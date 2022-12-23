package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Transactional
    @Modifying
    @Query("update Article a set a.status = ?1 where a.id = ?2")
    int updateStatus(String status, Integer id);

    @Transactional
    @Modifying
    @Query("update Article a set a.view = a.view + 1 where a.id = ?1")
    int updateView(Integer id);

    @Query("select a from Article a where a.title like concat('%', :keyword, '%')")
    List<Article> getArticlesListByTitle(@Param("keyword") String keyword);

    @Query("select a from Article a where a.summary like concat('%', :keyword, '%')")
    List<Article> getArticlesListBySummary(@Param("keyword") String keyword);

    @Query("select a from Article a where a.content like concat('%', :keyword, '%')")
    List<Article> getArticlesListByContent(@Param("keyword") String keyword);

    Article findByAuthorAndTitle(User author, String title);
}
