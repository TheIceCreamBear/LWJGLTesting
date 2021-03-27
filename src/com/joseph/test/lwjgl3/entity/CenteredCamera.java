package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;

public class CenteredCamera extends Camera {
	public CenteredCamera(Vector3f pos, float pitch, float yaw, float roll) {
		super(pos, pitch, yaw, roll);
	}
	
	@Override
	public void move() {
		this.calculateZoom();
		this.calculatePitch();
		this.calculateAngleAroundPlayer();
		float horizDist = (float) (distFromPlayer * Math.cos(Math.toRadians(pitch)));
		float vertDist = (float) (distFromPlayer * Math.sin(Math.toRadians(pitch)));
		this.calculateNewPos(horizDist, vertDist);
		this.yaw = 360.0f - angleAroundPlayer;
		this.yaw %= 360.0f;
	}
	
	@Override
	protected void calculateNewPos(float horizDist, float vertDist) {
		// split that into components using Trig(gered)
		float theta = angleAroundPlayer;
		float xOff = (float) (horizDist * Math.sin(Math.toRadians(theta)));
		float zOff = (float) (horizDist * Math.cos(Math.toRadians(theta)));
		// cam pos go brr
		position.x = xOff;
		position.z = zOff;
		position.y = vertDist;
	}
}
