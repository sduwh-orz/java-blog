package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Comment;
import cn.edu.sdu.orz.po.User;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByArticleId(Integer id);

    Boolean createComment(User user, Integer id, String ipAddr, String content);

    Boolean createComment(User user, Integer articleId, String ipAddr, String content, Integer commentId);

    Boolean modifyComment(User user, Integer id, String content);

    Boolean deleteComment(User user, Integer id);

    Boolean likeComment(Integer id);
}
