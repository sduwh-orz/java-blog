package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.ApiResponse;
import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.api.SimpleResponse;
import cn.edu.sdu.orz.api.fileInfo;
import cn.edu.sdu.orz.dao.ArticleRepository;
import cn.edu.sdu.orz.dao.FileRepository;
import cn.edu.sdu.orz.filter.CORSFilter;
import cn.edu.sdu.orz.po.Article;
import cn.edu.sdu.orz.po.File;
import cn.edu.sdu.orz.po.User;
import cn.edu.sdu.orz.service.FileService;
import cn.edu.sdu.orz.service.FileStorageService;
import cn.edu.sdu.orz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping(path = "/get")
    public DataResponse get(HttpSession session, @RequestParam String fileId) {
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            if(!(pattern.matcher(fileId).matches())) {
                return new DataResponse(false, "not a valid fileId", null);
            }
            if(session.getAttribute("user") != null) {
                Integer id = Integer.valueOf(fileId);
                File file = fileService.findFile(id);
                if(file != null) {
                    return new DataResponse(true, "", new fileInfo(
                            file.getName(), file.getType(),
                            userService.getUser(file.getUserId()).getUsername(),
                            fileStorageService.get(file.getName(), file.getPath()).getURL()
                    ));
                }
                return new DataResponse(false, "cannot find the file", null);
            }
            return new DataResponse(false, "not logged in", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataResponse(false, "cannot open the file", null);
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

    @PostMapping(path = "/upload")
    public ApiResponse upload(HttpSession session, @RequestParam MultipartFile file,
                              @RequestParam String fileId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(fileId).matches())) {
            return new SimpleResponse(false, "not a valid fileId");
        }
        String type = (file.getContentType().contains("image")) ? "image" : "file";
       if(session.getAttribute("user") != null) {
           User user = userService.getUser((Integer) session.getAttribute("user"));
           Integer id = Integer.valueOf(fileId);
           File foundedFile = fileRepository.findById(id).orElse(null);
           if(foundedFile == null) {
               return new SimpleResponse(false, "fileId doesn't exist");
           }
           if(!foundedFile.getType().equals(type)) {
               return new SimpleResponse(false, "type not equals");
           }
           if(fileService.uploadFile(user, file, id)) {
               fileStorageService.upload(file, foundedFile.getPath());
               return new SimpleResponse(true, "upload successfully");
           }
           return new SimpleResponse(false, "you're not the uploader of this file");
       }
       return new SimpleResponse(false, "not logged in");
    }

    @GetMapping(path = "/delete")
    public DataResponse delete(HttpSession session, @RequestParam String fileId) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(fileId).matches())) {
            return new DataResponse(false, "not a valid fileId", null);
        }
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            File foundedFile = fileRepository.findById(Integer.valueOf(fileId)).orElse(null);
            if(user.getType().equals("admin") || Objects.equals(foundedFile.getUserId(), user.getId())) {
                if(foundedFile == null) {
                    return new DataResponse(false, "cannot find the file", null);
                }
                fileStorageService.delete(foundedFile.getName(), foundedFile.getPath());
                fileRepository.deleteById(Integer.valueOf(fileId));
                return new DataResponse(true, "delete successfully", "");
            }
            return new DataResponse(false, "you're not an admin", null);
        }
        return new DataResponse(false, "not logged in", null);
    }

    @PostMapping(path = "/modify")
    public ApiResponse modify(HttpSession session, @RequestParam String fileId, @RequestParam(required = false) String newFileName,
                              @RequestParam(required = false) MultipartFile newFile) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!(pattern.matcher(fileId).matches())) {
            return new DataResponse(false, "not a valid fileId", null);
        }
        if(session.getAttribute("user") != null) {
            User user = userService.getUser((Integer) session.getAttribute("user"));
            File foundedFile = fileRepository.findById(Integer.valueOf(fileId)).orElse(null);
            if(foundedFile == null) {
                return new SimpleResponse(false, "cannot find the file");
            }
            if(user.getType().equals("admin") || Objects.equals(user.getId(), foundedFile.getUserId())) {
                if(newFileName != null && newFile != null) {
                    return new SimpleResponse(false, "you can't modify both file and filename");
                }
                if(newFileName != null) {
                    String oldName = foundedFile.getName();
                    if(fileService.modifyName(foundedFile, newFileName)) {
                        fileStorageService.modifyName(newFileName, foundedFile, oldName);
                    }
                    else {
                        return new SimpleResponse(false, "filename duplicate");
                    }
                }
                if(newFile != null) {
                    String oldName = foundedFile.getName();
                    if(fileService.modifyFile(foundedFile, newFile)) {
                        fileStorageService.modifyFile(newFile, foundedFile, oldName);
                    }
                    else {
                        return new SimpleResponse(false, "type not equal or file duplicate");
                    }
                }
                return new SimpleResponse(true, "modify successfully");
            }
            return new SimpleResponse(false, "you're neither admin nor this file's uploader");
        }
        return new SimpleResponse(false, "not logged in");
    }
}
