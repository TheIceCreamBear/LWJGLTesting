package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.Main;

/**
 * Una camera, only one camera, and it is a view, and you see it, and it is now a thing that 
 * you can see yes, meaning you can move things around
 * @author Joseph
 */
public class Camera {
	private static final float CAM_SPEED = 0.25f;
	private Vector3f position = new Vector3f(0.0f, 5.0f, 0.0f);
	private float pitch = 10.0f;
	private float yaw;
	private float roll;
	
	public Camera() {}
	
	/**
	 * THIS IS JANKY AND NOW HOW IT SHOULD BE THIS IS TEMPORARY WHEN WILL I CHANGE IT
	 * IDFK BUT THIS IS GROSS
	 */
	public void move() {
		float speed = CAM_SPEED;
		if (Main.keyDown[GLFW.GLFW_KEY_LEFT_SHIFT]) {
			speed *= 2;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_W]) {
			position.z -= speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_S]) {
			position.z += speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_D]) {
			position.x += speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_A]) {
			position.x -= speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_SPACE]) {
			position.y += speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_C]) {
			position.y -= speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_KP_8]) {
			pitch -= 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_KP_2]) {
			pitch += 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_KP_6]) {
			yaw += 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_KP_4]) {
			yaw -= 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_E]) {
			roll += 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_Q]) {
			roll -= 2 * speed;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_R]) {
			this.position = new Vector3f(0.0f, 0.0f, 0.0f);
			this.pitch = 0.0f;
			this.yaw = 0.0f;
			this.roll = 0.0f;
		}
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getRoll() {
		return this.roll;
	}
}
