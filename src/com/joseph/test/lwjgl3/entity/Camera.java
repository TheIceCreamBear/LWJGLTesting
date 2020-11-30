package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.Main;

/**
 * Una camera, only one camera, and it is a view, and you see it, and it is now a thing that 
 * you can see yes, meaning you can move things around
 * @author Joseph
 */
public class Camera {
	private static final float CAM_SPEED = 0.5f;
	private Vector3f position = new Vector3f(100.0f, 35.0f, 50.0f);
	private float pitch = 10.0f;
	private float yaw;
	private float roll;
	
	private Player player;
	
	private float distFromPlayer = 50.0f; // zoom
	private float angleAroundPlayer = 0.0f;
	
	public Camera(Player p) {
		this.player = p;
	}
	
	public void move() {
		this.calculateZoom();
		this.calculatePitch();
		this.calculateAngleAroundPlayer();
		this.calculateNewPos();
		this.yaw = 180 - (player.getRoty() + angleAroundPlayer);
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
	
	private void calculateNewPos() {
		// horizontal distance from the player
		float horizDist = (float) (distFromPlayer * Math.cos(Math.toRadians(pitch)));
		// split that into components using Trig(gered)
		float theta = player.getRoty() + angleAroundPlayer;
		float xOff = (float) (horizDist * Math.sin(Math.toRadians(theta)));
		float zOff = (float) (horizDist * Math.cos(Math.toRadians(theta)));
		// cam pos go brr
		position.x = player.getPos().x - xOff;
		position.z = player.getPos().z - zOff;
		// yup, same stuff but vertical with head offset
		float vertDist = (float) (distFromPlayer * Math.sin(Math.toRadians(pitch)));
		float atHead = 5.0f;
		position.y = player.getPos().y + vertDist + atHead;
	}
	
	private void calculateZoom() {
		double sensitivity = 2.0;
		double zoomL = GLFWHandler.getScroll() * sensitivity;
		this.distFromPlayer -= zoomL;
	}
	
	private void calculatePitch() {
		// THIS IS SOOOOOOOOOOOOO BAD LIKE NO NO NO BUT BUT BUT this is temp so we all gucci (dont do it 
		// like this later pls)
		if (GLFW.glfwGetMouseButton(Main.windowPointer, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
			double pitchChange = GLFWHandler.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if (GLFW.glfwGetMouseButton(Main.windowPointer, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
			double angleChange = GLFWHandler.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
