package com.joseph.test.lwjgl3.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.StaticShader;
import com.joseph.test.lwjgl3.shaders.TerrainShader;
import com.joseph.test.lwjgl3.skybox.SkyboxRenderer;
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
	
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	
	private Matrix4f projMatrix;
	
	// entity stuff
	private EntityRenderer eRender;
	private StaticShader sShader;
	
	// terrain stuff
	private TerrainRenderer tRender;
	private TerrainShader tShader;
	
	// skybox thing
	private SkyboxRenderer sRender;
	
	private HashMap<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains;
	
	public MainRenderer(ModelLoader loader) {
		enableCulling();
		
		// create the projection matrix
		this.createProjectionMatrix();
		
		this.sShader = new StaticShader();
		this.eRender = new EntityRenderer(sShader, projMatrix);
		
		this.tShader = new TerrainShader();
		this.tRender = new TerrainRenderer(tShader, projMatrix);
		
		this.sRender = new SkyboxRenderer(loader, projMatrix);
		
		this.entities = new HashMap<TexturedModel, List<Entity>>();
		this.terrains = new ArrayList<Terrain>();
	}
	
	/**
	 * Function to render a list of entites and terrains, with the given lights and to the given camera with the given delta.
	 * Useful so that you dont have to keep track of each thing that needs to get added to the list of things to render
	 * @param entities - the list of entities
	 * @param terrains - the list of terrains
	 * @param lights - a list of lights
	 * @param camera - the camera to render to
	 * @param delta - the delta time since the last frame
	 */
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane, float delta) {
		for (Terrain t : terrains) {
			this.addTerrain(t);
		}
		for (Entity e : entities) {
			this.addEntity(e);
		}
		this.render(lights, camera, clipPlane, delta);
	}
	
	/**
	 * render the saved entities in this object (wait why isnt this class static, i feel like this 
	 * should be a singleton or really just static) with the given world light or, "sun" and the given cam
	 * @param worldLight - a global light (how do we do more than one light because like thatll be important)
	 * @param camera - the camera
	 */
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane, float delta) {
		// this will clear the current frame buffer of its contents and set the pixels to the 
		// pixel color specified in the clearColor funciton call above
		this.prepare();
		// start le shader
		sShader.start();
		// load le clip plane
		sShader.loadClipPlane(clipPlane);
		// load the sky color
		sShader.loadSkyColor(RED, GREEN, BLUE);
		// load the lights and view matrix
		sShader.loadLights(lights);
		sShader.loadViewMatrix(camera);
		// render everything
		eRender.render(entities);
		// stop shader
		sShader.stop();
		// start the terrain shader
		tShader.start();
		// load le clip plane
		tShader.loadClipPlane(clipPlane);
		// load the sky color
		tShader.loadSkyColor(RED, GREEN, BLUE);
		// load the light and view matrix
		tShader.loadLights(lights);
		tShader.loadViewMatrix(camera);
		// render the terrains
		tRender.render(terrains);
		// stop the terrain shader
		tShader.stop();
		
		// render the sky box
		sRender.render(camera, delta, RED, GREEN, BLUE);
		
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
		// this will set the clear color of the current open GL context. Meaning, when the screen is cleared,
		// this is the color it will use. the current color is a sky blue
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
		
		// this will clear the current frame buffer of its contents and set the pixels to the 
		// pixel color specified in the clearColor funciton call above
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Creates a projection matrix to project the z axis and stuff
	 */
	private void createProjectionMatrix() {
		// TODO these should not be constant
		float width = GLFWHandler.SCREEN_WIDTH;
		float height = GLFWHandler.SCREEN_HEIGHT;
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
	 * clean up, clean up, everybody everywhere, clean up, clean up, come on do your share
	 */
	public void cleanUp() {
		sShader.cleanUp();
		tShader.cleanUp();
		sRender.cleanUp();
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
	
	public Matrix4f getProjMatrix() {
		return this.projMatrix;
	}
}
