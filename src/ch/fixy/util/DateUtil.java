package ch.fixy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	private DateUtil() {}
	
	private static DateTimeFormatter dtf;
	
	public static String getDate() {
		if (dtf == null) {
			initDefault();
		}
		
		return LocalDateTime.now().format(dtf);
	};
	
	/**
	 * "2021-12-20 20:10:00"
	 */
	public static void initDefault() {
		dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}
	
	public static void dateFormat(String formagString) {
		dtf = DateTimeFormatter.ofPattern(formagString);
	}
}