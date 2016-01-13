package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.BattleServerModel;
import com.zhwyd.server.dao.BattleServerDao;
import com.zhwyd.server.service.BattleServerService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.web.page.Pager;
public class BattleServerServiceImpl extends ServiceSupportImpl<BattleServer> implements BattleServerService {
    protected BattleServerDao battleServerDao;

    public void setBattleServerDao(BattleServerDao battleServerDao) {
        super.setDaoSupport(battleServerDao);
        this.battleServerDao = battleServerDao;
    }

    public Pager getBattleServerList(BattleServerModel redisConfigModel) {
        return battleServerDao.getBattleServerList(redisConfigModel);
    }

    /**
     * 获取页面跳转类型，用于页面跳转
     * @param operateType   操作类型
     * @return              页面跳转类型
     */
    public String getReturnType(String operateType) {
        String returnType = "";
        if (operateType.equals("start")) {
            returnType = "battleserver/startBattleLog";
        } else if (operateType.equals("stop")) {
            returnType = "battleserver/stopBattleLog";
        }
        return returnType;
    }

    /**
     * 操作battle
     */
    public void operateBattle(HttpSession session, Properties properties) throws Exception {
        if (properties != null && !StringUtils.isEmpty(properties.getProperty("ids"))) {
            String[] battleIds = properties.getProperty("ids").split(",");
            String operateType = properties.getProperty("operateType");
            String sessionVoListName = properties.getProperty("sessionVoListName");
            String sessionBeanListName = properties.getProperty("sessionBeanListName");
            List<BeanInterface> battleBeanInterfaceList = new ArrayList<BeanInterface>();
            List<BatchOpearVo> operateBattleVoList = new ArrayList<BatchOpearVo>();
            BattleServer battleServer = null;
            BatchOpearVo batchOpearVo = null;
            for (int i = 0; i < battleIds.length; i++) {
                batchOpearVo = new BatchOpearVo();
                int battleId = Integer.valueOf(battleIds[i]);
                battleServer = this.get(battleId);
                batchOpearVo.setId(battleServer.getId());
                batchOpearVo.setName(battleServer.getName());
                Application.getManager().getSimpleThreadPool().execute(this.operateBattleTask(battleServer, batchOpearVo, operateType));
                battleBeanInterfaceList.add(battleServer);
                operateBattleVoList.add(batchOpearVo);
                System.out.println("===battleId===" + battleIds[i]);
            }
            if (operateType.equals("start")) {
                session.setAttribute(sessionVoListName, operateBattleVoList);
                session.setAttribute(sessionBeanListName, battleBeanInterfaceList);
            } else if (operateType.equals("stop")) {
                session.setAttribute(sessionVoListName, operateBattleVoList);
                session.setAttribute(sessionBeanListName, battleBeanInterfaceList);
            }
        }
    }

    /**
     * 创建新的启动redis任务
     */
    private Runnable operateBattleTask(BattleServer battleServer, BatchOpearVo batchOpearVo, String operateType) {
        return new OperateBattleThread(battleServer, batchOpearVo, operateType);
    }
    /**
     * 批量启动reids线程类
     */
    public class OperateBattleThread implements Runnable {
        BattleServer battleServer;
        BatchOpearVo batchOpearVo;
        String       operateType;

        public OperateBattleThread(BattleServer battleServer, BatchOpearVo batchOpearVo, String operateType) {
            this.battleServer = battleServer;
            this.batchOpearVo = batchOpearVo;
            this.operateType = operateType;
        }

        @Override
        public void run() {
            try {
                if (operateType.equals("start")) {// 开启
                    batchOpearVo.setRemark("准备开启");
                    doStartBattle(battleServer, batchOpearVo);
                } else if (operateType.equals("stop")) {// 关闭
                    batchOpearVo.setRemark("准备关闭");
                    doStopBattle(battleServer, batchOpearVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启战斗服服务
     * @param battleServer
     * @param batchOpearVo
     */
    public void doStartBattle(BattleServer battleServer, BatchOpearVo batchOpearVo) {
        Server server = Application.getBean(ServerService.class).get(battleServer.getServerId());
        BattleServerService battleServerService = Application.getBean(BattleServerService.class);
        try {
            if (battleServer.getIsDeploy() == CommanConstant.STATE_STOP || battleServer.getState() == CommanConstant.STATE_START) {
                batchOpearVo.isFail("战斗服未部署或者已开启");
                return;
            }
            batchOpearVo.setRemark("战斗服开启中");
            // 检测该服务是否在运行中
            boolean result = SshxcuteUtils.progressIsExistNew(server, battleServer.getPath(), Global.BATTLE_PID_FILE);
            if (!result) {
                Object[] runResult = SshxcuteUtils.runExec(server, battleServer.getPath());
                // 启动出错时关闭该服务
                if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != 0) {
                    SshxcuteUtils.killExec(server, battleServer.getPath());
                    batchOpearVo.isFail("战斗服启动失败，请检查");
                } else {
                    int i = 0;
                    while (true) {
                        String stdout = SshxcuteUtils.getBatchStdout(battleServer, "战斗服务器启动");
                        if (!StringUtils.isEmpty(stdout) && (stdout.equals("true") || stdout.equals("false"))) {
                            if (stdout.equals("true")) {
                                battleServer.setState(CommanConstant.STATE_START);
                                CacheService.getBattleServer(battleServer.getId()).setState(CommanConstant.STATE_START);
                                battleServerService.update(battleServer);
                                batchOpearVo.isSUCCESS("战斗服启动成功");
                            } else if (stdout.equals("false")) {
                                batchOpearVo.isFail("战斗服启动失败");
                                SshxcuteUtils.killExec(server, battleServer.getPath());
                            }
                            break;
                        }
                        Thread.sleep(1500l);
                        if (i >= 90) {
                            batchOpearVo.isFail("战斗服启动超时");
                            break;
                        }
                        i++;
                    }
                }
            } else {
                batchOpearVo.isFail("战斗服未关闭，请检查");
            }
        } catch (Exception e) {
            batchOpearVo.isFail("未知错误，请检查");
            e.printStackTrace();
        }
    }

    /**
     * 关闭战斗服服务
     * @param battleServer
     * @param batchOpearVo
     */
    public void doStopBattle(BattleServer battleServer, BatchOpearVo batchOpearVo) {
        Server server = Application.getBean(ServerService.class).get(battleServer.getServerId());
        BattleServerService battleServerService = Application.getBean(BattleServerService.class);
        try {
            if (battleServer.getIsDeploy() == CommanConstant.STATE_STOP || battleServer.getState() == CommanConstant.STATE_STOP) {
                batchOpearVo.isFail("战斗服未部署或者已关闭");
                return;
            }
            batchOpearVo.setRemark("战斗服关闭中");
            // 检测该服务是否在运行中
            boolean result = SshxcuteUtils.progressIsExistNew(server, battleServer.getPath(), Global.BATTLE_PID_FILE);
            if (result) {
                Object[] runResult = SshxcuteUtils.killExec(server, battleServer.getPath());
                if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != 0) {// 启动出错时关闭该服务
                    batchOpearVo.isFail("战斗服关闭失败，请检查");
                } else {
                    boolean progressIsExist = SshxcuteUtils.progressIsExistNew(server, battleServer.getPath(), Global.BATTLE_PID_FILE);
                    if (!progressIsExist) {
                        battleServer.setState(CommanConstant.STATE_STOP);
                        CacheService.getBattleServer(battleServer.getId()).setState(CommanConstant.STATE_STOP);
                        battleServerService.update(battleServer);
                        batchOpearVo.isSUCCESS("战斗服完成");
                    } else {
                        batchOpearVo.isFail("关闭失败");
                    }
                }
            } else {
                batchOpearVo.isFail("战斗服未开启，请检查");
            }
        } catch (Exception e) {
            batchOpearVo.isFail("未知错误，请检查");
            e.printStackTrace();
        }
    }
}
