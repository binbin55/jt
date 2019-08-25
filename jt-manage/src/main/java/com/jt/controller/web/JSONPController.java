package com.jt.controller.web;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemCat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {

    /**
     * JSONP调用,服务器端返回数据的要求
     */
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonp(String callback){
        ItemCat itemCat = new ItemCat();
        itemCat.setId(1000L).setName("jsonp测试调用!");
//        String json = ObjectMapperUtil.toJson(itemCat);
//        json = callback + "(" + json + ")";
        return new JSONPObject(callback,itemCat);
    }

}
