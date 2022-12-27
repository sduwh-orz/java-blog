package cn.edu.sdu.orz.controller;

import cn.edu.sdu.orz.api.DataResponse;
import cn.edu.sdu.orz.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@CrossOrigin
@RequestMapping(path = "/uploads")
public class UploadsController {
    @Autowired
    private CORSFilter corsFilter;

    @GetMapping(path = "")
    public DataResponse download(@RequestParam String path, HttpServletResponse response, HttpSession session) {
        if(session.getAttribute("user") == null) {
            return new DataResponse(false, "not logged in", null);
        }
        if(!path.contains("uploads")) {
            return new DataResponse(false, "not a valid path", null);
        }
        try {
            InputStream inputStream = new FileInputStream(path);
            response.reset();
            response.setContentType("application/octet-stream");
            String filename = new File(path).getName();
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));;
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            return new DataResponse(true, "download successfully", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataResponse(false, "download failed", null);
    }
}
