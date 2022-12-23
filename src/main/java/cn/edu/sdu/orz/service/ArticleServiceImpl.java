package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article getArticle(Integer id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean createArticle(User author, String title, String summary, String content, String password) {
        try {
            Article prevArticle = articleRepository.findByAuthorAndTitle(author, title);
            if (prevArticle != null) {
                // There are articles with the specified author and the specified title.
                if (prevArticle.getStatus().equals("deleted")) {
                    articleRepository.updateStatus("normal", prevArticle.getId());
                    prevArticle.setSummary(summary);
                    prevArticle.setContent(content);
                    prevArticle.setPassword(password);
                    prevArticle.setView(0);
                    if (password.equals("")) {
                        prevArticle.setStatus("normal");
                    } else {
                        prevArticle.setStatus("hidden");
                    }
                    articleRepository.save(prevArticle);
                    return true;
                } else {
                    // The author isn't allowed to have articles with the same name.
                    return false;
                }
            } else {
                // Create a new article.
                Article article = new Article();
                article.setAuthor(author);
                article.setTitle(title);
                article.setSummary(summary);
                article.setContent(content);
                article.setPassword(password);
                article.setView(0);
                if (password.equals("")) {
                    article.setStatus("normal");
                } else {
                    article.setStatus("hidden");
                }
                articleRepository.save(article);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteArticle(User author, String title) {
        try {
            Article article = articleRepository.findByAuthorAndTitle(author, title);
            if (article != null) {
                if (article.getStatus().equals("deleted")) {
                    return false;
                }
                articleRepository.updateStatus("deleted", article.getId());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean likeArticle() {
        return null;
    }
}
