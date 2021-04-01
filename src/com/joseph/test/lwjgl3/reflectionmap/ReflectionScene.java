package com.joseph.test.lwjgl3.reflectionmap;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.CenteredCamera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.models.obj.OBJLoader;
import com.joseph.test.lwjgl3.reflectionmap.render.ReflectionSceneRenderer;
import com.joseph.test.lwjgl3.renderer.postprocess.Fbo;
import com.joseph.test.lwjgl3.textures.Texture;
import com.joseph.test.lwjgl3.textures.TextureLoader;

public class ReflectionScene {
	private Fbo multisampleFbo;
	private Camera cam;
	private List<Entity> entities;
	private ReflectionSceneRenderer renderer;
	
	public ReflectionScene(Matrix4f projMat) {
		multisampleFbo = new Fbo(GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT, false);
		cam = new CenteredCamera(new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f);
		renderer = new ReflectionSceneRenderer(projMat);

		entities.add(new Entity(loadModel("res/provided/reflection/meta.obj", "res/provided/reflection/meta.png"), new Vector3f(4.0f, 1.0f, 0.0f), 0.5f));
		entities.add(new Entity(loadModel("res/provided/reflection/tea.obj", "res/provided/reflection/tea.png"), new Vector3f(0.3f, 1, 0), 0.34f));
		entities.add(new Entity(loadModel("res/provided/reflection/dragon.obj", "res/provided/reflection/dragon.png"), new Vector3f(-4, 1, 0), 0.3f));
	}
	
	public void renderFull() {
		cam.move();
		
		renderer.render(entities, cam);
		
		// resolve fbo
		multisampleFbo.resolveToScreen();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		multisampleFbo.cleanUp();
	}
	
	private TexturedModel loadModel(String modelFile, String textureFile) {
		RawModel model = ModelLoader.instance.loadToVAO(OBJLoader.loadObjModel(modelFile));
		Texture texture = TextureLoader.loadTexture(textureFile);
		return new TexturedModel(model, texture);
	}
}
