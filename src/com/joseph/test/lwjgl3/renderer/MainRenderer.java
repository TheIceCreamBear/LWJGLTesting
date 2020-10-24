package com.joseph.test.lwjgl3.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.StaticShader;

/**
 * IDK if i have done whole class docs like this before but imma do it so basically this is really bad design IMO
 * because like whi is this a class that you an make an instance of like this imo should be static or designed diferent
 * but like thats not the full purpose but like also is of this project yay
 *
 */
public class MainRenderer {
	private StaticShader shader;
	private Renderer render;
	
	private HashMap<TexturedModel, List<Entity>> entities;
	
	public MainRenderer() {
		this.shader = new StaticShader();
		this.render = new Renderer(shader);
		
		this.entities = new HashMap<TexturedModel, List<Entity>>();
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
		render.prepare();
		// start le shader
		shader.start();
		// load the light and view matrix
		shader.loadLight(worldLight);
		shader.loadViewMatrix(camera);
		// render everything
		render.render(entities);
		// stop shader
		shader.stop();
		// remove entities (for some reason think this could be handeled a different way instead of clearing it tho the only thing that
		// is wasted is the ArrayList because the contents are stored else where
		entities.clear();
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
	 * okay so this is conna be commented out because it is REALLY BAD CODE but im including it here
	 * because i want to meme on the tutorial for how bad the code here is when like my slight modification to
	 * it above is much more simpler words arent the best for me anyway enjoy this cursed code
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
		shader.cleanUp();
	}
}
