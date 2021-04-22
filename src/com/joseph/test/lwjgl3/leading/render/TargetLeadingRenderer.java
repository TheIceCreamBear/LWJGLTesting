package com.joseph.test.lwjgl3.leading.render;

import java.util.List;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;

public class TargetLeadingRenderer {
	private Matrix4f projMat;
	
	public TargetLeadingRenderer(Matrix4f projMat) {
		this.projMat = projMat;
	}

	public void render(List<Entity> entities, Camera cam) {
		
	}
}
