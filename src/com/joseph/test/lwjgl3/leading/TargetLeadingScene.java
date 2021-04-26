package com.joseph.test.lwjgl3.leading;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.CenteredCamera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.grid.GridVao;
import com.joseph.test.lwjgl3.leading.render.TargetLeadingRenderer;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.models.obj.OBJLoader;
import com.joseph.test.lwjgl3.renderer.postprocess.Fbo;
import com.joseph.test.lwjgl3.textures.Texture;
import com.joseph.test.lwjgl3.textures.TextureLoader;

public class TargetLeadingScene {
	private Fbo multisampleFbo;
	private Camera cam;
	private List<Entity> entities;
	private TargetLeadingRenderer renderer;
	private Entity dirIndicator;
	private float alive = 0.0f;
	
	public TargetLeadingScene(Matrix4f projMat) {
		multisampleFbo = new Fbo(GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT, false);
		// TODO diff cam eventually
		cam = new CenteredCamera(new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f);
		renderer = new TargetLeadingRenderer(projMat);
		entities = new ArrayList<Entity>();
		
		dirIndicator = new Entity(loadModel("res/target/targetcylinder.obj", "res/red.png"), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
		entities.add(dirIndicator);
	}
	
	public void renderFull() {
		multisampleFbo.bindFrameBuffer();
		
		cam.move();
		
		this.alive += Main.delta;
		dirIndicator.increaseRotation(0.0f, 60.f * Main.delta, 0.0f);
//		dirIndicator.setRotx((float) (Math.sin(alive * 10.0f) * 10.0f));
		
		renderer.render(entities, cam);
		
		// resolve fbo
		multisampleFbo.resolveToScreen();
	}

	public void cleanUp() {
		this.renderer.cleanUp();
	}

	private TexturedModel loadModel(String modelFile, String textureFile) {
		RawModel model = ModelLoader.instance.loadToVAO(OBJLoader.loadObjModel(modelFile));
		Texture texture = TextureLoader.loadTexture(textureFile);
		return new TexturedModel(model, texture);
	}
}
