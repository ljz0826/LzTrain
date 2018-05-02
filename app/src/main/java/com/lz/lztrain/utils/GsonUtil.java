package com.lz.lztrain.utils;


import com.google.gson.Gson;

public class GsonUtil {

    public static <T> T json2bean(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> String bean2json(Object object, Class<T> clazz){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

}
