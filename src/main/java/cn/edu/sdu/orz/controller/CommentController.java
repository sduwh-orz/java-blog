package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.service.CommentService;
import cn.edu.sdu.orz.po.Comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping(path="/comment")
public class CommentController {
    @Autowired
    private CORSFilter corsFilter;
    @Autowired
    private CommentService commentService;

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
}
