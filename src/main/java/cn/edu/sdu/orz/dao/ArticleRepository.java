package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}