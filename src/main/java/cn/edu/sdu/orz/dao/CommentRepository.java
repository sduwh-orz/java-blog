package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
