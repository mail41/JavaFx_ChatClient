package ch.fixy.util;

import java.util.Random;

public class RandomUtil {
	private static Random random = new Random();
	
	public static int getRandom(int min, int max) {
		return random.nextInt((max-min)) + min;
	}
}
