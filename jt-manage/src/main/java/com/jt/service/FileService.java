package com.jt.service;

import com.jt.vo.EasyUIImage;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    EasyUIImage uploadFile(MultipartFile uploadFile);

}
