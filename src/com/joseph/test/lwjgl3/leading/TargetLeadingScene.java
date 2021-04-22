package com.joseph.test.lwjgl3.leading;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.CenteredCamera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.leading.render.TargetLeadingRenderer;
import com.joseph.test.lwjgl3.renderer.postprocess.Fbo;

public class TargetLeadingScene {
	private Fbo multisampleFbo;
	private Camera cam;
	private List<Entity> entities;
	private TargetLeadingRenderer renderer;
	
	public TargetLeadingScene(Matrix4f projMat) {
		multisampleFbo = new Fbo(GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT, false);
		// TODO diff cam
		cam = new CenteredCamera(new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f);
		renderer = new TargetLeadingRenderer(projMat);
		entities = new ArrayList<Entity>();
	}
	
	public void renderFull() {
		multisampleFbo.bindFrameBuffer();
		
		cam.move();
		
		renderer.render(entities, cam);
		
		// resolve fbo
		multisampleFbo.resolveToScreen();
	}

	public void cleanUp() {
		
	}
}
