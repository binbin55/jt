package com.jt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestJson {

    /**
     * 对象转化json时调用对象的getXXX方法
     * @throws JsonProcessingException
     */
    public void toJson() throws JsonProcessingException {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L);
        itemDesc.setItemDesc("商品描述信息!!");
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(itemDesc);
        System.out.println(s);
    }

    public void testList() throws IOException {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L);
        itemDesc.setItemDesc("商品描述信息!!");
        ItemDesc itemDesc2 = new ItemDesc();
        itemDesc2.setItemId(1000L);
        itemDesc2.setItemDesc("商品描述信息!!");
        ItemDesc itemDesc3 = new ItemDesc();
        itemDesc3.setItemId(1000L);
        itemDesc3.setItemDesc("商品描述信息!!");
        List<ItemDesc> list = new ArrayList<>();
        list.add(itemDesc);
        list.add(itemDesc2);
        list.add(itemDesc3);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(list);
        System.out.println(s);

        //将json转化为对象{key:value....}
        List<ItemDesc> list1 = mapper.readValue(s, list.getClass());
        System.out.println(list1);
    }

    public static void main(String[] args) {
        TestJson testJson = new TestJson();
        try {
            testJson.testList();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
