package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 方法说明:
     *  根据API将对象转化为JSON,同时将JSON转化为对象
     */
    public static String toJson(Object obj) {
        String s = null;
        try {
            s = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return s;
    }

    //json转化为对象
    public static <T>T toObject(String json,Class<T> clz){
        T object = null;
        try {
            object = MAPPER.readValue(json, clz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return object;
    }

}
