package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.CommentInfo;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.CommentService;
import cn.edu.sdu.orz.po.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.edu.sdu.orz.service.GetIPService;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private GetIPService getIPService;

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
        List<CommentInfo> output = new ArrayList<>();
        for(Comment c: lst) {
            if(!c.getStatus().equals("normal"))
                continue;
            CommentInfo commentInfo = new CommentInfo(c.getId(),
                    (c.getAuthor() != null ? c.getAuthor().getId() : null),
                    (c.getParent() != null ? c.getParent().getId() : 0),
                    c.getAuthorName(),
                    c.getEmail(),
                    c.getIp(),
                    c.getContent(),
                    c.getCreated(),
                    c.getModified(),
                    c.getLikeNum());
            output.add(commentInfo);
        }
        return new DataResponse(true, "", output);
    }

    @PostMapping(path="/create")
    public ApiResponse create(HttpSession session, HttpServletRequest request, @RequestParam String articleId,
                              @RequestParam String content, @RequestParam(required = false) String nickname,
                              @RequestParam(required = false) String email,
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
            comment = -1;
        }
        String ipAddr = getIPService.getRemoteIP(request);
        User user = null;
        if (session.getAttribute("user") != null) {
            user = userService.getUser((Integer) session.getAttribute("user"));
        }
        if (commentService.createComment(user, article, ipAddr, content, nickname, email, comment)) {
            return new SimpleResponse(true, "");
        } else {
            return new SimpleResponse(false, "Exists a same comment which created by you");
        }
    }

    @PostMapping(path="/modify")
    public ApiResponse modify(HttpSession session, @RequestParam String commentId,
                              @RequestParam(required = false) String content, @RequestParam(required = false) String status) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(commentId).matches())) {
            return new DataResponse(false, "not a valid commentId", null);
        }
        Integer id = Integer.valueOf(commentId);
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(user != null) {
                if(status == null && content == null) {
                    return new SimpleResponse(false, "Invalid modify");
                }
                if(content != null) {
                    if(commentService.modifyComment(user, id, content)) {
                        return new SimpleResponse(true, "modify content successfully");
                    }
                    else {
                        return new SimpleResponse(false, "You are not the author of this comment/article or you're not admin or the comment doesn't exist");
                    }
                }
                if(status != null) {
                    if(status.equals("normal") || status.equals("hidden") || status.equals("deleted")) {
                        if(commentService.modifyCommentByStatus(user, id, status)) {
                            return new SimpleResponse(true, "modify status successfully");
                        }
                        else {
                            return new SimpleResponse(false, "You are not the author of this comment/article or you're not admin or the comment doesn't exist");
                        }
                    }
                    else {
                        return new SimpleResponse(false, "not a valid status");
                    }
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
