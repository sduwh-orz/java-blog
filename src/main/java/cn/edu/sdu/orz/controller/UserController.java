package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.api.UserInfo;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private CORSFilter corsFilter;
    @Autowired
    private UserService userService;

    @PostMapping(path="/register")
    public ApiResponse register(@RequestParam String username, @RequestParam String password, @RequestParam String nickname,
                                @RequestParam String email) {
        if (userService.createUser(username, password, nickname, email))
            return new SimpleResponse(true, "");
        else
            return new SimpleResponse(false, "Server error");
    }

    @PostMapping(path="/login")
    public ApiResponse login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.getUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user.getId());
            return new SimpleResponse(true, "");
        } else {
            return new SimpleResponse(false, "用户名或密码错误");
        }
    }

    @PostMapping(path="/logout")
    public ApiResponse logout(HttpSession session) {
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
            return new SimpleResponse(true, "");
        } else {
            return new SimpleResponse(false, "未登录");
        }
    }

    @GetMapping(path="/info")
    public DataResponse info(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            if (user != null) {
                return new DataResponse(true, "", new UserInfo(user.getUsername(), user.getNickname(),
                        user.getEmail(), user.getType()));
            }
        }
        return new DataResponse(false, "Not logged in", null);
    }
}
