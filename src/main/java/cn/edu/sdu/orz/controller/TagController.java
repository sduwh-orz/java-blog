package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.*;
import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.dao.TagRepository;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.po.Tag;
import cn.edu.sdu.orz.service.ArticleService;
import cn.edu.sdu.orz.service.TagService;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ApiResponse getAll() {
        List<Tag> tag = tagService.getAllTag();
        List<TagInfo> result = tag.stream().map((t)-> new TagInfo(t.getId(), t.getName(), t.getDescription(), t.getCreated())).collect(Collectors.toList());
        return new DataResponse(true, "", result);
    }
    @GetMapping("/get")
    public ApiResponse get(@RequestParam String name) {
        Tag tag = tagService.getTag(name);
        if (tag != null) {
            TagInfo info = new TagInfo(tag.getId(), tag.getName(), tag.getDescription(), tag.getCreated());
            return new DataResponse(true, "", info);
        } else {
            return new DataResponse(false, "Tag doesn't exist", null);
        }
    }
    @PostMapping("/create")
    public ApiResponse create(@RequestParam String name, @RequestParam String description, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "Non-logged-in users aren't allowed to create a tag.");
        } else {
            if (tagService.createTag(name, description)) {
                return new SimpleResponse(true, "");
            }
        }
        return new SimpleResponse(false, "Server error.");
    }

    @GetMapping("/list/{tagName}")
    public ApiResponse list(@PathVariable("tagName") String name) {
        Tag foundTag = tagRepository.findByName(name);
        if (foundTag == null) {
            return new DataResponse(false, "This tag doesn't exist.", null);
        } else {
            List<Integer> articleList = tagRepository.getArticles(foundTag.getId());
            List<ArticleSearchInfo> articleSearchInfoList = new ArrayList<>();
            Iterator<Integer> it = articleList.iterator();
            while (it.hasNext()) {
                Integer articleId = it.next();
                Article article = articleRepository.findById(articleId).orElse(null);
                ArticleSearchInfo articleSearchInfo = new ArticleSearchInfo(
                        articleId, article.getTitle(), article.getAuthor().getUsername(),
                        article.getModified(), article.getView(), article.getSummary()
                );
                articleSearchInfoList.add(articleSearchInfo);
            }
            return new DataResponse(true, "", articleSearchInfoList);
        }
    }

    @PostMapping("/delete")
    public ApiResponse delete(@RequestParam String name, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new SimpleResponse(false, "You don't have permission to delete this tag.");
        } else {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if (!user.getType().equals("admin")) {
                return new SimpleResponse(false, "You don't have permission to delete this tag.");
            } else {
                Tag foundTag = tagRepository.findByName(name);
                if (foundTag == null) {
                    return new SimpleResponse(false, "This tag doesn't exist.");
                } else {
                    Integer tagId = foundTag.getId();
                    List<Integer> articleIdList = tagRepository.getArticles(tagId);
                    Iterator<Integer> it = articleIdList.iterator();
                    while (it.hasNext()) {
                        Article article = articleService.getArticle(it.next());
                        Set<Tag> set = article.getTags();
                        set.remove(foundTag);
                        article.setTags(set);
                        articleRepository.save(article);
                    }
                    tagRepository.deleteByTag(tagId);
                    tagRepository.updateStatus("deleted", tagId);
                    return new SimpleResponse(true, "");
                }
            }
        }
    }
}
