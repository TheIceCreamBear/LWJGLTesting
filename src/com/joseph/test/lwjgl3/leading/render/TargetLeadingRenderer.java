package com.joseph.test.lwjgl3.leading.render;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;

public class TargetLeadingRenderer {
	private Matrix4f projMat;
	private TargetLeadingEntityRenderer entity;
	
	public TargetLeadingRenderer(Matrix4f projMat) {
		this.projMat = projMat;
		this.entity = new TargetLeadingEntityRenderer(projMat);
	}

	public void render(List<Entity> entities, Camera cam) {
		this.prepare();
		this.entity.render(entities, cam);
	}
	
	public void cleanUp() {
		this.entity.cleanUp();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}
