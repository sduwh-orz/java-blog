package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.FileRepository;
import cn.edu.sdu.orz.po.File;
import cn.edu.sdu.orz.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

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

    @Override
    public Boolean uploadFile(User user, MultipartFile file, Integer fileId) {
        File findFile = fileRepository.findById(fileId).orElse(null);
        if(!Objects.equals(findFile.getUserId(), user.getId())) {
            return false;
        }
        String[] fileName = file.getOriginalFilename().split("\\.");
        fileRepository.updateMd5ById(File.getHashedName(fileName[0]), fileId);
        fileRepository.updateNameById(fileName[0] + "." + fileName[1], fileId);
        return true;
    }

    @Override
    public Boolean modifyName(File foundedFile, String newFileName) {
        String[] fileName = newFileName.split("\\.");
        if(fileRepository.existsByMd5(File.getHashedName(fileName[0]))) {
            return false;
        }
        fileRepository.updateMd5ById(File.getHashedName(fileName[0]), foundedFile.getId());
        fileRepository.updateNameById(fileName[0] + "." + fileName[1], foundedFile.getId());
        return true;
    }

    @Override
    public Boolean modifyFile(File foundedFile, MultipartFile newFile) {
        String type = newFile.getContentType().contains("image") ? "image" : "file";
        if(!type.equals(foundedFile.getType())) {
            return false;
        }
        if(foundedFile.getName().equals(newFile.getOriginalFilename())) {
            return false;
        }
        String newFileMd5 = File.getHashedName(newFile.getOriginalFilename().split("\\.")[0]);
        if(fileRepository.existsByMd5(newFileMd5)) {
            return false;
        }
        String[] fileName = newFile.getOriginalFilename().split("\\.");
        fileRepository.updateMd5ById(File.getHashedName(fileName[0]), foundedFile.getId());
        fileRepository.updateNameById(fileName[0] + "." + fileName[1], foundedFile.getId());
        return true;
    }
}
