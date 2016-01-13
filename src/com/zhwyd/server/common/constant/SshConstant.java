package com.zhwyd.server.common.constant;
/**
 * ssh操作基础类
 * 
 * @author linkaijie
 */
public class SshConstant {
    /** 进入服务器所在目录 -- {0}为服务放置路径 */
    public static final String CD_SERVER_PATH             = "cd {0};";
    /** 运行服务 */
    public static final String RUN_SHELL                  = "sh run.sh;";
    /** 查看日志 */
    public static final String SHOW_STDOUT                = "tail stdout.log;";
    /** 查看日志 */
    public static final String SHOW_STDOUT_LAST           = "tail -n200 stdout.log;";
    /** 查看当前进程 -- {0}为服务名称 */
    public static final String SHOW_CUR_PROGRESS          = "ps aux |grep {0};";
    /** kill服务 */
    public static final String KILL_SHELL                 = "sh kill.sh;";
    /** kill当前进程 -- {0}为服务名称 */
    public static final String KILL_CUR_PROGRESS          = "ps aux | grep {0} | grep -v 'grep' | awk '{print $2}' | xargs kill -9;";
    /** 清除日志 */
    public static final String CLEAR_STDOUT               = ">stdout.log;";
    /** 查看所有程序 */
    public static final String SHOW_ALL_PROCEDURE         = "ps -A;";
    /** 查看进程是否存在 -- {0}为进程ID */
    public static final String SHOW_PROGRESS_IS_EXIST     = "ps --no-heading {0} | wc -l;";
    /** 获取当前进程号 -- {0}为PID文件名称 */
    public static final String GET_CUR_PID                = "cat {0};";
    /** 判断目录是否存在 -- {0}为文件目录 */
    /** "test -d ./account && echo '目录存在'  || echo '目录不存在';" -- 效果与下面一样 */
    public static final String SHOW_DIR_IS_EXIST          = "if [ -d {0} ];  then   echo 'DIR IS EXIST'; else   echo 'DIR IS NOT EXIST'; fi;";
    /** 创建指定目录 -- {0}为目录路径 */
    public static final String CREATE_DIR                 = "mkdir -p {0};";
    /** 复制目录下的所有内容到指定的目录中， -- {0}源目录，{1}为目标目录 */
    public static final String CP_FILE_INTO_DIR           = "cp -rf {0}/* {1};";
    /** 替换配置文件内容 -- {0}为通配信息，{1}为配置文件名称 */
    // var=0.0.0.0,6667; var=${var//,/ }; i=0; for element in $var; do echo $element {$i}; sed -i s/{$i}/$element/g `grep {$i} -rl ./`; i=$(($i+1)); done
    public static final String UPDATE_CONTENT             = "var={0}; var=${var//,/ }; i=0; for element in $var; do echo $element {$i}; sed -i s/{$i}/$element/g `grep {$i} -rl ./{1}`;  i=$(($i+1));  done";
    /** kill 占用指定端口的进程 */
    public static final String KILL_CUR_PORT              = "lsof -i:{0} | awk '{print $2}' | cut -d : -f 1 | sed '1d' | xargs kill -9";
    /** 准确kill某进程 #!/bin/bash work_path=`dirname $0`/../account/ work_path=`cd "${work_path}"; pwd` ps axu | grep 'java' | grep -v 'grep' | awk '{print $2}' | xargs pwdx | grep "${work_path}" | cut -d : -f 1 | xargs kill -9 */
    // data=$(cat gunsoulAccserver.pid);echo $data 将执行结果赋值给变量再输出，可以将纪念册存在检查拼成一条语句
    // echo $(cat gunsoulAccserver.pid) 直接输出
    // ps --no-heading $(cat gunsoulAccserver.pid) | wc -l; 查看进程是否存在 -- {0}为进程ID
    // if [ -n 'kill -9 123456' ]; then echo $(kill -9 123456); else echo 'DIR IS NOT EXIST'; fi; //关闭时如果关闭成功，输出为空，继续，否则返回错误信息
    // data=$(lsof -i:80880);if [ -n "$data" ]; then echo $data; else ps --no-heading 8080 | wc -l; fi;
    // -n string 如果 string 长度非零，则为真 [ -n $myvar ]
    /** kill服务 */
    public static final String BATCH_KILL_SHELL           = "data=$(sh kill.sh;);if [ -n \"$data\" ];  then echo $data; else   ps --no-heading 8080 | wc -l; fi;";
    /** 查看进程是否存在 */
    public static final String SHOW_PROGRESS_IS_EXIST_NEW = "ps --no-heading $(cat {0}) | wc -l;";
    /** 获取文件内容 -- {0}文件路径，{1}文件名称 */
    public static final String GET_FILE_CONTENT           = "cat {0}/{1};";
    /** 运行复制公钥脚本 -- {0}脚本路径，{1}远程服务器IP，{2}服务器密码 */
    public static final String SSH_COPT                   = "sh {0}/sshcopy.sh {1} {2} {3};";
    /** 运行复制公钥脚本 -- {0}脚本路径，{1}文件路径，{2}远程服务器IP ，{3}远程路径，{4}用户名，{5}服务器密码 */
    public static final String SSH_SCP                    = "sh {0}/sshscp.sh {1} {2} {3} {4} {5} {6}";
    /** 复制文件夹下所有内容到远程服务器 -- {0}本地文件夹路径，{1}远程服务器IP，{2}远程文件夹路径 */
    public static final String UPDATE_SCP                 = "scp -P{3} -r {0}/* {1}:{2};";
    /** 复制单个文件远程服务器 -- {0}本地文件夹路径，{1}远程服务器IP，{2}远程文件夹路径 */
    public static final String UPDATE_FILE_SCP            = "scp -P{3} -r {0} {1}:{2};";
    /** 取得某路徑下文件名稱 -- {0}文件夾路径 */
    public static final String GET_FILE_NAME_LIST         = "ls -l {0} | awk '{print $9}';";
    /** 取得某文件的MD5值 -- {0}文件全路径 */
    public static final String GET_FILE_MD5               = "md5sum {0} | awk '{print $1}';";
}
