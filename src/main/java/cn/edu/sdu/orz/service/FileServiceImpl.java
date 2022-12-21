package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.FileRepository;
import cn.edu.sdu.orz.po.File;
import cn.edu.sdu.orz.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileRepository fileRepository;

    @Override
    public File findFile(Integer id) {
        return fileRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean createFile(User user, String type, String parentArticleId) {
        File file = new File();
        file.setName("");
        file.setUserId(user.getId());
        file.setType(type);
        file.setPath("/" + parentArticleId);
        try {
            fileRepository.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
