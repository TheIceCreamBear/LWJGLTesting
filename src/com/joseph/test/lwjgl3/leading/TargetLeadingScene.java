package com.joseph.test.lwjgl3.leading;

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.CenteredCamera;
import com.joseph.test.lwjgl3.entity.Entity;
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
	private VelocityEntity last;
	private float alive = 0.0f;
	private float rgbspeed = 0.75f;
	private int rgbstate = 1;
	private float r = 1.0f;
	private float g = 0.0f;
	private float b = 0.0f;
	private Vector3f rgb = new Vector3f();
	
	private TexturedModel shellModel;
	
	public TargetLeadingScene(Matrix4f projMat) {
		multisampleFbo = new Fbo(GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT, false);
		// TODO diff cam eventually
		cam = new CenteredCamera(new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f);
		renderer = new TargetLeadingRenderer(projMat);
		renderer.setGridRadius(50);
		entities = new ArrayList<Entity>();
		
		dirIndicator = new RotMatEntity(loadModel("res/target/targetcylinder.obj", "res/red.png"), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
		entities.add(dirIndicator);
		
		shellModel = loadModel("res/target/shell.obj", "res/target/ShellMaterialTexture.png");
		
		// have some init entity so things dont break
		VelocityEntity e = new VelocityEntity(shellModel, new Vector3f(-25.0f, 10.0f, -25.0f), new Vector3f(5.0f, 0.0f, 0.0f), 0.0f, 90.0f, 0.0f, 1.0f);
		entities.add(e);
		last = e;
	}
	
	public void renderFull() {
		multisampleFbo.bindFrameBuffer();
		
		cam.move();
		
		for (Entity entity : entities) {
			if (entity instanceof VelocityEntity) {
				((VelocityEntity) entity).move();
			}
		}
		
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_G]) {
			VelocityEntity e = new VelocityEntity(shellModel, new Vector3f(-25.0f, 10.0f, -25.0f), new Vector3f(5.0f, 0.0f, 0.0f), 0.0f, 90.0f, 0.0f, 1.0f);
			entities.add(e);
			last = e;
		}
		
		this.alive += Main.delta;
		dirIndicator.increaseRotation(0.0f, 60.f * Main.delta, 0.0f);
//		dirIndicator.setRotx((float) (Math.sin(alive * 10.0f) * 10.0f));
		
		float drgb = rgbspeed * Main.delta;
		
		switch (rgbstate) {
			case 0:
				b -= drgb;
				r += drgb;
				if (b <= 0.0f) {
					b = 0.0f;
				}
				if (r >= 1.0f) {
					r = 1.0f;
				}
				if (b <= 0.0f && r >= 1.0f) {
					rgbstate = 1;
				}
				break;
			case 1:
				r -= drgb;
				g += drgb;
				if (r <= 0.0f) {
					r = 0.0f;
				}
				if (g >= 1.0f) {
					g = 1.0f;
				}
				if (r <= 0.0f && g >= 1.0f) {
					rgbstate = 2;
				}
				break;
			case 2:
				g -= drgb;
				b += drgb;
				if (g <= 0.0f) {
					g = 0.0f;
				}
				if (b >= 1.0f) {
					b = 1.0f;
				}
				if (g <= 0.0f && b >= 1.0f) {
					rgbstate = 0;
				}
				break;
		}
				
		rgb.set(r, g, b);
		renderer.render(entities, rgb, cam);
		
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
