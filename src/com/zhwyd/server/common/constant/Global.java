package com.zhwyd.server.common.constant;
public class Global {
    /** 游戏ID */
    public static final String GAME_ID                           = "GAME_ID";
    /** ACCOUNT服务PID文件名称 */
    public static final String ACCOUNT_PID_FILE                  = "gunsoulAccserver.pid";
    /** IPDMAIN服务PID文件名称 */
    public static final String IPDMAIN_PID_FILE                  = "gunsoulIpd.pid";
    /** WORLD服务PID文件名称 */
    public static final String WORLD_PID_FILE                    = "gunsoulWorldServer.pid";
    /** DISPATCH服务PID文件名称 */
    public static final String DISPATCH_PID_FILE                 = "gunsoulDispatchServer.pid";
    /** BATTLE服务PID文件名称 */
    public static final String BATTLE_PID_FILE                   = "battleServer.pid";
    /** ACCOUNT服务config配置文件名称 */
    public static final String CONFIG_ACCOUNT_PROPERTIES         = "configAccount.properties";
    /** IPD服务config配置文件名称 */
    public static final String CONFIG_IPD_PROPERTIES             = "config.properties";
    /** WORLD服务config配置文件名称 */
    public static final String CONFIG_WORLD_PROPERTIES           = "configWorld.properties";
    /** DISPATCH服务config配置文件名称 */
    public static final String CONFIG_DISPATCH_PROPERTIES        = "configDispatch.properties";
    /** WORLD服务version配置文件名称 */
    public static final String CONFIG_WORLD_VERSION              = "version.xml";
    /** WORLD服务jdbc配置文件名称 */
    public static final String CONFIG_WORLD_JDBC                 = "jdbc.properties";
    /** ACCOUNT服务jdbc配置文件名称 */
    public static final String CONFIG_ACCOUNT_JDBC               = "jdbc.properties";
    /** WORLD服务run.sh配置文件名称 */
    public static final String CONFIG_WORLD_RUN                  = "run.sh";
    /** 目录存在--常量 */
    public static final String DIR_IS_EXIST                      = "目标目录已经存在,请重新选择！";
    /** 目录不存在--常量 */
    public static final String DIR_IS_NOT_EXIST                  = "模板目录不存在，请先创建指定目录";
    /** 临时配置文件存放路径 */
    public static final String TEMP_CONFIG_PATH                  = "目录不存在，请先创建指定目录";
    /** 当前登录用户 */
    public static final String LOGIN_CURRENT_USER                = "LOGIN_CURRENT_USER";
    /** 当前登录用户名称 */
    public static final String LOGIN_CURRENT_NAME                = "LOGIN_CURRENT_NAME";
    /** 公共密码 */
    public static final String PASSWORD                          = "000000";
    /** 公共密码 */
    public static final String KEY                               = "000000";
    /** 错误协议返回 */
    public static final String PROTOCOL_ERROR                    = "错误协议返回";
    // ==================================登录流程==============================================
    /** 玩家连接服务器成功 */
    public static final String CONNECT_OK                        = "连接成功";
    /** 玩家连接服务器失败 */
    public static final String CONNECT_FAIL                      = "连接失败";
    /** 玩家第三方登录成功 */
    public static final String LOGIN_OK                          = "玩家第三方登录成功";
    /** 玩家第三方登录失败 */
    public static final String LOGIN_FAIL                        = "玩家第三方登录失败";
    /** 玩家获取随机名称成功 */
    public static final String GET_RANDOM_NAME_OK                = "获取随机名称成功";
    /** 玩家获取随机名称失败 */
    public static final String GET_RANDOM_NAME_FAIL              = "获取随机名称失败";
    /** 玩家创建角色成功 */
    public static final String CREATE_ROLE_ACTOR_OK              = "玩家创建角色成功";
    /** 玩家获取角色列表成功 */
    public static final String GET_ROLE_ACTOR_LIST_OK            = "获取角色列表成功";
    /** 玩家获取角色列表失败 */
    public static final String GET_ROLE_ACTOR_LIST_FAIL          = "获取角色列表失败";
    /** 玩家角色登录成功 */
    public static final String ROlE_ACTOR_LOGIN_OK               = "玩家角色登录成功";
    /** 玩家角色登录失败 */
    public static final String ROlE_ACTOR_LOGIN_FAIL             = "玩家角色登录失败";
    /** 三方登录时抛错误协议 */
    public static final String THIRD_LOGIN_ERROR                 = "第三方登录时抛错误协议，请检查是否能正常登录";
    /** 获取随机名称时抛错误协议 */
    public static final String RANDOM_NAME_ERROR                 = "获取随机名称时抛错误协议，请检查是否能正常获取随机名称";
    /** 创建新角色时抛错误协议 */
    public static final String ROLE_ACTOR_ERROR                  = "创建新角色时抛错误协议，请检查是否能正常创建新角色";
    /** 获取角色列表时抛错误协议 */
    public static final String ACTOR_LIST_ERROR                  = "获取角色列表时抛错误协议，请检查是否能正常获取角色列表";
    /** 角色登录时抛错误协议 */
    public static final String ACTOR_LOGIN_ERROR                 = "角色登录时抛错误协议，请检查是否能正常角色登录";
    /** 第三方登录时超过5次不成功 */
    public static final String LOGIN_TWO_TIMES_ERROR             = "第三方登录时超过2次不成功，登录失败";
    /** 创建角色超过5次未能成功 */
    public static final String ROLE_ACTOR_TWO_TIMES_ERROR        = "创建角色超过2次未能成功，请检查是否能正常创建角色";
    /** 玩家连接服务器失败 */
    public static final String CONNECT_FAIL_TWO_TIME             = "与服务器建立连接失败";
    // ==================================服務相關操作============================================
    /** ACCOUNT服务开启成功返回结果 */
    public static final String ACCOUNT_SUCCESS_RESULT            = "认证服务器启动";
    /** DISPATCH服务开启成功返回结果 */
    public static final String DISPATCH_SUCCESS_RESULT           = "数据分发服务器启动完成";
    /** IPD服务开启成功返回结果 */
    public static final String IPD_SUCCESS_RESULT                = "服务分区公告器启动";
    /** WORLD服务开启成功返回结果 */
    public static final String WORLD_SUCCESS_RESULT              = "《枪魂》游戏世界服务器启动";
    /** 关联检查 */
    public static final String IS_RELATE                         = "有其他服务关联到该服务，请先删除其他关联服务再重试!";
    /** DISPATCH服务关闭成功返回结果 */
    public static final String DISPATCH_SUCCESS_CLOSE_RESULT     = "DispatchServer: {0} Is Closed.";
    /** 所有DISPATCH服务关闭成功返回结果 */
    public static final String ALL_DISPATCH_SUCCESS_CLOSE_RESULT = "All DispatchServer Is Closed";
    /** 批量启动ACCOUNT类型 */
    public static final String BATCH_START_ACCOUNT               = "batchStartAccountSession";
    /** 批量启动IPD类型 */
    public static final String BATCH_START_IPD                   = "batchStartIpdSession";
    /** 批量启动WORLD类型 */
    public static final String BATCH_START_WORLD                 = "batchStartWorldSession";
    /** 批量启动DISPATCH类型 */
    public static final String BATCH_START_DISPATCH              = "batchStartDispatchSession";
    /** 批量关闭ACCOUNT类型 */
    public static final String BATCH_CLOSE_ACCOUNT               = "batchCloseAccountSession";
    /** 批量关闭IPD类型 */
    public static final String BATCH_CLOSE_IPD                   = "batchCloseIpdSession";
    /** 批量关闭WORLD类型 */
    public static final String BATCH_CLOSE_WORLD                 = "batchCloseWorldSession";
    /** 批量关闭DISPATCH类型 */
    public static final String BATCH_CLOSE_DISPATCH              = "batchCloseDispatchSession";
    /** 批量更新操作类型 */
    public static final String BATCH_UPDATEVOLIST                = "batchUpdateVoList";
    /** 批量部署操作类型 */
    public static final String BATCH_DEPLOYVOLIST                = "batchDeployVoList";
    /** 更新bean操作类型 */
    public static final String UPDATE_BEAN_LIST                  = "updateBeanList";
    /** 部署bean操作类型 */
    public static final String DEPLOY_BEAN_LIST                  = "deployBeanList";
    /** 更新遠程配置bean操作类型 */
    public static final String UPDATE_CFG_BEAN_LIST              = "updateCfgBeanList";
    /** 更新模板文件名稱列表,在點擊更新時設置 */
    public static final String UPDATE_FILE_NAME_LIST             = "UpdateFileNameList";
    /** 迁服操作类型 */
    public static final String CHANGE_SERVER_VO_LIST             = "changeServerVoList";
    /** 更新遠程配置bean操作类型 */
    public static final String CHANGE_SERVER_BEAN_LIST           = "changeServerBeanList";
    /** 更新并重啟列表类型 */
    public static final String UPDATE_RESTART_VO_LIST            = "updateRestartVoList";
    /** 更新并重啟bean操作类型 */
    public static final String UPDATE_RESTART_BEAN_LIST          = "updateRestartBeanList";
    /** 更新模板文件名稱串，在获取更新文件列表时设置 */
    public static final String UPDATE_FILE_NAME_STR              = "UpdateFileNameStr";
    /** 更新jar包列表 */
    public static final String JAR_NAME_LIST                     = "JarNameList";
    /** 本地文件MD5值 ，在獲取本地文件MD5時設置 */
    public static final String LOCAL_FILE_MD5_VALUE              = "LocalFileMD5Value";
    /** 獲取批量开、关时dispatch的数量，session会在batchDispatchNum后面 加上一个标志，1为开启，0为关闭 */
    public static final String BATCH_DISPATCH_NUM                = "batchDispatchNum";
    /** 批量更新远程配置文件 */
    public static final String BATCH_UPDATE_REMOTE_CFG           = "batchUpdateRemoteCfg";
    /** world模板路径 */
    public static final String WORLD_MODEL_PATH                  = "/data/apps/model/model/worldServer";
    /** world更新路径 */
    public static final String WORLD_UPDATE_PATH                 = "/data/apps/model/update/worldServer";
    /** dispatch模板路径 */
    public static final String DISPATCH_MODEL_PATH               = "/data/apps/model/model/dispatchServer";
    /** dispatch更新路径 */
    public static final String DISPATCH_UPDATE_PATH              = "/data/apps/model/update/dispatchServer";
    /** account模板路径 */
    public static final String ACCOUNT_MODEL_PATH                = "/data/apps/model/model/account";
    /** account更新路径 */
    public static final String ACCOUNT_UPDATE_PATH               = "/data/apps/model/update/account";
    /** ipd模板路径 */
    public static final String IPD_MODEL_PATH                    = "/data/apps/model/model/ipdmain";
    /** ipd更新路径 */
    public static final String IPD_UPDATE_PATH                   = "/data/apps/model/update/ipdmain";
    /** 服务迁移 */
    public static final String CHANGE_SERVER_TO_REMOTE           = "changeServerToRemote";
    /** 硬核数据库 */
    public static final String JDBC_HD                           = "10.10.13.209,gunsoul_world_hd";
    /** 1-36服数据库 */
    public static final String JDBC_BEFORE_36                    = "10.10.13.209,gunsoul_world";
    /** 37及以后数据库 */
    public static final String JDBC_AFTER_37                     = "10.10.66.34,gunsoul_ssd1";
    /** 161及以后数据库 */
    public static final String JDBC_AFTER_161                    = "10.10.137.165,gunsoul_ssd2";
    /** .6数据库 */
    public static final String JDBC_6                            = "192.168.1.6,gunsoul_world2";
    /** 国服IOS数据库1 */
    public static final String JDBC_IOS_WORLD1                   = "rdsuo8syydwdrc5qe357.mysql.rds.aliyuncs.com,gunsoul_world_1";
    /** 台湾测试库 */
    public static final String JDBC_TW_TEST                      = "10.0.0.150,gunsoul_world_tw_1";
    /** 台湾账号测试库 */
    public static final String JDBC_TW_TEST_ACCOUNT              = "10.0.0.150,gunsoul_account";
    /** 天象代理数据库 */
    public static final String JDBC_TX_ACCOUNT                   = "10.10.66.34,gunsoul_account_ssd1";
    /** .6账号测试库 */
    public static final String JDBC_6_ACCOUNT                    = "192.168.1.6,gunsoul_account";
    /** 国服IOS账号库 */
    public static final String JDBC_IOS_ACCOUNT                  = "rdsuo8syydwdrc5qe357.mysql.rds.aliyuncs.com,gunsoul_account";
    /** 越南测试库 */
    public static final String JDBC_VN_TEST                      = "125.212.244.5,gunsoul_world_test";
    /** 越南账号测试库 */
    public static final String JDBC_VN_TEST_ACCOUNT              = "125.212.244.5,gunsoul_account_test";
    /** 分配1G内存 */
    public static final String RUN_1G                            = "1024,1024,170,256,256,256,8";
    /** 分配2G内存 */
    public static final String RUN_2G                            = "2048,2048,341,256,256,256,8";
    /** 分配4G内存 */
    public static final String RUN_4G                            = "4096,4096,600,500,500,256,12";
    /** 分配6G内存 */
    public static final String RUN_6G                            = "6144,6144,1000,500,500,256,12";
}
