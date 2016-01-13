package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.SshConstant;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.RedisConfigModel;
import com.zhwyd.server.dao.RedisConfigDao;
import com.zhwyd.server.service.RedisConfigService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.sshxcute.SSHExces;
import com.zhwyd.server.web.page.Pager;
public class RedisConfigServiceImpl extends ServiceSupportImpl<RedisConfig> implements RedisConfigService {
    protected RedisConfigDao redisConfigDao;

    public void setRedisConfigDao(RedisConfigDao redisConfigDao) {
        super.setDaoSupport(redisConfigDao);
        this.redisConfigDao = redisConfigDao;
    }

    public Pager getRedisConfigList(RedisConfigModel redisConfigModel) {
        return redisConfigDao.getRedisConfigList(redisConfigModel);
    }

    /**
     * 获取页面跳转类型，用于页面跳转
     * @param operateType   操作类型
     * @return              页面跳转类型
     */
    public String getReturnType(String operateType) {
        String returnType = "";
        if (operateType.equals("start")) {
            returnType = "redisconfig/startRedisLog";
        } else if (operateType.equals("stop")) {
            returnType = "redisconfig/stopRedisLog";
        }
        return returnType;
    }

    /**
     * 批量启动World和Dispatch
     */
    public void operateRedis(HttpSession session, String ids, String operateType) throws Exception {
        if (!StringUtils.isEmpty(ids)) {
            String[] redisIds = ids.split(",");
            List<BatchOpearVo> operateRedisVoList = new ArrayList<BatchOpearVo>();
            List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
            RedisConfig redisConfig = null;
            BatchOpearVo batchOpearVo = null;
            for (int i = 0; i < redisIds.length; i++) {
                batchOpearVo = new BatchOpearVo();
                int redisId = Integer.valueOf(redisIds[i]);
                redisConfig = this.get(redisId);
                batchOpearVo.setId(redisConfig.getId());
                batchOpearVo.setName(redisConfig.getName());
                Application.getManager().getSimpleThreadPool().execute(this.operateRedisTask(redisConfig, batchOpearVo, operateType));
                beanInterfaceList.add(redisConfig);
                operateRedisVoList.add(batchOpearVo);
                System.out.println("===redisId===" + redisIds[i]);
            }
            if (operateType.equals("start")) {
                session.setAttribute("startRedisVo", operateRedisVoList);
                session.setAttribute("startRedisList", beanInterfaceList);
            } else if (operateType.equals("stop")) {
                session.setAttribute("stopRedisVo", operateRedisVoList);
                session.setAttribute("stopRedisList", beanInterfaceList);
            }
        }
    }

    /**
     * 创建新的启动redis任务
     */
    private Runnable operateRedisTask(RedisConfig redisConfig, BatchOpearVo batchOpearVo, String operateType) {
        return new OperateRedisThread(redisConfig, batchOpearVo, operateType);
    }
    /**
     * 批量启动reids线程类
     */
    public class OperateRedisThread implements Runnable {
        RedisConfig  redisConfig;
        BatchOpearVo batchOpearVo;
        String       operateType;

        public OperateRedisThread(RedisConfig redisConfig, BatchOpearVo batchOpearVo, String operateType) {
            this.redisConfig = redisConfig;
            this.batchOpearVo = batchOpearVo;
            this.operateType = operateType;
        }

        @Override
        public void run() {
            try {
                if (operateType.equals("start")) {// 开启
                    batchOpearVo.setRemark("准备开启");
                    doStartRedis(redisConfig, batchOpearVo);
                } else if (operateType.equals("stop")) {// 关闭
                    batchOpearVo.setRemark("准备关闭");
                    doStopRedis(redisConfig, batchOpearVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启redis服务
     * @param redisConfig
     * @param batchOpearVo
     */
    public void doStartRedis(RedisConfig redisConfig, BatchOpearVo batchOpearVo) {
        Server server = Application.getBean(ServerService.class).get(redisConfig.getServerId());
        RedisConfigService redisConfigService = Application.getBean(RedisConfigService.class);
        try {
            if (redisConfig.getIsDeploy() == CommanConstant.STATE_STOP || redisConfig.getState() == CommanConstant.STATE_START) {
                batchOpearVo.isFail("redis未部署或者已开启");
                return;
            }
            batchOpearVo.setRemark("redis开启中");
            int portNum = redisConfig.getPorts().split("\\|").length;
            String pidPath = redisConfig.getPath() + "/pid/*";
            String openNum = SshxcuteUtils.getProgressOpenNum(server, pidPath);
            if (openNum.equals("0")) {// 0表示没运行与改redis相关的线程
                StringBuilder command = new StringBuilder();
                command.append(SshConstant.CD_SERVER_PATH.replace("{0}", redisConfig.getPath()));
                command.append(SshConstant.RUN_SHELL);
                Object[] runResult = SSHExces.exce(server, new ExecCommand(command.toString()));
                // 启动出错时关闭该服务
                if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != 0) {
                    SshxcuteUtils.killExec(server, redisConfig.getPath());
                    batchOpearVo.isFail("redis启动失败，请检查");
                } else {
                    openNum = SshxcuteUtils.getProgressOpenNum(server, pidPath);
                    if (openNum.equals(String.valueOf(portNum))) {
                        redisConfig.setState(CommanConstant.STATE_START);
                        CacheService.getRedisConfig(redisConfig.getId()).setState(CommanConstant.STATE_START);
                        redisConfigService.update(redisConfig);
                        batchOpearVo.isSUCCESS("redis启动成功");
                    } else {
                        batchOpearVo.isFail("redis启动失败，请检查");
                    }
                }
            } else if (openNum.equals("error")) {
                batchOpearVo.isFail("redis检测pid出错");
            } else {
                batchOpearVo.isFail("redis未关闭，请检查");
            }
        } catch (Exception e) {
            batchOpearVo.isFail("未知错误，请检查");
            e.printStackTrace();
        }
    }

    /**
     * 关闭redis服务
     * @param redisConfig
     * @param batchOpearVo
     */
    public void doStopRedis(RedisConfig redisConfig, BatchOpearVo batchOpearVo) {
        Server server = Application.getBean(ServerService.class).get(redisConfig.getServerId());
        RedisConfigService redisConfigService = Application.getBean(RedisConfigService.class);
        try {
            if (redisConfig.getIsDeploy() == CommanConstant.STATE_STOP || redisConfig.getState() == CommanConstant.STATE_STOP) {
                batchOpearVo.isFail("redis未部署或者已开启");
                return;
            }
            batchOpearVo.setRemark("redis关闭中");
            String pidPath = redisConfig.getPath() + "/pid/*";
            String openNum = SshxcuteUtils.getProgressOpenNum(server, pidPath);
            if (openNum.equals("0")) {// 0表示没运行与改redis相关的线程
                batchOpearVo.isFail("redis已关闭，请检查");
            } else if (openNum.equals("error")) {
                batchOpearVo.isFail("redis检测pid出错");
            } else {
                StringBuilder command = new StringBuilder();
                command.append(SshConstant.CD_SERVER_PATH.replace("{0}", redisConfig.getPath()));
                command.append(SshConstant.KILL_SHELL);
                Object[] runResult = SSHExces.exce(server, new ExecCommand(command.toString()));
                // 启动出错时关闭该服务
                if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != 0) {
                    SshxcuteUtils.killExec(server, redisConfig.getPath());
                    batchOpearVo.isFail("关闭失败，请检查");
                } else {
                    openNum = SshxcuteUtils.getProgressOpenNum(server, pidPath);
                    if (openNum.equals("0")) {
                        redisConfig.setState(CommanConstant.STATE_STOP);
                        CacheService.getRedisConfig(redisConfig.getId()).setState(CommanConstant.STATE_STOP);
                        redisConfigService.update(redisConfig);
                        batchOpearVo.isSUCCESS("关闭成功");
                    } else {
                        batchOpearVo.isFail("关闭失败，请检查");
                    }
                }
            }
        } catch (Exception e) {
            batchOpearVo.isFail("未知错误，请检查");
            e.printStackTrace();
        }
    }
}
