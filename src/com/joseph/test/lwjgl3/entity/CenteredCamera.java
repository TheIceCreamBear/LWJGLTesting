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
}
