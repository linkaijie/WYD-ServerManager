package com.zhwyd.server.dao.impl;
import java.util.List;
import java.util.Vector;
import com.zhwyd.server.bean.Mail;
import com.zhwyd.server.dao.MailDao;
public class MailDaoImpl extends DaoSupportImpl<Mail> implements MailDao {
	
	/**
	 * 获取邮件信息列表
	 */
	@SuppressWarnings("unchecked")
    public List<Mail> getMailList(Integer gameId){
		StringBuffer hql = new StringBuffer(); 
		List<Object> values = new Vector<Object>();
		hql.append(" From Mail ");
		hql.append(" Where 1 = 1 ");
//		hql.append(" and id = ");
//		values.add(gameId);
		this.getList(hql.toString(), values.toArray());
		return (List<Mail>)this.getList(hql.toString(), values.toArray());
	}
}
