package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.dao.TagArticleRepository;
import cn.edu.sdu.orz.dao.TagRepository;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.po.Tag;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagArticleRepository tagArticleRepository;

    @Override
    public Article getArticle(Integer id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean createArticle(User author, String title, String summary,
                                 String content, String password, Set<String> tagNames) {
        try {
            Article article = articleRepository.findByAuthorAndTitle(author, title);
            if (article == null || article != null && article.getStatus().equals("deleted")) {
                if (article == null) {
                    article = new Article();
                }
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
                if (tagNames != null) {
                    Set<Tag> tagSet = new LinkedHashSet<>();
                    Iterator<String> stringIterator = tagNames.iterator();
                    while (stringIterator.hasNext()) {
                        String s = stringIterator.next();
                        if (tagRepository.findByName(s) == null) {
                            return false;
                        }
                        tagSet.add(tagRepository.findByName(s));
                    }
                    article.setTags(tagSet);
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
    public Boolean deleteArticle(User operator, Integer articleId) {
        try {
            Optional<Article> article = articleRepository.findById(articleId);
            if (article.isPresent()) {
                if (!Objects.equals(operator.getType(), "admin") &&
                        !Objects.equals(operator.getId(), article.get().getAuthor().getId())) {
                    return false;
                }
                if (article.get().getStatus().equals("deleted")) {
                    return false;
                }
                articleRepository.updateStatus("deleted", article.get().getId());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
