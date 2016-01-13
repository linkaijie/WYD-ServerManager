package com.zhwyd.server.service;
import java.util.List;
import com.zhwyd.server.bean.Mail;
public interface MailService extends ServiceSupport<Mail> {
	
    /**
     * 获取邮件信息列表
     */
	public List<Mail> getMailList(Integer mailId);
	
}
