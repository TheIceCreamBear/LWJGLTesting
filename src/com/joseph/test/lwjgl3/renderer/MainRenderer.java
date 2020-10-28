package com.joseph.test.lwjgl3.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.StaticShader;
import com.joseph.test.lwjgl3.shaders.TerrainShader;
import com.joseph.test.lwjgl3.terrain.Terrain;

/**
 * IDK if i have done whole class docs like this before but imma do it so basically this is really bad design IMO
 * because like whi is this a class that you an make an instance of like this imo should be static or designed diferent
 * but like thats not the full purpose but like also is of this project yay
 *
 */
public class MainRenderer {
	// bruv, what if le user wants to change this (although i dont like that idea so)
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private Matrix4f projMatrix;
	
	// entity stuff
	private EntityRenderer eRender;
	private StaticShader sShader;
	
	// terrain stuff
	private TerrainRenderer tRender;
	private TerrainShader tShader;
	
	private HashMap<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains;
	
	public MainRenderer() {
		enableCulling();
		
		// create the projection matrix
		this.createProjectionMatrix();
		
		this.sShader = new StaticShader();
		this.eRender = new EntityRenderer(sShader, projMatrix);
		
		this.tShader = new TerrainShader();
		this.tRender = new TerrainRenderer(tShader, projMatrix);
		
		this.entities = new HashMap<TexturedModel, List<Entity>>();
		this.terrains = new ArrayList<Terrain>();
	}
	
	/**
	 * render the saved entities in this object (wait why isnt this class static, i feel like this 
	 * should be a singleton or really just static) with the given world light or, "sun" and the given cam
	 * @param worldLight - a global light (how do we do more than one light because like thatll be important)
	 * @param camera - the camera
	 */
	public void render(Light worldLight, Camera camera) {
		// this will clear the current frame buffer of its contents and set the pixels to the 
		// pixel color specified in the clearColor funciton call above
		this.prepare();
		// start le shader
		sShader.start();
		// load the light and view matrix
		sShader.loadLight(worldLight);
		sShader.loadViewMatrix(camera);
		// render everything
		eRender.render(entities);
		// stop shader
		sShader.stop();
		tShader.start();
		tShader.loadLight(worldLight);
		tShader.loadViewMatrix(camera);
		tRender.render(terrains);
		tShader.stop();
		
		// remove entities (for some reason think this could be handeled a different way instead of clearing it tho the only thing that
		// is wasted is the ArrayList because the contents are stored else where
		entities.clear();
		terrains.clear();
	}
	
	/**
	 * This method contains things that need to happen at the beginning of every frame i think,
	 * essentially preparing for the next frame to be drawn
	 */
	public void prepare() {
		// this will clear the current frame buffer of its contents and set the pixels to the 
		// pixel color specified in the clearColor funciton call above
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Creates a projection matrix to project the z axis and stuff
	 */
	private void createProjectionMatrix() {
		// TODO these should not be constant
		float width = 1600;
		float height = 900;
		float aspectRatio = width / height;
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projMatrix = new Matrix4f();
		projMatrix.m00(xScale);
		projMatrix.m11(yScale);
		projMatrix.m22(-(FAR_PLANE + NEAR_PLANE) / frustumLength);
		projMatrix.m23(-1.0f);
		projMatrix.m32(-(2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projMatrix.m33(0.0f);
	}
	
	public void addTerrain(Terrain t) {
		terrains.add(t);
	}
	
	/**
	 * add the E to the list of entities for its given tex model
	 * @param e
	 */
	public void addEntity(Entity e) {
		// get le tex model
		TexturedModel texMod = e.getModel();
		// get the list of entities if it exists
		List<Entity> list = entities.get(texMod);
		// if we dont have the list for that entity type, make a new one
		if (list == null) {
			// make it
			list = new ArrayList<Entity>();
			// put it in the list for later
			entities.put(texMod, list);
		}
		// actually add the darn thing
		list.add(e);
	}
	
	/**
	 * okay so this is gonna be commented out because it is REALLY BAD CODE but im including it here
	 * because i want to meme on the tutorial for how bad the code here is when like my slight modification to
	 * it above is much more simpler words arent the best for me anyway enjoy this cursed code
	 * 
	 * this will be deleted in TUT 16 FINAL (if i dont forget, prob gonna forget, oh well)
	 * @param e
	 */
	/*
	public void addEntityBAD_BAD_BAD_BAD(Entity e) {
		// first two lines are the same
		TexturedModel texMod = e.getModel();
		List<Entity> list = entities.get(texMod);
		
		// this is where it gets bad
		// BRO THIS IS SO BAD WHY ARE YOU DOING THIS LIKE HERE LEMME SHOW YOU WHY ITS BAD
		if (list != null) {
			// this line is repeated in the other case, and the only purpose of the if is so it can run this ONE LINE
			list.add(e);
		} else {
			// this else is just bad in general cause you dont need an else if you do it right and arent bad but like anyway
			// as you can see the creation of the list happens here which is normal and everything
			list = new ArrayList<Entity>();
			entities.put(texMod, list);
			// bUT AGAIN THIS IS REPEATED LIKE WHY DUDE JUT DO IT THE RIGHT WAY
			list.add(e);
			// like this entire else block is unnecessary, you could move the repeated line outside the control structure
			// because both decision branches execute that line, which then means that you dont need an if check for != null
			// because there is no code there, which then means you dont need the if, which means now you have a floating else
			// which then means that you just make the else into the if with the == null check rather than the != null check like
			// hello that seems so much smarter to do but NOOOOO that is like not what they do so like BRUHHHHHHHHHHHH
		}
	}
	 */
	
	/**
	 * clean up, clean up, everybody everywhere, clean up, clean up, come on do your share
	 */
	public void cleanUp() {
		sShader.cleanUp();
		tShader.cleanUp();
	}
	
	/**
	 * i mean this should be obvious but still docs is nice enables culling
	 */
	public static void enableCulling() {
		// enable the like renderer api to like, decide to not render faces that we cant see so like performance gains
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * i mean this should be obvious but still docs is nice disables culling
	 */
	public static void disableCulling() {
		// disable the removal of the cull feature of faces, i want all the faces
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}
