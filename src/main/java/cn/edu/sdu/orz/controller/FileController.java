package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.dao.FileRepository;
import cn.edu.sdu.orz.dao.UserRepository;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.File;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.FileService;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping(path = "/file")
public class FileController {

    @Autowired
    private CORSFilter corsFilter;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/get")
    public DataResponse get(HttpSession session, @RequestParam String fileId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(fileId).matches())) {
            return new DataResponse(false, "not a valid fileId", null);
        }
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            Integer id = Integer.valueOf(fileId);
            File file = fileService.findFile(id);
            if(file != null) {
                return new DataResponse(true, "", file.getMd5());
            }
            return new DataResponse(false, "cannot find the file", null);
        }
        return new DataResponse(false, "not logged in", null);
    }

    @PostMapping(path = "/create")
    public ApiResponse create(HttpSession session, @RequestParam String type,
                              @RequestParam String parentArticleId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(parentArticleId).matches())) {
            return new SimpleResponse(false, "not a valid articleId");
        }
        if((!type.equals("image")) && !(type.equals("file"))) {
            return new SimpleResponse(false, "not a valid file type");
        }
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if(fileService.createFile(user, type, parentArticleId)) {
                return new SimpleResponse(true, "create successfully");
            }
            return new SimpleResponse(false, "create failed");
        }
        return new SimpleResponse(false, "not logged in");
    }
}
