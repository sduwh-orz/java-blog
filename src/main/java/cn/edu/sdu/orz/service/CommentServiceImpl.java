package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.CommentRepository;
import cn.edu.sdu.orz.po.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByArticleId(Integer id) {
        List<Comment> all = commentRepository.findAll();
        List<Comment> sel = new ArrayList<>();
        for(Comment c: all)
        {
            if(Objects.equals(c.getArticle().getId(), id))
                sel.add(c);
        }
        return sel;
    }
}
