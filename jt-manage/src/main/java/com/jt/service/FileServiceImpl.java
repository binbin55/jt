package com.jt.service;

import com.jt.vo.EasyUIImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

    @Value("${image.localFileDir}")
    private String localFileDir;

    @Value("${image.urlPath}")
    private String urlPath;

    /**
     * 1.判断文件是否为图片  jpg|png|gif
     * 2.防止恶意程序上传   判断图片固有属性 宽度和高度
     * 3.将图片分目录存储   按照时间进行存储 yyyy/MM//dd
     * 4.解决文件重名问题   UUID
     * @param uploadFile
     * @return
     */
    @Override
    public EasyUIImage uploadFile(MultipartFile uploadFile) {
        EasyUIImage uiImage = new EasyUIImage();
        //1.获取图片名称  abc.jpg
        String fileName = uploadFile.getOriginalFilename().toLowerCase();
        //2.利用正则表达式判断后缀
        if (!fileName.matches("^.+\\.(jpg|png|gif)$")){
            uiImage.setError(1);//表示不是正经图片
            return uiImage;
        }
        //3.获取图片的宽度和高度
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            if (height==0 || width==0){
                //如果宽度和高度为0,则程序终止
                uiImage.setError(1);
                return uiImage;
            }
            uiImage.setWidth(width).setHeight(height);

            //4.以时间格式进行数据存储yyyy/MM/dd
            String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            //准备文件上传路径
            String localDir = localFileDir + dateDir;
            File dirFile = new File(localDir);
            if (!dirFile.exists()){
                //如果文件不存在,则创建文件夹
                dirFile.mkdirs();
            }

            String uuid = UUID.randomUUID().toString().replace("-","");
            String fileType = fileName.substring(fileName.lastIndexOf("."));

            String realFileName = uuid + fileType;

            /*
            文件上传
             */
            String realPath = localDir + "/" + realFileName;
            File realFilePath = new File(realPath);

            uploadFile.transferTo(realFilePath);

            System.out.println("上传成功!!!");

            uiImage.setUrl(urlPath + dateDir + "/" + realFileName);

        } catch (IOException e) {
            e.printStackTrace();
            uiImage.setError(1);    //程序出错,上传终止
        }
        return uiImage;
    }

}
