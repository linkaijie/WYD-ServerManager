package com.zhwyd.server.common.util;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;
//import org.apache.struts2.ServletActionContext;

public class I18NUtil {
    public static ResourceBundle returnBundle(HttpSession session) {
        String currentString = "";
        // 避免在默认语言进入系统时session获取到该属性的值为空
        if (session.getAttribute("WW_TRANS_I18N_LOCALE") != null) {
        	// 通过session获取当前语言,格式是zh_CN,vi_VN,en_US
            currentString = session.getAttribute("WW_TRANS_I18N_LOCALE").toString();
        } else {
        	// 当前系统默认语言
            Locale defaultLocale = Locale.getDefault();
            if (defaultLocale.toString() != "") {
                currentString = defaultLocale.toString();
            } else {
            	// 如果当前语言获取失败则默认为中文zh_CN
                currentString = "zh_CN";
            }
        }
        String frontString = currentString.substring(0, currentString.indexOf('_'));//
        String backString = currentString.substring(currentString.indexOf('_') + 1, currentString.length());
        Locale currentLocale = new Locale(frontString, backString);
        // 创建当前语言的local对象,new Locale("zh", "CN");
        ResourceBundle bundle = ResourceBundle.getBundle("messageResource", currentLocale);
        return bundle;
    }
    
//    public static ResourceBundle boundBundle(){
//    	String currentString = "";
//    	//获取请求对象
//    	HttpServletRequest request = ServletActionContext.getRequest();
//    	//国际化常量非空时
//    	if (request.getAttribute("WW_TRANS_I18N_LOCALE") != null) {
//			currentString = request.getAttribute("WW_TRANS_I18N_LOCALE").toString();
//		} else {
//			//国际化常量为空时，默认获取本地
//			Locale locale = Locale.getDefault();
//			if (locale.toString() != "") {
//				currentString = locale.toString();
//			}else
//				currentString = "zh_CN";
//		}
//    	String frontString = currentString.substring(0, currentString.indexOf('_'));
//    	String backString = currentString.substring(currentString.indexOf('_') + 1, currentString.length());
//    	Locale currentLocale = new Locale(frontString, backString);
//
//    	ResourceBundle bundle = ResourceBundle.getBundle("messageResource", currentLocale);
//    	return bundle;  	
//    }
}

