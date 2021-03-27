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
	protected Vector3f position = new Vector3f(100.0f, 35.0f, 50.0f);
	protected float pitch = 10.0f;
	protected float yaw;
	protected float roll;
	
	private Player player;
	
	protected float distFromPlayer = 50.0f; // zoom
	protected float angleAroundPlayer = 0.0f;
	
	public Camera(Player p) {
		this.player = p;
	}
	
	public Camera(Vector3f pos, float pitch, float yaw, float roll) {
		this.position = pos;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	
	public void move() {
		this.calculateZoom();
		this.calculatePitch();
		this.calculateAngleAroundPlayer();
		float horizDist = (float) (distFromPlayer * Math.cos(Math.toRadians(pitch)));
		float vertDist = (float) (distFromPlayer * Math.sin(Math.toRadians(pitch)));
		this.calculateNewPos(horizDist, vertDist);
		this.yaw = 180.0f - (player.getRoty() + angleAroundPlayer);
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
	
	public void invertPitch() {
		this.pitch = -this.pitch;
	}
	
	protected void calculateNewPos(float horizDist, float vertDist) {
		// split that into components using Trig(gered)
		float theta = player.getRoty() + angleAroundPlayer;
		float xOff = (float) (horizDist * Math.sin(Math.toRadians(theta)));
		float zOff = (float) (horizDist * Math.cos(Math.toRadians(theta)));
		// cam pos go brr
		position.x = player.getPos().x - xOff;
		position.z = player.getPos().z - zOff;
		// yup, same stuff but vertical with head offset
		float atHead = 5.0f;
		position.y = player.getPos().y + vertDist + atHead;
	}
	
	protected void calculateZoom() {
		double sensitivity = 2.0;
		double zoomL = GLFWHandler.getScroll() * sensitivity;
		this.distFromPlayer -= zoomL;
		
		// clamp lower bound of distance
		this.distFromPlayer = Math.max(2.0f, distFromPlayer);
	}
	
	protected void calculatePitch() {
		// THIS IS SOOOOOOOOOOOOO BAD LIKE NO NO NO BUT BUT BUT this is temp so we all gucci (dont do it 
		// like this later pls)
		if (GLFW.glfwGetMouseButton(Main.windowPointer, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
			double pitchChange = GLFWHandler.getDY() * 0.1f;
			pitch -= pitchChange;
			
			// clamp the pitch to be in a better range
			pitch = org.joml.Math.clamp(pitch, -10.0f, 90.0f);
		}
	}
	
	protected void calculateAngleAroundPlayer() {
		if (GLFW.glfwGetMouseButton(Main.windowPointer, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
			double angleChange = GLFWHandler.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
