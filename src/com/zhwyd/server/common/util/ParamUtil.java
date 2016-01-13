package com.zhwyd.server.common.util;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;
public class ParamUtil {
    public static Double getDouble(String name, String param) {
        try {
            if(!StringUtils.hasText(param)){
                return null;
            }
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getDouble(name);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(String name, String param) {
        try {
            if(!StringUtils.hasText(param)){
                return null;
            }
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getLong(String name, String param) {
        try {
            if(!StringUtils.hasText(param)){
                return null;
            }
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getLong(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Integer getInteger(String name, String param) {
        try {
            if(!StringUtils.hasText(param)){
                return null;
            }
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject getJSONObject(String name,String param){
        try {
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getJSONObject(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONArray getJSONArray(String name,String param){
        try {
            JSONObject obj = JSONObject.fromObject(param);
            return obj.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}