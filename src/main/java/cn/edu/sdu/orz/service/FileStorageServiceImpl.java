package cn.edu.sdu.orz.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public void upload(MultipartFile file, String parentPath) {
        try {
            Path cur = Paths.get("./uploads" + parentPath);
            File f = new File("./uploads" + parentPath + "/" + file.getOriginalFilename());
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            Files.copy(file.getInputStream(), cur.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if(e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists");
            }
        }
    }

    @Override
    public Resource get(String filename, String parentPath) {
        try {
            Path cur = Paths.get("./uploads" + parentPath);
            Path file = cur.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String filename, String parentPath) {
        try {
            Path cur = Paths.get("./uploads" + parentPath);
            FileSystemUtils.deleteRecursively(cur.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file");
        }
    }
}
