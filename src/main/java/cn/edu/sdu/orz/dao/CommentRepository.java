package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.Comment;
import cn.edu.sdu.orz.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c from Comment c " +
            "where c.article = ?1 and c.author = ?2 and c.ip = ?3 and c.content = ?4 and c.parent = ?5")
    Comment findByArticleAndAuthorAndIpAndContentAndParent(Article article, User author, String ip, String content, Comment parent);
    @Transactional
    @Modifying
    @Query("update Comment c set c.status = ?1 where c.id = ?2")
    void updateStatusById(String status, Integer id);
    @Query("select c from Comment c where c.article = ?1 and c.author = ?2 and c.ip = ?3 and c.content = ?4")
    Comment findByIdAndAuthorAndIpAndContent(Article article, User author, String ip, String content);
}
