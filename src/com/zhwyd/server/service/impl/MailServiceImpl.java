package com.zhwyd.server.service.impl;
import java.util.List;
import com.zhwyd.server.bean.Mail;
import com.zhwyd.server.dao.MailDao;
import com.zhwyd.server.service.MailService;
public class MailServiceImpl extends ServiceSupportImpl<Mail> implements MailService {
    protected MailDao mailDao;

    public void setMailDao(MailDao mailDao) {
        super.setDaoSupport(mailDao);
        this.mailDao = mailDao;
    }

    /**
     * 获取邮件信息列表
     */
	public List<Mail> getMailList(Integer gameId){
	    if (gameId == null) {
	        gameId =1;
        }
		List<Mail> gameList = mailDao.getMailList(gameId);
		return gameList;
	}
    
}
