package com.joseph.test.lwjgl3;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import com.joseph.test.lwjgl3.util.Point;

public class GLFWHandler {
	public static final int SCREEN_WIDTH = 1600;
	public static final int SCREEN_HEIGHT = 900;
	
	// NOT THE FINAL RESTING PLACE FOR THIS, GOOD "for now"
	public static boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
	public static boolean[] frameKeyDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
	private static double scroll = 0.0f;
	private static double dx = 0.0f;
	private static double dy = 0.0f;
	
	private static double lastX = -1.0;
	private static double lastY = -1.0;
	
	private static boolean lineMode = false;
	
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
		
		// if the user releases the T key and is pressing the left control key
		if (key == GLFW.GLFW_KEY_T && action == GLFW.GLFW_RELEASE && keyDown[GLFW.GLFW_KEY_LEFT_CONTROL]) {
			// then act like youre in a "debug mode" and switch the state of the rendering
			// from either full faces to only lines
			if (lineMode) {
				// disable line mode
				// this is how you make it go brrrrrrr and display normal polygons
				GL20.glPolygonMode(GL20.GL_FRONT_AND_BACK, GL20.GL_FILL);
				lineMode = false;
			} else {
				// endable line mode
				// this is how you make it go brrrrrrr and display only wires
				GL20.glPolygonMode(GL20.GL_FRONT_AND_BACK, GL20.GL_LINE);
				lineMode = true;
			}
		}
		
		// THIS IS TEMPORARY GROSS CODE (maybe not)
		if (action == GLFW.GLFW_PRESS) {
			keyDown[key] = true;
			frameKeyDown[key] = true;
		}
		
		// THIS IS TEMPORARY GROSS CODE (maybe not)
		if (action == GLFW.GLFW_RELEASE) {
			keyDown[key] = false;
		}
		
		// manual overwrite for a second
		// if number along top of keyboard
		if (action == GLFW.GLFW_RELEASE && key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) {
			if (keyDown[GLFW.GLFW_KEY_LEFT_CONTROL]) {
				int mode = key - GLFW.GLFW_KEY_0;
				if (mode > Main.maxSceneControl) {
					System.err.println("Failed to set new mode, out of bounds: " + mode + ">" + Main.maxSceneControl);
				}
				Main.sceneControl = mode;
				System.err.println("Setting new Scene mode: " + mode);
				
			}
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
		
		for (int i = 0; i < frameKeyDown.length; i++) {
			frameKeyDown[i] = false;
		}
	}
	
	/**
	 * Janky solution to return the current point that the mouse is hovering over
	 * (should be replaced with a frame mouse)
	 * @return
	 */
	public static Point<Float> getMousePos() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(Main.windowPointer, x, y);
		return new Point<Float>((float) x.get(0), (float) y.get(0));
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
