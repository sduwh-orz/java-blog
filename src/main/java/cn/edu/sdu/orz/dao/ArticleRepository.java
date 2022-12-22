package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
