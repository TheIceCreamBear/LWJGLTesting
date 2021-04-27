package com.joseph.test.lwjgl3.leading.render;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.grid.GridRendererShader;

public class TargetLeadingRenderer {
	private Matrix4f projMat;
	private TargetLeadingEntityRenderer entity;
	private GridRendererShader grid;
	
	public TargetLeadingRenderer(Matrix4f projMat) {
		this.projMat = projMat;
		this.entity = new TargetLeadingEntityRenderer(projMat);
		this.grid = new GridRendererShader(projMat);
	}

	public void render(List<Entity> entities, Camera cam) {
		this.prepare();
		this.grid.render(cam);
		this.entity.render(entities, cam);
	}
	
	public void setGridRadius(int radius) {
		this.grid.start();
		this.grid.loadRadius(radius);
		this.grid.stop();
	}
	
	public void cleanUp() {
		this.entity.cleanUp();
		this.grid.cleanUp();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}
