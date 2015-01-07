package input;

import org.lwjgl.glfw.GLFW;

public class Keyboard {
		
	public static boolean keyDown(int keycode, long window) {
		return GLFW.glfwGetKey(window, keycode) == 1;
	}
	
	public static boolean keyUp(int keycode, long window) {
		return GLFW.glfwGetKey(window, keycode) == 0;
	}
}
