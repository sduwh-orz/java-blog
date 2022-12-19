package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.CommentService;
import cn.edu.sdu.orz.po.Comment;

import java.util.List;
import java.util.stream.Collectors;

import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@CrossOrigin
@RequestMapping(path="/comment")
public class CommentController {
    @Autowired
    private CORSFilter corsFilter;
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping(path="")
    public DataResponse test() {
        return new DataResponse(true, "", null);
    }

    @GetMapping(path="/get/{articleId}")
    public DataResponse get(@PathVariable("articleId") String Id) {
        Integer id = Integer.valueOf(Id);
        List<Comment> lst = commentService.getCommentsByArticleId(id);
        if(lst.isEmpty()) {
            return new DataResponse(false, "nothing found", null);
        }
        return new DataResponse(true, "",
                //output id of comments
                lst.stream().map(Comment::getId).collect(Collectors.toList())
                );
    }
    @PostMapping(path="/create")
    public ApiResponse create(HttpSession session, @RequestParam String articleId,
                              @RequestParam String ipAddr, @RequestParam String content) {
        Integer id = Integer.valueOf(articleId);
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if(commentService.createComment(user, id, ipAddr, content)) {
                    return new SimpleResponse(true, "");
                }
                else {
                    return new SimpleResponse(false, "Exists a same comment which created by you");
                }
            }
        }
        return new SimpleResponse(false, "not logged in");
    }

    @PostMapping(path="/create/{commentId}")
    public ApiResponse create(HttpSession session, @RequestParam String articleId,
                              @RequestParam String ipAddr, @RequestParam String content,
                              @PathVariable("commentId") String Id) {
        Integer article = Integer.valueOf(articleId);
        Integer comment = Integer.valueOf(Id);
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if(commentService.createComment(user, article, ipAddr, content, comment)) {
                    return new SimpleResponse(true, "");
                }
                else {
                    return new SimpleResponse(false, "Exists a same comment which created by you");
                }
            }
        }
        return new SimpleResponse(false, "not logged in");
    }
}
