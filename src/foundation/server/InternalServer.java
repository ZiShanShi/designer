package foundation.server;

import org.apache.log4j.Logger;

import foundation.config.ConfigLoaderContainer;
import foundation.config.Configer;
import foundation.config.IPreloader;
import foundation.config.MainConfigLoader;
import foundation.schedule.Schedule;

public class InternalServer {

	public static final boolean Terminate = false;
	private static Logger logger;
	private static InternalServer instance;
	private static String version;
	public static boolean Terminated;

	public InternalServer() {
		version = "3.0.0.1";
	}

	public static void startService() {
		try {
			logger = Logger.getLogger("foundation.server.InternalServer");

			instance = new InternalServer();

			instance.beforeStart();
			try {
				// 入口
				instance.start();
				printStartUpMessage();
			} finally {
				instance.afterStart();
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public static void stopService() {
		try {
			instance.stop();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void start() throws Exception {
		try {
			logger.info("internal server is startting...");
			logger.debug("internal server root path is " + Configer.getPath_Application());

			// 主配置加载器 加载配置参数（单例）
			MainConfigLoader mainConfigLoader = MainConfigLoader.getInstance();
			mainConfigLoader.load();

			ConfigLoaderContainer configLoaderContainer = ConfigLoaderContainer.getInstance();

			// 循环加载其它数据，完成初始化
			for (IPreloader configLoader : configLoaderContainer) {
				if (configLoader.isActive()) {
					logger.info("load config " + configLoader.getName());
					configLoader.load();
				} else {
					logger.info("config loader [" + configLoader.getName() + "] is not acitve");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("internal server has been started!");
		}
	}

	public void beforeStart() {

	}

	private void afterStart() {

	}

	public void stop() {
		logger.info("internal server is stopping...");

		Schedule.shutdown();

		logger.info("internal server has been stopped");
	}

	public void restart() throws Exception {
		stop();
		start();
	}

	private static String echoString(String word, int count) {
		StringBuilder result = new StringBuilder();

		for (int i = 1; i <= count; i++) {
			result.append(word);
		}

		return result.toString();
	}

	private static void printStartUpMessage() {
		int length_total = 40;

		long total = Math.round((Runtime.getRuntime().totalMemory())
				/ (1024.0 * 1024));
		long max = Math.round((Runtime.getRuntime().maxMemory())
				/ (1024.0 * 1024));

		String envirenment = Configer.getParam("EnvironmentName");
		System.out.println("--->>> " + envirenment + " --->>>");

		System.out.println(echoString("*", length_total + 2));
		printMessage("Total Memory:" + total + "M", length_total);
		printMessage("Max Memory:" + max + "M", length_total);
		printMessage(version, length_total);
		System.out.println(echoString("*", length_total + 2));
	}

	private static void printMessage(String message, int totalLength) {
		MessagePos messagePos = new MessagePos(totalLength, message);
		System.out.println("*" + echoString(" ", messagePos.length_begin)
				+ message + echoString(" ", messagePos.length_end) + "*");
	}

	private static class MessagePos {
		private int length_begin;
		private int length_end;
		private int length_message;

		public MessagePos(int totalLength, String message) {
			length_message = message.length();
			length_begin = (totalLength - length_message) / 2;
			length_end = totalLength - length_begin - length_message;
		}
	}
}
