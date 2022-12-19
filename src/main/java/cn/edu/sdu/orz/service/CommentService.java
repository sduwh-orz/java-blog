package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByArticleId(Integer id);
}
