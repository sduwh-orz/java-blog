package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.*;
import cn.edu.sdu.orz.dao.*;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.*;
import cn.edu.sdu.orz.service.ArticleService;
import cn.edu.sdu.orz.service.GetIPService;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
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
    private UserRepository userRepository;
    @Autowired
    private LikeHistoryRepository likeHistoryRepository;
    @Autowired
    private GetIPService getIPService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagArticleRepository tagArticleRepository;

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
        // Get relevant tags for this article.
        Set<String> tagNames = new LinkedHashSet<>();
        Iterator<Tag> it = foundArticle.getTags().iterator();
        while (it.hasNext()) {
            Tag tag = it.next();
            tagNames.add(tag.getName());
        }
        // Randomly recommend articles with the same tag(s).
        List<Integer> tagIdList = tagArticleRepository.getTagsByArticle(id);
        List<Integer> articleIdList = new ArrayList<>();
        for (int i = 0; i < Math.min(tagIdList.size(), 3); i++) {
            Random random = new Random();
            articleIdList.add(tagIdList.get(random.nextInt(tagIdList.size())));
        }
        Iterator<Integer> iterator = articleIdList.iterator();
        Set<Integer> articleSet = new HashSet<>();
        while (iterator.hasNext()) {
            articleSet.addAll(tagArticleRepository.getArticlesByTag(iterator.next()));
        }
        articleSet.remove(id);
        List<Integer> articleList = new ArrayList<>(articleSet);
        List<Integer> recommendArticleIdList = new ArrayList<>();
        for (int i = 0; i < Math.min(articleList.size(), 3); i++) {
            recommendArticleIdList.add(articleList.get(i));
        }
        // Start working.
        if (session.getAttribute("user") == null) {
            if (foundArticle.getStatus().equals("normal")) {
                articleRepository.updateView(id);
                foundArticle.setView(foundArticle.getView() + 1);
                return new DataResponse(true, null, new ArticleInfo(
                        foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                        foundArticle.getModified(), foundArticle.getView(),
                        foundArticle.getSummary(), foundArticle.getContent(), tagNames, recommendArticleIdList)
                );
            } else if (foundArticle.getStatus().equals("hidden")) {
                if (password.equals(foundArticle.getPassword())) {
                    articleRepository.updateView(id);
                    foundArticle.setView(foundArticle.getView() + 1);
                    return new DataResponse(true, null, new ArticleInfo(
                            foundArticle.getTitle(), foundArticle.getAuthor().getUsername(),
                            foundArticle.getModified(), foundArticle.getView(),
                            foundArticle.getSummary(), foundArticle.getContent(), tagNames, recommendArticleIdList)
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
                        foundArticle.getSummary(), foundArticle.getContent(), tagNames, recommendArticleIdList)
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
                        foundArticle.getSummary(), foundArticle.getContent(), tagNames, recommendArticleIdList)
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
                            foundArticle.getSummary(), foundArticle.getContent(), tagNames, recommendArticleIdList)
                    );
                }
            }
        }
        return new DataResponse(false, "You don't have permission to view this article.", null);
    }

    @PostMapping("/create")
    public ApiResponse create(@RequestParam String title, @RequestParam String summary, @RequestParam String content,
                              @RequestParam(required = false, defaultValue = "") String password,
                              @RequestParam(required = false) Set<String> tagNames, HttpSession session) {
        // Non-logged-in users aren't allowed to create an article.
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "Non-logged-in users aren't allowed to create an article.");
        } else {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if (!user.getType().equals("deleted") &&
                    articleService.createArticle(user, title, summary, content, password, tagNames)) {
                if (!user.getType().equals("admin")) {
                    userRepository.updateType("author", user.getId());
                }
                return new SimpleResponse(true, "");
            }
        }
        return new SimpleResponse(false, "Server error.");
    }

    @PostMapping("/modify")
    public ApiResponse modify(@RequestParam Integer articleId,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String summary,
                              @RequestParam(required = false) String content,
                              @RequestParam(required = false) String password,
                              @RequestParam(required = false) Set<String> tagNames,
                              HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(
                    false, "Non-logged-in users don't have permission to modify this article."
            );
        } else {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            Optional<Article> article =
                    articleRepository.findById(articleId);
            if (article.isEmpty() || article.get().getStatus().equals("deleted")) {
                return new SimpleResponse(false, "This article doesn't exist.");
            }
            Article foundArticle = article.get();
            if (user.getType().equals("user") || user.getType().equals("deleted") ||
                    user.getType().equals("author") && !Objects.equals(user.getId(), foundArticle.getAuthor().getId())) {
                return new SimpleResponse(false, "You don't have permission to modify this article.");
            } else {
                if (title != null && title.length() != 0) {
                    foundArticle.setTitle(title);
                }
                if (summary != null && summary.length() != 0) {
                    foundArticle.setSummary(summary);
                }
                if (content != null && content.length() != 0) {
                    foundArticle.setContent(content);
                }
                foundArticle.setPassword(password);
                if (password != null) {
                    if (password.equals("")) {
                        foundArticle.setStatus("normal");
                    } else {
                        foundArticle.setStatus("hidden");
                    }
                }
                Set<Tag> tagSet = new LinkedHashSet<>();
                if (tagNames != null) {
                    Iterator<String> stringIterator = tagNames.iterator();
                    while (stringIterator.hasNext()) {
                        String s = stringIterator.next();
                        if (tagRepository.findByName(s) == null) {
                            return new SimpleResponse(false, "Some tags don't exist.");
                        }
                        tagSet.add(tagRepository.findByName(s));
                    }
                    foundArticle.setTags(tagSet);
                }
                articleRepository.save(foundArticle);
                return new SimpleResponse(true, "");
            }
        }
    }

    @PostMapping("/delete")
    public ApiResponse delete(@RequestParam Integer articleId, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "Non-logged-in users can't delete this article.");
        }
        User user = userService.getUser((Integer) session.getAttribute("user"));
        if (user.getType().equals("user") || user.getType().equals("deleted")) {
            return new SimpleResponse(false, "You don't have permission to delete this article.");
        } else {
            // The "author" user can delete his articles which aren't "deleted".
            if (articleService.deleteArticle(user, articleId)) {
                return new SimpleResponse(true, "");
            }
            return new SimpleResponse(false, "Article is not present or user cannot delete this article");
        }
    }

    @PostMapping("/like/{articleId}")
    public ApiResponse like(@Valid @PathVariable(value = "articleId") Integer article, HttpServletRequest request) {
        Article foundArticle = articleService.getArticle(article);
        if (foundArticle == null || foundArticle != null && foundArticle.getStatus().equals("deleted")) {
            return new SimpleResponse(false, "This article doesn't exist.");
        } else {
            String ip = getIPService.getRemoteIP(request);
            if (!likeHistoryRepository.getArticlesListByArticleAndIp(article, ip).isEmpty()) {
                return new SimpleResponse(false, "You have liked this article.");
            } else {
                likeHistoryRepository.addArticleAndIpToLikeHistory(article, ip);
                return new SimpleResponse(true, "");
            }
        }
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

    @GetMapping("/list")
    public ApiResponse list(HttpSession session) {

        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "You are not logged in");
        }
        User user = userService.getUser((Integer) session.getAttribute("user"));
        List<Article> articleList = new ArrayList<>();
        articleList.addAll(articleRepository.getArticlesListByAuthor(user));
        List<ArticleDetailedInfo> articleSearchInfoList = new ArrayList<>();
        Iterator<Article> it = articleList.iterator();
        while (it.hasNext()) {
            Article article = it.next();
            ArticleDetailedInfo articleSearchInfo = new ArticleDetailedInfo(
                    article.getId(), article.getTitle(), article.getAuthor().getUsername(),
                    article.getModified(), article.getCreated(), article.getView(), article.getSummary(), article.getContent()
            );
            if (!article.getStatus().equals("deleted")) {
                articleSearchInfoList.add(articleSearchInfo);
            }
        }
        return new DataResponse(true, "", articleSearchInfoList);
    }

}
