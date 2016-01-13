package com.zhwyd.server.servlet;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;
public class Log4InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // @Autowired
    // private ServerService serverService;

    /**
     * <li>HttpServlet的初始化方法</li>
     * <li>调用该方法来初始化Log4j日志系统</li>
     */
    @Override
    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j");
        if (file != null) {
            System.out.println("log4j 初始化成功");
            PropertyConfigurator.configure(prefix + file);
        } else {
            System.out.println("log4j 初始化失败");
        }
    }
}
