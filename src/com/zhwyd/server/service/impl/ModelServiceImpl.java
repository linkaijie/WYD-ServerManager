package com.zhwyd.server.service.impl;
import java.util.List;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.dao.ModelDao;
import com.zhwyd.server.service.ModelService;
public class ModelServiceImpl extends ServiceSupportImpl<Model> implements ModelService {
    protected ModelDao modelDao;

    public void setModelDao(ModelDao modelDao) {
        super.setDaoSupport(modelDao);
        this.modelDao = modelDao;
    }

    /**
	 * 获取模板列表
	 * author:LKJ 
	 * 2014-9-4 
	 * @param gameId
	 * @return
	 */
	public List<Model> getModelList(Integer gameId){
	    if (gameId == null) {
            gameId = 1;
        }
		List<Model> modelList = modelDao.getModelList(gameId);
		return modelList;
	}
    
}
