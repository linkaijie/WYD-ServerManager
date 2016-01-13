package com.zhwyd.server.dao.impl;
import java.util.List;
import java.util.Vector;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.dao.ModelDao;
public class ModelDaoImpl extends DaoSupportImpl<Model> implements ModelDao {
	
	/**
	 * 获取模板对象
	 * author:LKJ 
	 * 2014-9-4 
	 * @param gameId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Model> getModelList(Integer gameId){
		StringBuffer hql = new StringBuffer(); 
		List<Object> values = new Vector<Object>();
		hql.append(" From Model ");
		hql.append(" Where 1 = 1 ");
		hql.append(" AND gameId = ? ");
		values.add(gameId);
		this.getList(hql.toString(), values.toArray());
		return (List<Model>)this.getList(hql.toString(), values.toArray());
	}
}
