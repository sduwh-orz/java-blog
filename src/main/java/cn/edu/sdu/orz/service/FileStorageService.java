package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void upload(MultipartFile file, String parentPath);
    public Resource get(String filename, String parentPath);
    public void delete(String filename, String parentPath);

    void modifyName(String newFileName, File foundedFile, String oldName);

    void modifyFile(MultipartFile newFile, File foundedFile, String oldName);
}
