package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.CommentService;
import cn.edu.sdu.orz.po.Comment;

import java.util.List;
import java.util.regex.Pattern;
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

    @GetMapping(path="/get")
    public DataResponse get(@RequestParam String articleId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(articleId).matches())) {
            return new DataResponse(false, "not a valid articleId", null);
        }
        Integer id = Integer.valueOf(articleId);
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
                              @RequestParam String ipAddr, @RequestParam String content,
                              @RequestParam(required = false) String parent) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(articleId).matches())) {
            return new DataResponse(false, "not a valid articleId", null);
        }
        Integer article = Integer.valueOf(articleId);
        Integer comment;
        if(parent != null) {
            comment = Integer.valueOf(parent);
        }
        else {
            comment = null;
        }
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if (comment != null) {
                    if (commentService.createComment(user, article, ipAddr, content, comment)) {
                        return new SimpleResponse(true, "");
                    } else {
                        return new SimpleResponse(false, "Exists a same comment which created by you");
                    }
                }
                else {
                    if (commentService.createComment(user, article, ipAddr, content)) {
                        return new SimpleResponse(true, "");
                    } else {
                        return new SimpleResponse(false, "Exists a same comment which created by you");
                    }
                }
            }
        }
        return new SimpleResponse(false, "not logged in");
    }

    @PostMapping(path="/modify")
    public ApiResponse modify(HttpSession session, @RequestParam String commentId,
                              @RequestParam String content) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(commentId).matches())) {
            return new DataResponse(false, "not a valid commentId", null);
        }
        Integer id = Integer.valueOf(commentId);
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if(commentService.modifyComment(user, id, content)) {
                    return new SimpleResponse(true, "");
                }
                else {
                    return new SimpleResponse(false, "You are not the author of this comment or the comment doesn't exist");
                }
            }
        }
        return new SimpleResponse(false, "not logged in");
    }

    @GetMapping(path="/delete")
    public DataResponse delete(HttpSession session, @RequestParam String commentId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(commentId).matches())) {
            return new DataResponse(false, "not a valid commentId", null);
        }
        Integer id = Integer.valueOf(commentId);
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if(commentService.deleteComment(user, id)) {
                    return new DataResponse(true, "delete successfully", "");
                }
                else {
                    return new DataResponse(false, "You are not the author of this comment or article or the comment doesn't exist", null);
                }
            }
        }
        return new DataResponse(false, "not logged in", null);
    }

    @GetMapping(path="/like")
    public DataResponse like(@RequestParam String commentId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(commentId).matches())) {
            return new DataResponse(false, "not a valid commentId", null);
        }
        Integer id = Integer.valueOf(commentId);
        if(commentService.likeComment(id)) {
            return new DataResponse(true, "like successfully", "");
        }
        return new DataResponse(false, "like failed", null);
    }
}
