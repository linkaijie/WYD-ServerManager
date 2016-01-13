package com.zhwyd.server.service;
import java.util.List;
import com.zhwyd.server.bean.Model;
public interface ModelService extends ServiceSupport<Model> {
	
	/**
	 * 获取模板对象
	 * author:LKJ 
	 * 2014-9-4 
	 * @param gameId
	 * @return
	 */
	public List<Model> getModelList(Integer gameId);
	
}
