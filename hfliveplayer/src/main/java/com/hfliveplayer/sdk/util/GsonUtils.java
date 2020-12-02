package com.hfliveplayer.sdk.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {

    public static <T> List<T> parseJson(String jsonString,Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(String.valueOf(jsonString),type);
        return list;
    }

    public static <T> List<T> getRecords(String jsonString,Class clazz) {
        JsonElement element = new JsonParser().parse(jsonString).getAsJsonObject().get("records");
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(element,type);
        return list;
    }

    public static JsonElement getValue(String jsonString,String key) {
        return new JsonParser().parse(jsonString).getAsJsonObject().get(key);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
