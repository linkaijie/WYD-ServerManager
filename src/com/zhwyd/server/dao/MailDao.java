package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.Mail;
public interface MailDao extends DaoSupport<Mail> {
	
    /**
     * 获取邮件信息列表
     */
	public List<Mail> getMailList(Integer gameId);
}
