package com.zhwyd.server.config;
import net.sf.json.JSONObject;
public class ServerResult {
    private JSONObject json;

    public JSONObject getJson() {
        return json;
    }

    public ServerResult() {
        json = new JSONObject();
    }

    public ServerResult setSuccess() {
        setInt("code", 200);
        setString("success", "true");
        setString("message", "");
        return this;
    }
    public ServerResult setSuccessWithStatus() {
        setInt("status", 1);
        setString("msg", "");
        return this;
    }
    
    public ServerResult setFail(String message) {
        setInt("code", 0);
        setString("success", "false");
        setString("message", message);
        return this;
    }
    
    public ServerResult setFail() {
        setInt("code", 0);
        setString("success", "false");
        return this;
    }
    
    public ServerResult setFailWithStatus(String message) {
        setInt("status", 0);
        setString("msg", message);
        return this;
    }

    public ServerResult setString(String key, String value) {
        if (value == null) {
            value = "";
        }
        json.element(key, value);
        return this;
    }

    public ServerResult setInt(String key, int value) {
        json.element(key, value);
        return this;
    }

    public void clear() {
        json.clear();
    }
}
