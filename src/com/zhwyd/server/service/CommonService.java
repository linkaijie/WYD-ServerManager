package com.zhwyd.server.service;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.controller.model.BaseModel;
public interface CommonService {
    public String getRedirect(BaseModel baseModel, String redirectType);

    /**
     * 更新前获取更新名称列表
     * @return 
     */
    public List<String> getFileNameList(HttpServletResponse response, HttpSession session, String beanType, String pathType) throws Exception;

    public List<String> getUpdateFileNameList(HttpServletResponse response, HttpSession session, int updateType) throws Exception;
    
    /**
     * 更新远程配置文件
     * 
     * @return TODO
     */
    public String changeServerDBCfg(String ids, int serverId, int dispatchServerId, HttpSession session) throws Exception;

    public Map<WorldServer, List<DispatchServer>> getChangeServerBeanList(String[] beanIds);

    /**
     * 更新數據庫配置文件（只修改DB信息，不同步文件,單個文件）
     * 
     * @param changeServerVo
     *            TODO
     */
    public boolean changeOneServerDBCfg(ChangeServerVo changeServerVo, HttpSession session) throws Exception;

    public boolean scpFileToRemote(ChangeServerVo changeServerVo, HttpSession session) throws Exception;

    public boolean synRemoteCfg(ChangeServerVo changeServerVo, HttpSession session) throws Exception;

    /**
     * 迁服
     */
    public void changeServer(HttpSession session, String worldIds, Integer serverId, int dispatchServerId) throws Exception;
}
