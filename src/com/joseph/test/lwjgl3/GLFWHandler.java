package com.joseph.test.lwjgl3;

import org.lwjgl.system.MemoryUtil;

public class GLFWHandler {
	/**
	 * A method to receive errors from GLFW
	 * @param error - the error code
	 * @param descriptionHandle - a pointer to the error string in memory. In the C/C++ level bindings,
	 * this is a 'const char*'
	 */
	public static void errorCallback(int error, long descriptionHandle) {
		System.err.println("GLFW ERROR: [" + error + "]: " + MemoryUtil.memASCII(descriptionHandle));
	}
}
