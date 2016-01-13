package com.zhwyd.server.common.constant;
public class CommanConstant {
    /** 状态--关闭 */
    public static final int    STATE_STOP                    = 0;
    /** 状态--启动 */
    public static final int    STATE_START                   = 1;
    /** 状态--上传 */
    public static final int    UPLOAD                        = 0;
    /** 状态--下载 */
    public static final int    DOWNLOAD                      = 1;
    /** 类型--servreLib */
    public static final int    SERVER_LIB                    = 0;
    /** 状态--lib */
    public static final int    LIB                           = 1;
    /** 运行脚本 */
    public static final int    RUN_SHELL                     = 1;
    /** 显示日志 */
    public static final int    SHOW_STDOUT                   = 2;
    /** 停止脚本 */
    public static final int    KILL_SHELL                    = 3;
    /** 清除日志 */
    public static final int    CLEAR_STDOUT                  = 4;
    /** 获取PID */
    public static final int    GET_CUR_PID                   = 5;
    /** 创建目录 */
    public static final int    CREATE_DIR                    = 6;
    /** 修改运行状态 */
    public static final int    RUN_STATE                     = 0;
    /** 修改部署状态 */
    public static final int    DEPLOY_STATE                  = 1;
    /** 返回结果正确状态 */
    public static final int    RESULT_TRUE_STATE             = 0;
    /** 返回结果错误状态 */
    public static final int    RESULT_FALSE_STATE            = 1;
    /** 上传单个文件 */
    public static final int    STATE_UPLOAD_SINGLE           = 0;
    /** 上传文件夹下所有文件 */
    public static final int    STATE_UPLOAD_ALL              = 1;
    /** 颜色-绿 */
    public static final String COLOR_GREEN                   = "green";
    /** 颜色-红 */
    public static final String COLOR_RED                     = "red";
    /** 只更新重啟world */
    public static final int    UPDATE_RESTARY_WORLD          = 1;
    /** 只更新重啟dispatch */
    public static final int    UPDATE_RESTARY_DISPATCH       = 2;
    /** 更新重啟world、dispatch */
    public static final int    UPDATE_RESTARY_WORLD_DISPATCH = 3;
    /** 只重啟dispatch，不更新 */
    public static final int    RESTARY_DISPATCH              = 4;
    /** 更新只重啟world、dispatch，不更新 */
    public static final int    RESTARY_WORLD_DISPATCH        = 5;
    /** JDBC类型--所有 */
    public static final int    TYPE_JDBC_ALL                 = 0;
    /** JDBC类型--world */
    public static final int    TYPE_JDBC_WORLD               = 1;
    /** JDBC类型--account */
    public static final int    TYPE_JDBC_ACCOUNT             = 2;
}
