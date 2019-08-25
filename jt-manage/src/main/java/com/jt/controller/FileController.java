package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.EasyUIImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 如果用户使用文件上传,重定向到上传页面
 */
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * SpringMVC负责流操作API
     * 1.准备文件路径
     * 2.准备文件名称后后缀 abc.jpg
     *
     * @return
     */
    @RequestMapping("file")
    public String file(MultipartFile multipartFile){
        File dirFile = new File("D:/1-JT/jt-image");
        if (!dirFile.exists()){
            //如果文件不存在需要新建文件夹/目录
            dirFile.mkdirs();
        }

        //动态获取文件名称
        String fileName = multipartFile.getOriginalFilename();
        File file = new File("D:/1-JT/jt-image/"+fileName);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/file.jsp";
    }

    @RequestMapping("pic/upload")
    @ResponseBody
    public EasyUIImage uploadFile(MultipartFile uploadFile){
        return fileService.uploadFile(uploadFile);
    }

}
