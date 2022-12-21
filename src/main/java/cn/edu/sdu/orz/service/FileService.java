package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.File;
import cn.edu.sdu.orz.po.User;

public interface FileService {
    File findFile(Integer id);

    Boolean createFile(User user, String type, String parentArticleId);
}
