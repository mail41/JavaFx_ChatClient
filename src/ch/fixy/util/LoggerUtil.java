package ch.fixy.util;

import java.util.logging.Logger;

public class LoggerUtil {
	
	private LoggerUtil() {}
	
	public static void info(String msg) {
		Logger.getGlobal().info(msg);
	}
	
	public static void error(String msg) {
		Logger.getGlobal().warning(msg);
	}
}
