package com.joseph.test.lwjgl3;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class GLFWHandler {
	// NOT THE FINAL RESTING PLACE FOR THIS, GOOD "for now"
	public static boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
	private static double scroll = 0.0f;
	private static double dx = 0.0f;
	private static double dy = 0.0f;
	
	private static double lastX = -1.0;
	private static double lastY = -1.0;
	
	/**
	 * A method to receive errors from GLFW
	 * @param error - the error code
	 * @param descriptionHandle - a pointer to the error string in memory. In the C/C++ level bindings,
	 * this is a 'const char*'
	 */
	public static void errorCallback(int error, long descriptionHandle) {
		System.err.println("GLFW ERROR: [" + error + "]: " + MemoryUtil.memASCII(descriptionHandle));
	}
	
	public static void keyboardCallback(long window, int key, int scancode, int action, int mods) {
		// this is what will be called when the poolEvents() method is called in the loop portion of the code
		// all key presses will be passed here
		// an example is using the escape key as the determining factor of when the window should close
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
			// this will tell the window it should close. it DOES NOT actually close the window
			// this will cause our main loop to exit and stop.
			GLFW.glfwSetWindowShouldClose(window, true);
		}
		
		// THIS IS TEMPORARY GROSS CODE
		if (action == GLFW.GLFW_PRESS) {
			keyDown[key] = true;
		}

		// THIS IS TEMPORARY GROSS CODE
		if (action == GLFW.GLFW_RELEASE) {
			keyDown[key] = false;
		}
	}
	
	public static void scrollCallback(long window, double xOff, double yOff) {
		scroll += yOff;
	}
	
	public static void cursorPosCallback(long window, double xPos, double yPos) {
		// this method gets called once right after the window is shown and setup, meaning that this will only happen once
		if (lastX == 1.0 && lastY == 1.0) {
			lastX = xPos;
			lastY = yPos;
			return;
		}
		
		dx = xPos - lastX;
		dy = yPos - lastY;

		lastX = xPos;
		lastY = yPos;
	}
	
	public static void frameReset() {
		dx = 0;
		dy = 0;
		scroll = 0;
	}
	
	// = = = = = = = = = = this is bloop = = = = = = = =
	// this is still here incase the something needs to be done to the 
	// values before they are used
	
	public static double getScroll() {
		return scroll;
	}
	
	public static double getDX() {
		return dx;
	}
	
	public static double getDY() {
		return dy;
	}
}
