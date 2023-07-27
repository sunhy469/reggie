package com.sunhy.controller;

import com.sunhy.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 文件上传和下载
 * @Version 1.0
 */

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    //文件上传
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//这个参数名宁字file 与前端请求保持一致
        //file是一个临时文件
        log.info("图片"+file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //文件后缀名
        assert originalFilename != null;
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));
        //随机生成文件名
        String fileName = UUID.randomUUID()+suffix;

        File dir = new File(basePath);
        if (!dir.exists())
            dir.mkdirs();
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try( FileInputStream inputStream = new FileInputStream(new File(basePath + name));
             ServletOutputStream outputStream = response.getOutputStream();) {
            //输入流读取文件内容
            //输出流写回浏览器
            response.setContentType("image/jpeg");
            int length=0;
            byte[] bytes = new byte[1024];
            while ((length=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
