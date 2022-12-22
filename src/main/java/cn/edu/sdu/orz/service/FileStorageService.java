package cn.edu.sdu.orz.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void upload(MultipartFile file, String parentPath);
    public Resource get(String filename, String parentPath);
    public void delete(String filename, String parentPath);
}
