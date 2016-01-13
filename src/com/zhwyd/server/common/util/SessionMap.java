package com.zhwyd.server.common.util;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
/**
 * 用于存放回调值
 * 
 * @author rL.
 */
public class SessionMap {
    private static SessionMap                      sessionMap;
    // private Map<User, Map<String, AbstractData>> map; // 协议回调集合
    private Map<String, Map<String, AbstractData>> map;            // 协议回调集合
    private Map<String, User>                      sessionIdMap;   // 协议sessionId集合
    private static final int                       WAIT_TIME = 10; // 等待handler处理时间
    private Map<String, Map<String, AbstractData>> newMap;         // 协议回调集合
    private Map<String, String>                    newSessionIdMap; // 协议sessionId集合

    public SessionMap() {
        // map = new ConcurrentHashMap<User, Map<String, AbstractData>>();
        map = new ConcurrentHashMap<String, Map<String, AbstractData>>();
        sessionIdMap = new ConcurrentHashMap<String, User>();
        newMap = new ConcurrentHashMap<String, Map<String, AbstractData>>();
        newSessionIdMap = new ConcurrentHashMap<String, String>();
    }

    public static SessionMap getInstance() {
        synchronized (SessionMap.class) {
            if (null == sessionMap) {
                sessionMap = new SessionMap();
            }
        }
        return sessionMap;
    }

    // /**
    // * 获取协议回调集合
    // *
    // * @return
    // */
    // public Map<User, Map<String, AbstractData>> getMap() {
    // return map;
    // }
    /**
     * 获取协议回调集合
     * 
     * @return
     */
    public Map<String, Map<String, AbstractData>> getMap() {
        return map;
    }

    /**
     * 获取协议回调集合
     * 
     * @return
     */
    public Map<String, Map<String, AbstractData>> getNewMap() {
        return newMap;
    }

    /**
     * 获取协议sessionId集合
     * 
     * @return
     */
    public Map<String, User> getSessionIdMap() {
        return sessionIdMap;
    }

    /**
     * 获取协议sessionId集合
     * 
     * @return
     */
    public Map<String, String> getNewSessionIdMap() {
        return newSessionIdMap;
    }

    /**
     * 根据sessionId获取用户名
     * 
     * @param sessionId
     * @return
     */
    public User getUserBySessionId(int sessionId) {
        return sessionIdMap.get(sessionId);
    }

    /**
     * 根据sessionId获取用户名
     * 
     * @param sessionId
     * @return
     */
    public User getUserBySessionId(String sessionId) {
        return sessionIdMap.get(sessionId);
    }

    /**
     * 根据sessionId获取用户名
     * 
     * @param sessionId
     * @return
     */
    public String getIdBySessionId(String sessionId) {
        return newSessionIdMap.get(sessionId);
    }

    /**
     * 根据当前玩家和返回协议成功名称查询出协议值
     * 
     * @param user
     *            当前玩家
     * @param successKey
     *            返回协议成功名称
     * @return
     */
    public AbstractData getMsgByUser(User user, String successKey) {
        try {
            boolean isTure = true;
            int i = 0;
            while (isTure) {
                // Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user);
                Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user.getUsername());
                if (valueMap.isEmpty()) {
                    Map<String, AbstractData> userMap = new ConcurrentHashMap<String, AbstractData>();
                    // map.put(user, userMap);
                    map.put(user.getUsername(), userMap);
                }
                if (valueMap.get(successKey) == null) {
                    if (i == WAIT_TIME) {
                        isTure = false;
                    }
                    Thread.sleep(1000L);
                    i++;
                    // System.out.println("i=" + i);
                } else {
                    return valueMap.get(successKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据当前玩家和返回协议成功名称查询出协议值
     * 
     * @param user
     *            当前玩家
     * @param successKey
     *            返回协议成功名称
     * @return
     */
    public AbstractData getMsgByUser(String sessionId, String successKey) {
        try {
            boolean isTure = true;
            int i = 0;
            while (isTure) {
                Map<String, AbstractData> valueMap = SessionMap.getInstance().getNewMap().get(sessionId);
                if (valueMap.isEmpty()) {
                    Map<String, AbstractData> userMap = new ConcurrentHashMap<String, AbstractData>();
                    newMap.put(sessionId, userMap);
                }
                if (valueMap.get(successKey) == null) {
                    if (i == WAIT_TIME) {
                        isTure = false;
                    }
                    Thread.sleep(1000L);
                    i++;
                    System.out.println("i=" + i);
                } else {
                    return valueMap.get(successKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据当前玩家和返回协议成功名称查询出协议值
     * 
     * @param user
     *            当前玩家
     * @param successKey
     *            返回协议成功名称
     * @return
     */
    public AbstractData getMsgByUserForNoWait(User user, String successKey) {
        // Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user);
        Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user.getUsername());
        if (valueMap == null) {
            return null;
        }
        return valueMap.get(successKey);
    }

    /**
     * 根据当前玩家和返回协议成功名称查询出协议值
     * 
     * @param user
     *            当前玩家
     * @param successKey
     *            返回协议成功名称
     * @param waitTime
     *            等待时间
     * @param period
     *            时间间隔
     * @return
     */
    public AbstractData getMsgByUserForWait(User user, String successKey, Long waitTime, Long period) {
        // Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user);
        Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user.getUsername());
        if (valueMap == null) {
            return null;
        }
        boolean flag = true;
        while (flag) {
            if (valueMap.get(successKey) == null) {
                // 如果超时则跳出循环
                if (waitTime <= 0L) {
                    flag = false;
                }
                try {
                    // 等待时间
                    Thread.sleep(period);
                    // 减少等待总时间
                    waitTime -= period;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return valueMap.get(successKey);
            }
        }
        return null;
    }

    /**
     * 根据协议KEY删除回调值
     * 
     * @param user
     * @param key
     */
    public void removeByKey(User user, String key) {
        synchronized (SessionMap.class) {
            // Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user);
            Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(user.getUsername());
            if (valueMap != null) {
                valueMap.remove(key);
            }
        }
    }

    // /**
    // * 根据协议KEY删除回调值
    // *
    // * @param user
    // * @param key
    // */
    // public void removeByKey(String sessionId, String key) {
    // synchronized (SessionMap.class) {
    // Map<String, AbstractData> valueMap = SessionMap.getInstance().getNewMap().get(sessionId);
    // if (valueMap != null) {
    // valueMap.remove(key);
    // }
    // }
    // }
    /**
     * 根据协议KEY删除回调值
     * 
     * @param user
     * @param key
     */
    public void removeByKey(String userName, String key) {
        synchronized (SessionMap.class) {
            Map<String, AbstractData> valueMap = SessionMap.getInstance().getMap().get(userName);
            if (valueMap != null) {
                valueMap.remove(key);
            }
        }
    }

    /**
     * 根据sessionId删除user
     */
    public void removeBySessionId(String sessionId) {
        synchronized (SessionMap.class) {
            SessionMap.getInstance().getSessionIdMap().remove(sessionId);
        }
    }

    /**
     * 根据user删除对应的用于存放回调值的map
     */
    public void removeByUser(User user) {
        synchronized (SessionMap.class) {
            // SessionMap.getInstance().getMap().remove(user);
            SessionMap.getInstance().getMap().remove(user.getUsername());
        }
    }

    /**
     * 根据sessionId绑定一个user,一个user对应唯一个userMap
     * 
     * @param sessionId
     * @param user
     */
    public void bind(String sessionId, User user) {
        Map<String, AbstractData> userMap;
        // synchronized (map) {
        // userMap = map.get(user);
        // if (userMap == null) {
        // userMap = new ConcurrentHashMap<String, AbstractData>();
        // map.put(user, userMap);
        // }
        // }
        synchronized (map) {
            userMap = map.get(user.getUsername());
            if (userMap == null) {
                userMap = new ConcurrentHashMap<String, AbstractData>();
                map.put(user.getUsername(), userMap);
            }
        }
        sessionIdMap.put(sessionId, user);
    }

    /**
     * 根据sessionId绑定一个user,一个user对应唯一个userMap
     * 
     * @param sessionId
     * @param user
     */
    public void bindNew(String sessionId, String Id) {
        Map<String, AbstractData> sessionMap;
        synchronized (newMap) {
            sessionMap = map.get(Id);
            if (sessionMap == null) {
                sessionMap = new ConcurrentHashMap<String, AbstractData>();
                newMap.put(Id, sessionMap);
            }
        }
        newSessionIdMap.put(sessionId, Id);
    }
}
