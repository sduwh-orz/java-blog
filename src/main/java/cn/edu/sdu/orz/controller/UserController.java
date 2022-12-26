package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.dao.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

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
        if(session.getAttribute("user") != null) {
            return new SimpleResponse(false, "You've already logged in");
        }
        User user = userService.getUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user.getId());
            return new SimpleResponse(true, "");
        } else {
            return new SimpleResponse(false, "Wrong username or password");
        }
    }

    @GetMapping(path="/logout")
    public ApiResponse logout(HttpSession session) {
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
            return new SimpleResponse(true, "");
        } else {
            return new SimpleResponse(false, "Not logged in");
        }
    }

    @GetMapping(path="/info")
    public DataResponse info(HttpSession session, @RequestParam(required = false) String username) {
        if(session.getAttribute("user") != null) {
            if(username != null) {
                User user = userService.getUser(username);
                if (user != null) {
                    return new DataResponse(true, "", new UserInfo(user.getUsername(), user.getNickname(),
                            user.getEmail(), user.getType()));
                } else {
                    return new DataResponse(false, "Can't find user with username " + username, null);
                }
            }
            else {
                User user = userService.getUser((Integer) session.getAttribute("user"));
                if (user != null) {
                    return new DataResponse(true, "", new UserInfo(user.getUsername(), user.getNickname(),
                            user.getEmail(), user.getType()));
                }
            }
        }
        return new DataResponse(false, "Not logged in", null);
    }

    @PostMapping(path="/modify")
    public ApiResponse modify(HttpSession session, @RequestParam String username, @RequestParam(required = false) String password,
                              @RequestParam(required = false) String nickname, @RequestParam(required = false) String type) {
        if(session.getAttribute("user") != null) {
            User admin = userService.getUser((Integer) session.getAttribute("user"));
            User user = userService.getUser(username);
            if(user == null) {
                return new SimpleResponse(false, "Can't find user with username " + username);
            }
            if(!(admin.getType().equals("admin")) && !admin.getUsername().equals(user.getUsername())) {
                return new SimpleResponse(false, "You're not admin or you can't modify others' information");
            }
            else {
                if(password != null) {
                    userRepository.updatePasswordById(User.getHashedPassword(password), user.getId());
                }
                if(nickname != null) {
                    userRepository.updateNicknameById(nickname, user.getId());
                }
                if(type != null) {
                    if(type.equals("user") || type.equals("author") || type.equals("admin") || type.equals("deleted")) {
                        userRepository.updateType(type, user.getId());
                    }
                    else {
                        return new SimpleResponse(false, "Not a valid type");
                    }
                }
                return new SimpleResponse(true, "Modify successfully");
            }
        }
        return new SimpleResponse(false, "Not logged in");
    }

    @GetMapping(path="/delete")
    public DataResponse delete(HttpSession session, @RequestParam String username) {
        if(session.getAttribute("user") != null) {
            User admin = userService.getUser((Integer) session.getAttribute("user"));
            if(!(admin.getType().equals("admin"))) {
                return new DataResponse(false, "You're not admin", "null");
            }
            else {
                User user = userService.getUser(username);
                if(user == null || user.getType().equals("deleted")) {
                    return new DataResponse(false, "Can't delete user who doesn't exist!", "null");
                }
                if(user.getUsername().equals(admin.getUsername())) {
                    return new DataResponse(false, "Can't delete yourself!", "null");
                }
                if(userService.deleteUser(username)) {
                    return new DataResponse(true, "Delete user with username " +
                            username + " successfully!", "null");
                }
            }
        }
        return new DataResponse(false, "Not logged in", "null");
    }
}
