package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;

import java.util.List;

public interface ArticleService {
    Article getArticle(Integer id);
    Boolean createArticle(User author, String title, String summary, String content, String password);
    Boolean deleteArticle(User author, String title);
    Boolean likeArticle();
}
