package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.LikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Integer> {
    @Query("select lh from LikeHistory lh where lh.article = ?1 and lh.ip = ?2")
    List<LikeHistory> getArticlesListByArticleAndIp(Integer article, String ip);

    @Transactional
    @Modifying
    @Query(value = "insert into like_history(article, ip) values(?1, ?2);", nativeQuery = true)
    int addArticleAndIpToLikeHistory(Integer article, String ip);
}
