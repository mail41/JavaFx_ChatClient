package ch.fixy.client;

import java.util.concurrent.ConcurrentHashMap;

public class UserProperties {
	private final static ConcurrentHashMap<String, Object> userProperties = new ConcurrentHashMap<>();
	
	private UserProperties() {}
	
	public static ConcurrentHashMap<String, Object> getUserInfo() {
		return userProperties;
	}
}