package com.zhwyd.server.cache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.Area;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Game;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.Mail;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CryptionUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.BattleServerService;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.MailService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.RedisConfigService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
public class CacheService {
    private static Map<Integer, AccountServer>        accountServerCache               = new ConcurrentHashMap<Integer, AccountServer>();
    private static Map<Integer, AccountServer>        accountServerCacheByServerId     = new ConcurrentHashMap<Integer, AccountServer>();
    private static Map<String, AccountServer>         accountNameServerCache           = new ConcurrentHashMap<String, AccountServer>();
    private static Map<Integer, List<AccountServer>>  accountServerByWorldIdCache      = new ConcurrentHashMap<Integer, List<AccountServer>>();
    private static Map<Integer, IpdServer>            ipdServerCache                   = new ConcurrentHashMap<Integer, IpdServer>();
    private static Map<String, IpdServer>             ipdServerCacheByPort             = new ConcurrentHashMap<String, IpdServer>();
    private static Map<Integer, WorldServer>          worldServerCache                 = new ConcurrentHashMap<Integer, WorldServer>();
    private static Map<Integer, List<WorldServer>>    worldServerListCacheByGameId     = new ConcurrentHashMap<Integer, List<WorldServer>>();
    private static Map<Integer, DispatchServer>       dispatchServerCache              = new ConcurrentHashMap<Integer, DispatchServer>();
    private static Map<Integer, List<DispatchServer>> dispatchServerListCacheByWorldId = new ConcurrentHashMap<Integer, List<DispatchServer>>();
    private static Map<Integer, Server>               serverCache                      = new ConcurrentHashMap<Integer, Server>();
    private static Map<Integer, Model>                modelCache                       = new ConcurrentHashMap<Integer, Model>();
    private static List<AccountServer>                accountServerAllList             = new ArrayList<AccountServer>();
    private static List<IpdServer>                    ipdServerAllList                 = new ArrayList<IpdServer>();
    private static List<WorldServer>                  worldServerAllList               = new ArrayList<WorldServer>();
    private static List<DispatchServer>               dispatchServerAllList            = new ArrayList<DispatchServer>();
    private static List<Mail>                         mailList                         = new ArrayList<Mail>();
    private static List<String>                       mailSendAddressList              = new ArrayList<String>();
    private static boolean                            isMonitor                        = true;
    private static Map<String, String[]>              sessionTypeMap                   = new HashMap<String, String[]>();
    private static IpdServer                          ipdServerMain                    = null;
    private static List<Server>                       serverAllList                    = new ArrayList<Server>();
    private static List<Game>                         gameList                         = new ArrayList<Game>();
    private static Map<Integer, Game>                 gameCache                        = new ConcurrentHashMap<Integer, Game>();
    private static List<RedisConfig>                  redisConfigList                  = new ArrayList<RedisConfig>();
    private static Map<Integer, RedisConfig>          redisConfigCache                 = new ConcurrentHashMap<Integer, RedisConfig>();
    private static List<Area>                         areaList                         = new ArrayList<Area>();
    private static Map<Integer, Area>                 areaCache                        = new ConcurrentHashMap<Integer, Area>();
    private static List<BattleServer>                 battleServersList                = new ArrayList<BattleServer>();
    private static Map<Integer, BattleServer>         battleServerCache                = new ConcurrentHashMap<Integer, BattleServer>();
    private static boolean isOpareting = false;

    /**
     * 初始化所有缓存
     */
    public static void initAll() {
        initAccountServer();
        initIpdServer();
        initWorldServer();
        initDispatchServer();
        initServer();
        initModel();
        initMail();
        initSessionTypeMap();
        initGame();
        initRedisConfig();
        initArea();
        initBattleServer();
        System.out.println("初始化成功！");
    }

    /** 初始化AccountServer */
    public static void initAccountServer() {
        accountServerCache.clear();
        accountServerCacheByServerId.clear();
        accountServerAllList.clear();
        accountNameServerCache.clear();
        List<AccountServer> accountServerList = Application.getBean(AccountServerService.class).getAll();
        accountServerAllList = accountServerList;
        for (AccountServer accountServer : accountServerList) {
            accountServerCache.put(accountServer.getId(), accountServer);
            accountServerCacheByServerId.put(accountServer.getServerId(), accountServer);
            accountNameServerCache.put(accountServer.getName(), accountServer);
        }
    }

    public static AccountServer getAccountServerById(int accountId) {
        return accountServerCache.get(accountId);
    }

    public static AccountServer getAccountServerByServerId(int serverId) {
        return accountServerCacheByServerId.get(serverId);
    }

    public static Map<Integer, AccountServer> getAccountCacheMap() {
        return accountServerCache;
    }

    public static Map<String, AccountServer> getAccountNameCacheMap() {
        return accountNameServerCache;
    }

    public static List<AccountServer> getAccountServerAllList() {
        return accountServerAllList;
    }

    public static void initIpdServer() {
        ipdServerCache.clear();
        ipdServerCacheByPort.clear();
        ipdServerAllList.clear();
        List<IpdServer> ipdServerList = Application.getBean(IpdServerService.class).getAll();
        ipdServerAllList = ipdServerList;
        for (IpdServer ipdServer : ipdServerList) {
            if (ipdServer.getIsMain() == 1) {
                ipdServerMain = ipdServer;
            }
            ipdServerCache.put(ipdServer.getId(), ipdServer);
            ipdServerCacheByPort.put(getKeyName(ipdServer.getServerId(), ipdServer.getPort()), ipdServer);
        }
    }

    public static IpdServer getIpdServerMain() {
        return ipdServerMain;
    }

    public static IpdServer getIpdServerById(int ipdServerId) {
        return ipdServerCache.get(ipdServerId);
    }

    public static IpdServer getIpdServerByServerIdAndPort(int serverId, int port) {
        String ipdKey = getKeyName(serverId, port);
        return ipdServerCacheByPort.get(ipdKey);
    }

    public static List<IpdServer> getIpdServerAllList() {
        return ipdServerAllList;
    }

    public static void initWorldServer() {
        worldServerCache.clear();
        worldServerListCacheByGameId.clear();
        List<WorldServer> worldServerList = Application.getBean(WorldServerService.class).getAll();
        worldServerAllList = worldServerList;
        for (WorldServer worldServer : worldServerList) {
            worldServerCache.put(worldServer.getId(), worldServer);
            if (worldServer.getGameId() > 0) {
                List<WorldServer> worldServerListTemp = null;
                if (worldServerListCacheByGameId.containsKey(worldServer.getGameId())) {
                    worldServerListTemp = worldServerListCacheByGameId.get(worldServer.getGameId());
                    worldServerListTemp.add(worldServer);
                } else {
                    worldServerListTemp = new ArrayList<WorldServer>();
                    worldServerListTemp.add(worldServer);
                    worldServerListCacheByGameId.put(worldServer.getGameId(), worldServerListTemp);
                }
            }
            AccountServer accountServer = null;
            if (worldServer.getAccountId() > 0) {
                List<AccountServer> accountServerListTemp = null;
                accountServer = getAccountServerById(worldServer.getAccountId());
                if (accountServerByWorldIdCache.containsKey(worldServer.getAccountId())) {
                    accountServerListTemp = accountServerByWorldIdCache.get(worldServer.getAccountId());
                    accountServerListTemp.add(accountServer);
                } else {
                    accountServerListTemp = new ArrayList<AccountServer>();
                    accountServerListTemp.add(accountServer);
                    accountServerByWorldIdCache.put(worldServer.getAccountId(), accountServerListTemp);
                }
            }
        }
    }

    public static WorldServer getWorldServerById(int worldId) {
        return worldServerCache.get(worldId);
    }

    public static List<WorldServer> getWorldServerListByGameId(int gameId) {
        return worldServerListCacheByGameId.get(gameId);
    }

    public static List<WorldServer> getWorldServerAllList() {
        return worldServerAllList;
    }

    public static List<AccountServer> getAccountServerListByWorldId(int worldId) {
        return accountServerByWorldIdCache.get(worldId);
    }

    public static void initDispatchServer() {
        dispatchServerCache.clear();
        dispatchServerListCacheByWorldId.clear();
        List<DispatchServer> dispatchServerList = Application.getBean(DispatchServerService.class).getAll();
        dispatchServerAllList = dispatchServerList;
        Collections.sort(dispatchServerList, new Comparator<DispatchServer>() {
            @Override
            public int compare(DispatchServer o1, DispatchServer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        int worldId = 0;
        for (DispatchServer dispatchServer : dispatchServerList) {
            dispatchServerCache.put(dispatchServer.getId(), dispatchServer);
            worldId = dispatchServer.getWorldId();
            if (worldId > 0) {
                List<DispatchServer> dispatchServerListTemp = null;
                if (dispatchServerListCacheByWorldId.containsKey(worldId)) {
                    dispatchServerListTemp = dispatchServerListCacheByWorldId.get(worldId);
                    dispatchServerListTemp.add(dispatchServer);
                } else {
                    dispatchServerListTemp = new ArrayList<DispatchServer>();
                    dispatchServerListTemp.add(dispatchServer);
                    dispatchServerListCacheByWorldId.put(worldId, dispatchServerListTemp);
                }
            }
        }
    }

    public static DispatchServer getDispatchServerById(int dispatchId) {
        return dispatchServerCache.get(dispatchId);
    }

    public static List<DispatchServer> getDispatchServerListByWorldId(int worldId) {
        return dispatchServerListCacheByWorldId.get(worldId);
    }

    public static List<DispatchServer> getDispatchServerAllList() {
        return dispatchServerAllList;
    }

    public static String getKeyName(int paramOne, int paramTwo) {
        return paramOne + "_" + paramTwo;
    }

    /** 初始化Server */
    public static void initServer() {
        serverCache.clear();
        serverAllList.clear();
        List<Server> serverList = Application.getBean(ServerService.class).getAll();
        serverAllList = serverList;
        for (Server server : serverList) {
            String psw = CryptionUtil.getDecryptString(server.getSshPwd(), Application.getConfig("system", "key"));
            server.setSshPwd(psw);
            serverCache.put(server.getId(), server);
        }
    }

    public static List<Server> getServerAllList() {
        return serverAllList;
    }

    public static Server getServerById(int serverId) {
        return serverCache.get(serverId);
    }

    public static Map<Integer, Server> getServerMap() {
        return serverCache;
    }

    /** 初始化Model */
    public static void initModel() {
        modelCache.clear();
        List<Model> modelList = Application.getBean(ModelService.class).getAll();
        for (Model model : modelList) {
            modelCache.put(model.getId(), model);
        }
    }

    public Model getModelById(int modelId) {
        return modelCache.get(modelId);
    }

    public static void initMail() {
        mailList.clear();
        List<Mail> mailListTemp = Application.getBean(MailService.class).getAll();
        mailList = mailListTemp;
        for (Mail mail : mailListTemp) {
            if (mail.getStatue() == CommanConstant.STATE_START) {
                mailSendAddressList.add(mail.getMail());
            }
        }
    }

    public static List<Mail> getMailList() {
        return mailList;
    }

    public static List<String> getMailSendList() {
        return mailSendAddressList;
    }

    public static void initGame() {
        gameList.clear();
        gameCache.clear();
        gameList = Application.getBean(GameService.class).getAll();
        for (Game game : gameList) {
            gameCache.put(game.getId(), game);
        }
    }

    public static List<Game> getGameList() {
        return gameList;
    }

    public static Map<Integer, Game> getGameMap() {
        return gameCache;
    }

    public static void initRedisConfig() {
        redisConfigList.clear();
        redisConfigCache.clear();
        redisConfigList = Application.getBean(RedisConfigService.class).getAll();
        for (RedisConfig redisConfig : redisConfigList) {
            redisConfigCache.put(redisConfig.getId(), redisConfig);
        }
    }

    public static List<RedisConfig> getRedisConfigList() {
        return redisConfigList;
    }

    public static Map<Integer, RedisConfig> getRedisConfigMap() {
        return redisConfigCache;
    }

    public static RedisConfig getRedisConfig(Integer id) {
        return redisConfigCache.get(id);
    }

    public static void initArea() {
        areaList.clear();
        areaCache.clear();
        areaList = Application.getBean(GameService.class).getAreaList();
        for (Area area : areaList) {
            areaCache.put(area.getId(), area);
        }
    }

    public static List<Area> getAreaList() {
        return areaList;
    }

    public static Map<Integer, Area> getAreaMap() {
        return areaCache;
    }

    public static Area getArea(Integer id) {
        return areaCache.get(id);
    }

    public static void initBattleServer() {
        battleServersList.clear();
        battleServerCache.clear();
        battleServersList = Application.getBean(BattleServerService.class).getAll();
        for (BattleServer battleServer : battleServersList) {
            battleServerCache.put(battleServer.getId(), battleServer);
        }
    }

    public static List<BattleServer> getBattleServerList() {
        return battleServersList;
    }

    public static Map<Integer, BattleServer> getBattleServerMap() {
        return battleServerCache;
    }

    public static BattleServer getBattleServer(Integer id) {
        return battleServerCache.get(id);
    }

    /**
     * 设置监控状态
     */
    public static void setIsMonitor(int isMonitors) {
        if (isMonitors == 0) {
            isMonitor = false;
        } else {
            isMonitor = true;
        }
    }

    public static boolean getIsMonitor() {
        return isMonitor;
    }

    public static void initSessionTypeMap() {
        sessionTypeMap.clear();
        sessionTypeMap.put(Global.BATCH_START_ACCOUNT, new String[] { "batchAccount", "batchAccountNum"});
        sessionTypeMap.put(Global.BATCH_START_IPD, new String[] { "batchIpd", "batchIpdNum"});
        sessionTypeMap.put(Global.BATCH_START_WORLD, new String[] { "batchWorld", "batchWorldNum"});
        sessionTypeMap.put(Global.BATCH_START_DISPATCH, new String[] { "batchDispatch", "batchDispatchNum"});
    }

    /**
     * 记录session数据
     */
    public static void updateSession(HttpSession session, String type, StringBuilder stringBuilder) {
        StringBuffer batchSb = null;
        synchronized (session) {
            if (session.getAttribute(sessionTypeMap.get(type)[0]) != null) {
                batchSb = (StringBuffer) session.getAttribute(sessionTypeMap.get(type)[0]);
                batchSb.append(stringBuilder + "</br>");
            } else {
                batchSb = new StringBuffer();
                batchSb.append(stringBuilder + "</br>");
            }
            int batchNum = 1;
            if (session.getAttribute(sessionTypeMap.get(type)[1]) != null) {
                batchNum = (Integer) session.getAttribute(sessionTypeMap.get(type)[1]) + 1;
            }
            System.out.println(sessionTypeMap.get(type)[0] + "=" + batchSb);
            session.setAttribute(sessionTypeMap.get(type)[0], batchSb);
            System.out.println(sessionTypeMap.get(type)[1] + "=" + batchNum);
            session.setAttribute(sessionTypeMap.get(type)[1], batchNum);
        }
        SystemLogService.batchWorldLog(session, stringBuilder.toString());
        stringBuilder = null;
        batchSb = null;
    }

    /**
     * 记录session数据
     */
    public static void updateFirstSession(HttpSession session, String type, StringBuilder stringBuilder) {
        StringBuffer batchSb = null;
        synchronized (session) {
            if (session.getAttribute(sessionTypeMap.get(type)[0]) != null) {
                batchSb = (StringBuffer) session.getAttribute(sessionTypeMap.get(type)[0]);
                batchSb.append(stringBuilder + "</br>");
            } else {
                batchSb = new StringBuffer();
                batchSb.append(stringBuilder + "</br>");
            }
            System.out.println(sessionTypeMap.get(type)[0] + "=" + batchSb);
            session.setAttribute(sessionTypeMap.get(type)[0], batchSb);
        }
        SystemLogService.batchWorldLog(session, stringBuilder.toString());
        stringBuilder = null;
        batchSb = null;
    }

    /**
     * 记录session数据
     */
    public static void updateSecondSession(HttpSession session, String type) {
        synchronized (session) {
            int batchNum = 1;
            if (session.getAttribute(sessionTypeMap.get(type)[1]) != null) {
                batchNum = (Integer) session.getAttribute(sessionTypeMap.get(type)[1]) + 1;
            }
            System.out.println("sessionTypeMap=" + sessionTypeMap.get(type)[1] + "=" + batchNum);
            session.setAttribute(sessionTypeMap.get(type)[1], batchNum);
        }
    }

    /**
     * 记录session数据
     */
    @SuppressWarnings("unchecked")
    public static void updateSession(HttpSession session, BatchOpearVo batchOpearVo, String type) {
        List<BatchOpearVo> batchOpearteList = null;
        synchronized (session) {
            if (session.getAttribute(type) != null) {
                batchOpearteList = (List<BatchOpearVo>) session.getAttribute(type);
                batchOpearteList.add(batchOpearVo);
            } else {
                batchOpearteList = new ArrayList<BatchOpearVo>();
                batchOpearteList.add(batchOpearVo);
            }
            session.setAttribute(type, batchOpearteList);
        }
    }
    
    /**
     * 是否正在操作
     * @return
     */
    public static boolean isOpareting() {
        return isOpareting;
    }
    
    /**
     * 设置是否正在操作
     * @param isOparetings
     */
    public static void setIsOpareting(boolean isOparetings) {
        isOpareting = isOparetings;
    }
}
