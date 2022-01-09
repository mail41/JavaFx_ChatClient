package ch.fixy.util;

import java.util.Optional;

public class StringUtil {
	public static boolean isNotNull(String str) {
		return Optional.ofNullable(str)
					   .filter(elem -> elem.trim().length() >= 1)
					   .isPresent();
	}
}