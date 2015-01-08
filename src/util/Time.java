package util;

public class Time {

	public static float dt; //delta in ms
	
	private static long lastTime;
		
	public static void update() {
		long time = System.nanoTime();
		dt = (time - lastTime) / 1000000f;
		lastTime = time;
	}
	
}
