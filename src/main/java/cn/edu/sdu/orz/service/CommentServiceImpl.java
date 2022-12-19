package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.dao.CommentRepository;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.edu.sdu.orz.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Comment> getCommentsByArticleId(Integer id) {
        List<Comment> all = commentRepository.findAll();
        List<Comment> sel = new ArrayList<>();
        for (Comment c : all) {
            if (Objects.equals(c.getArticle().getId(), id))
                sel.add(c);
        }
        return sel;
    }

    @Override
    public Boolean createComment(User user, Integer id, String ipAddr, String content) {
        try {
            Integer authorId = user.getId();
            Article article = articleRepository.findById(id).orElse(null);
            if (article == null) {
                return false;
            }
            //check if exists a comment which authorId equals, ip equals,
            // content equals and type equals "deleted"
            Comment prevComment = commentRepository.findByIdAndAuthorAndIpAndContent(article, user, ipAddr, content);
            if (prevComment != null) {
                if (prevComment.getStatus().equals("deleted")) {
                    commentRepository.updateStatusById("normal", prevComment.getId());
                    return true;
                } else {
                    return false;
                }
            }

            Comment comment = new Comment();
            comment.setAuthor(user);
            comment.setArticle(article);
            comment.setIp(ipAddr);
            comment.setContent(content);
            comment.setStatus("normal");
            comment.setAuthorName(user.getNickname());
            comment.setEmail(user.getEmail());
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean createComment(User user, Integer articleId, String ipAddr, String content, Integer commentId) {
        try {
            Integer authorId = user.getId();
            Article article = articleRepository.findById(commentId).orElse(null);
            Comment parentComment = commentRepository.findById(commentId).orElse(null);
            if (article == null) {
                return false;
            }
            //check if exists a comment which authorId equals, ip equals,
            // content equals, parents equals and type equals "deleted"
            Comment prevComment = commentRepository.findByArticleAndAuthorAndIpAndContentAndParent(article, user, ipAddr, content, parentComment);
            if (prevComment != null) {
                if (prevComment.getStatus().equals("deleted")) {
                    commentRepository.updateStatusById("normal", prevComment.getId());
                    return true;
                } else {
                    return false;
                }
            }

            Comment comment = new Comment();
            comment.setAuthor(user);
            comment.setArticle(article);
            comment.setIp(ipAddr);
            comment.setContent(content);
            comment.setStatus("normal");
            comment.setAuthorName(user.getNickname());
            comment.setEmail(user.getEmail());
            comment.setParent(parentComment);
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean modifyComment(User user, Integer id, String content) {
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment == null) {
                return false;
            }
            if (!comment.getAuthor().getId().equals(user.getId())) {
                return false;
            }
            commentRepository.updateContentById(content, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
