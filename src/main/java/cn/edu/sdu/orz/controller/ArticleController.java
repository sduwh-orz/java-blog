package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.*;
import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.dao.UserRepository;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.ArticleService;
import cn.edu.sdu.orz.service.ArticleServiceImpl;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private CORSFilter corsFilter;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private  UserRepository userRepository;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(path = "/get/{articleId}")
    public DataResponse getInfo(@PathVariable("articleId") Integer id,
                                @RequestParam(required = false, defaultValue = "") String password,
                                HttpSession session) {
        Article foundArticle = articleService.getArticle(id);
        if (foundArticle == null || foundArticle.getStatus().equals("deleted")) {
            return new DataResponse(false, "This article doesn't exist.", null);
        }
        if (session.getAttribute("user") == null) {
            if (foundArticle.getStatus().equals("normal")) {
                articleRepository.updateView(id);
                foundArticle.setView(foundArticle.getView() + 1);
                return new DataResponse(true, null, new ArticleInfo(
                        foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                        foundArticle.getModified(), foundArticle.getView(),
                        foundArticle.getSummary(), foundArticle.getContent())
                );
            } else if (foundArticle.getStatus().equals("hidden")) {
                if (password.equals(foundArticle.getPassword())) {
                    articleRepository.updateView(id);
                    foundArticle.setView(foundArticle.getView() + 1);
                    return new DataResponse(true, null, new ArticleInfo(
                            foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                            foundArticle.getModified(), foundArticle.getView(),
                            foundArticle.getSummary(), foundArticle.getContent())
                    );
                } else {
                    return new DataResponse(false, "Password error.", null);
                }
            }
        }
        User user = userService.getUser((Integer) session.getAttribute("user"));
        if (foundArticle.getStatus().equals("normal")) {
            if (!user.getType().equals("deleted")) {
                if (!user.getUsername().equals(foundArticle.getAuthor().getUsername())) {
                    articleRepository.updateView(id);
                    foundArticle.setView(foundArticle.getView() + 1);
                }
                return new DataResponse(true, null, new ArticleInfo(
                        foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                        foundArticle.getModified(), foundArticle.getView(),
                        foundArticle.getSummary(), foundArticle.getContent())
                );
            }
        } else if (foundArticle.getStatus().equals("hidden")) {
            if (user.getType().equals("admin") || user.getUsername().equals(foundArticle.getAuthor().getUsername())) {
                if (user.getType().equals("admin")) {
                    articleRepository.updateView(id);
                    foundArticle.setView(foundArticle.getView() + 1);
                }
                return new DataResponse(true, null, new ArticleInfo(
                        foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                        foundArticle.getModified(), foundArticle.getView(),
                        foundArticle.getSummary(), foundArticle.getContent())
                );
            } else {
                if (!password.equals(foundArticle.getPassword())) {
                    return new DataResponse(false, "Password error.", null);
                } else {
                    articleRepository.updateView(id);
                    foundArticle.setView(foundArticle.getView() + 1);
                    return new DataResponse(true, null, new ArticleInfo(
                            foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                            foundArticle.getModified(), foundArticle.getView(),
                            foundArticle.getSummary(), foundArticle.getContent())
                    );
                }
            }
        }
        return new DataResponse(false, "You don't have permission to view this article.", null);
    }

    @PostMapping("/create")
    public ApiResponse create(@RequestParam String title, @RequestParam String summary, @RequestParam String content,
                              @RequestParam String password, HttpSession session) {
        // Non-logged-in users aren't allowed to create an article.
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "Non-logged-in users aren't allowed to create an article.");
        } else {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if (!user.getType().equals("deleted") &&
                    articleService.createArticle(user, title, summary, content, password)) {
                userRepository.updateType("author", user.getId());
                return new SimpleResponse(true, "");
            }
        }
        return new SimpleResponse(false, "Server error.");
    }

    @PostMapping("/modify")
    public ApiResponse modify() {
        return new SimpleResponse(true, "");
    }

    @PostMapping("/delete")
    public ApiResponse delete(@RequestParam String authorName, @RequestParam String title, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "Non-logged-in users can't delete this article.");
        }
        User user = userService.getUser((Integer) session.getAttribute("user"));
        if (user.getType().equals("user") || user.getType().equals("deleted")) {
            return new SimpleResponse(false, "You don't have permission to delete this article.");
        } else if (user.getType().equals("admin")) {
            if (userService.getUser(authorName) == null) {
                return new SimpleResponse(false, "This author doesn't exist.");
            } else {
                if (articleService.deleteArticle(userService.getUser(authorName), title)) {
                    return new SimpleResponse(true, "");
                } else {
                    return new SimpleResponse(false, "This article doesn't exist.");
                }
            }
        } else {
            // The "author" user can delete his articles which aren't "deleted".
            if (articleService.deleteArticle(user, title)) {
                return new SimpleResponse(true, "");
            } else {
                // Other "author" users can't delete this article.
                if (articleRepository.findByAuthorAndTitle(user, title) == null) {
                    return new SimpleResponse(false, "You don't have permission to delete this article.");
                } else {
                    // The "author" user can't delete his articles which are "deleted".
                    return new SimpleResponse(false, "This article doesn't exist.");
                }
            }
        }
    }

    @PostMapping("/like")
    public ApiResponse like() {
        return new SimpleResponse(true, "");
    }

    @GetMapping("/search")
    public ApiResponse search(@RequestParam(required = false, defaultValue = "") String keyword) {
        // Merge, de duplicate and sort search results.
        List<Article> articleList = new ArrayList<>();
        Set<Article> articleSet = new HashSet<>();
        articleSet.addAll(articleRepository.getArticlesListByTitle(keyword));
        articleSet.addAll(articleRepository.getArticlesListBySummary(keyword));
        articleSet.addAll(articleRepository.getArticlesListByContent(keyword));
        articleList.addAll(articleSet);
        articleList = articleList.stream().sorted(
                Comparator.comparing(Article::getView).thenComparing(Article::getModified).reversed()
        ).collect(Collectors.toList());
        // Write articleList to articleSearchInfoList.
        List<ArticleSearchInfo> articleSearchInfoList = new ArrayList<>();
        Iterator<Article> it = articleList.iterator();
        while (it.hasNext()) {
            Article article = it.next();
            ArticleSearchInfo articleSearchInfo = new ArticleSearchInfo(
                    article.getId(), article.getTitle(), article.getAuthor().getUsername(),
                    article.getModified(), article.getView(), article.getSummary()
            );
            if (!article.getStatus().equals("deleted")) {
                articleSearchInfoList.add(articleSearchInfo);
            }
        }
        return new DataResponse(true, null, articleSearchInfoList);
    }

    @GetMapping("/recommend")
    public ApiResponse recommend() {
        return new SimpleResponse(true, "");
    }

}
