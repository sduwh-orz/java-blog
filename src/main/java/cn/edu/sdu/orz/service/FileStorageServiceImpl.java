package cn.edu.sdu.orz.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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

    @Override
    public void modifyName(String newFileName, cn.edu.sdu.orz.po.File foundedFile, String oldName) {
        FileInputStream fileInputStream;
        try {
          Path cur = Paths.get("./uploads" + foundedFile.getPath());
            fileInputStream =
                    new FileInputStream("./uploads" + foundedFile.getPath() + "/" + oldName);
          MultipartFile renamedFile = new MockMultipartFile(newFileName, fileInputStream);
          fileInputStream.close();
          Files.copy(renamedFile.getInputStream(), cur.resolve(newFileName));
          new FileStorageServiceImpl().delete(foundedFile.getName(), foundedFile.getPath());
        } catch (Exception e) {
            throw new RuntimeException("cannot modify file name");
        }
    }

    @Override
    public void modifyFile(MultipartFile newFile, cn.edu.sdu.orz.po.File foundedFile, String oldName) {
        try {
            Path cur = Paths.get("./uploads" + foundedFile.getPath());
            new FileStorageServiceImpl().delete(oldName, foundedFile.getPath());
            Files.copy(newFile.getInputStream(), cur.resolve(newFile.getOriginalFilename()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cannot modify file");
        }
    }
}
