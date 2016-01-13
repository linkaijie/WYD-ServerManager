package com.zhwyd.server.servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.sshxcute.pool.ConnectionPoolManager;
public class InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        Application.setWebRootPath(servletContext.getRealPath("/"));
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        Application.setContext(context);
        HierarchicalINIConfiguration hierarchicalINIConfiguration = context.getBean(HierarchicalINIConfiguration.class);
        Application.config(hierarchicalINIConfiguration);
        Application.getManager();
        ConnectionPoolManager.getInstance().initSurplusConnect();;
        CacheService.initAll();
    }
}
