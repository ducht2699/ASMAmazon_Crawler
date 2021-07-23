package com.vnsoft.exam.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by GNUD on 4/8/2017
 */

public class GsonUtils {

    private static Gson gson;

    public synchronized static Gson getInstance() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) gson = new Gson();
            }
        }
        return gson;
    }

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    public static String serialize(Object object, Class clazz) {
        return getInstance().toJson(object, clazz);
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    public static <T> T deserialize(String string, Class<T> clazz) {
        return getInstance().fromJson(string, clazz);
    }




    public static String serializeList(List<Object> objects, Class clazz) {
        return getInstance().toJson(objects, clazz);
    }

    public static <T> List<T> deserializeList(String string, Class<T> clazz) {
        Type type = new TypeToken<T>() {
        }.getType();
        return getInstance().fromJson(string, type);
    }
}
