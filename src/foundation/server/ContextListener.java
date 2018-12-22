package foundation.server;

import foundation.config.Configer;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

	public ContextListener() {

	}

	// 服务器启动时调用
	public void contextInitialized(ServletContextEvent event) {
		// 初始化路径
		Configer.init(event.getServletContext());

		// 加载日志配置
		PropertyConfigurator.configure(Configer.getPath_LoggerConfig());

		// 初始化各项配置数据
		InternalServer.startService();
	}

	// 服务器关闭时调用
	public void contextDestroyed(ServletContextEvent arg0) {
		InternalServer.stopService();
	}

}
