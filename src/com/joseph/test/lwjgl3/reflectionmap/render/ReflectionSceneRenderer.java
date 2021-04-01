package com.joseph.test.lwjgl3.reflectionmap.render;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.reflectionmap.cubemap.CubeMap;
import com.joseph.test.lwjgl3.reflectionmap.cubemap.CubeMapRenderer;
import com.joseph.test.lwjgl3.skybox.SkyboxRenderer;

public class ReflectionSceneRenderer {
	private static final String[] ENVIRO_MAP_SNOW = {"cposx", "cnegx", "cposy", "cnegy", "cposz", "cnegz"};
	private static final String[] ENVIRO_MAP_LAKE = {"posx", "negx", "posy", "negy", "posz", "negz"};
	private static final String[] ENVIRO_MAP_INSIDE = {"lposx", "lnegx", "lposy", "lnegy", "lposz", "lnegz"};
	
	private Matrix4f projMat;
	private CubeMapRenderer environmentRenderer;
	private ReflectionEntityRenderer renderer;
	
	public ReflectionSceneRenderer(Matrix4f projMat) {
		this.projMat = projMat;
		CubeMap map = new CubeMap(ENVIRO_MAP_INSIDE);
		this.environmentRenderer = new CubeMapRenderer(map, projMat);
		this.renderer = new ReflectionEntityRenderer(projMat, map);
	}
	
	public void render(List<Entity> entities, Camera cam) {
		this.prepare();
		this.renderer.render(entities, cam);
		this.environmentRenderer.render(cam);
	}
	
	public void cleanUp() {
		this.environmentRenderer.cleanUp();
		this.renderer.cleanUp();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}
