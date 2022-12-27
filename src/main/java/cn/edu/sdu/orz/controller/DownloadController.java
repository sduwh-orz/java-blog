package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

@RestController
@CrossOrigin
@RequestMapping(path = "/download")
public class DownloadController {
    @Autowired
    private CORSFilter corsFilter;

    @GetMapping(path = "")
    public void download(@RequestParam String path, HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("user") == null) {
            throw new RuntimeException("Not logged in");
        }
        File file = new File(path);
        byte[] buffer = new byte[1024];
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        try {
            if(file.exists()) {
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                String filename = new File(path).getName();
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));;
                outputStream = response.getOutputStream();
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                while(bufferedInputStream.read(buffer) != -1) {
                    outputStream.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if(outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
