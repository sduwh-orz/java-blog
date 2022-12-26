package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.Tag;
import cn.edu.sdu.orz.po.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface ArticleService {
    Article getArticle(Integer id);
    Boolean createArticle(User author, String title, String summary,
                          String content, String password, Set<String> tagNames);
    Boolean deleteArticle(User author, String title);
}
