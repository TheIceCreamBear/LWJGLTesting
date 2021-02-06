package com.joseph.test.lwjgl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.entity.Player;
import com.joseph.test.lwjgl3.gui.GuiRenderer;
import com.joseph.test.lwjgl3.gui.GuiTexture;
import com.joseph.test.lwjgl3.math.MousePicker;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.models.obj.ModelData;
import com.joseph.test.lwjgl3.models.obj.OBJLoader;
import com.joseph.test.lwjgl3.renderer.MainRenderer;
import com.joseph.test.lwjgl3.terrain.Terrain;
import com.joseph.test.lwjgl3.textures.TerrainTexture;
import com.joseph.test.lwjgl3.textures.TerrainTexturePack;
import com.joseph.test.lwjgl3.textures.Texture;
import com.joseph.test.lwjgl3.textures.TextureLoader;
import com.joseph.test.lwjgl3.water.WaterFrameBuffers;
import com.joseph.test.lwjgl3.water.WaterRenderer;
import com.joseph.test.lwjgl3.water.WaterShader;
import com.joseph.test.lwjgl3.water.WaterTile;

public class Main {
	// note this is created and explained later in the program
	// default value is null
	public static long windowPointer = MemoryUtil.NULL;
	
	public static float delta = 0.0f;
	
	public static void main(String[] args) {		
		// lol this is the main method bois
		
		// the lines following this will not stay here, they will be put into a different class.
		// HOWEVER, because this project is for learning, this will only be temporary and it 
		// will not be best practice. lol
		
		// set the error handler so that we handle the errors bois
		// the :: operator is a method reference operator. I am passing in
		// the method called 'errorCallback' in class 'GLFWHandler' to this method
		GLFW.glfwSetErrorCallback(GLFWHandler::errorCallback);
		
		// init GLFW. it is "standard" to throw an IllegalStateException here, because duh, if
		// this shit doesn't init right, then like, why would we keep going
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("LOL! GLFW failled to initialize properly, nothing will work!");
		}
		
		// LOL This is wayyyy more complicated than it needs to be, but essentially, the code inside
		// the run method will get run at the end of the program. We are required to, at the end
		// of our program, terminate GLFW (issues might happen if we dont, maybe, maybe not) but 
		// what these next 5 lines of code do, is make sure that at the end of the program, 
		// GLFW.glfwTerminate() is called
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				GLFW.glfwTerminate();
			}
		}));
		
		// sets some window hints
		// makes sure that all hints are default first
		// then the first one is setting the window to not visible yet
		// and the second one is setting the window to not resizeable by the user (but still in code)
		// this is done before the window is created
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		
		// so this generates the window and displays it, the reason we store this in a long rather 
		// some sort of special class is because the GLFW bindings are direct C level bindings
		// and what is returned from the C code is a pointer to a struct. because pointers are at max
		// 64 bits on a 64 bit system, and 32 bits on a 32 bit system, the long primitive in Java has enough
		// space to fully store any value of a pointer, and is therefore the best way to represent the 
		// reference to the window without the use of pointers
		windowPointer = GLFW.glfwCreateWindow(GLFWHandler.SCREEN_WIDTH, GLFWHandler.SCREEN_HEIGHT, "LOLOL this is our main Window", MemoryUtil.NULL, MemoryUtil.NULL);
		
		// sets a key call back. all key input is handled via key call backs, and the key, action, and mods 
		// determine what was pressed. this specific callback will use a lambda (not any more haha lol g3t r3kt),
		// however, a method reference (:: operator) is perfectly valid here as well. because this is a test 
		// environment and we are exploring the feature set of LWJGL 3, this will be better as a lambda expression
		// (again, lol nope, decided to change this to a call back to GLFWHandler due to adding more call backs, to
		// prevent cluter)
		GLFW.glfwSetKeyCallback(windowPointer, GLFWHandler::keyboardCallback);
		
		// sets a scroll call back. as mentioned, all input is handled via call backs, and the information is passed
		// to the call back, defined using a method reference(:: operator).
		GLFW.glfwSetScrollCallback(windowPointer, GLFWHandler::scrollCallback);
		
		// sets a cursor postion call back.
		GLFW.glfwSetCursorPosCallback(windowPointer, GLFWHandler::cursorPosCallback);
		
		// makes the openGl current context to this window
		// any changes made using open gl or that relate to the window
		// and the window isnt passed in will be done on this window
		// remember everthing is done with the window pointer
		GLFW.glfwMakeContextCurrent(windowPointer);
		
		// this will enable V-sync (when the window waits till the vertical synchronization of the monitor
		// to begin rendering the buffered frame). this value can go above 1 to use a longer interval
		// between swaps, but, that will begin to increase input lag (technically, it isnt input lag, 
		// it is display lag as the input is handled on time but the frame is a bit behind
		// a value of one passed in disables V-sync, an option the users may want control over (MAY)
		GLFW.glfwSwapInterval(1);
		
		// honestly, the most self explanatory piece of code so far, makes the window visible
		GLFW.glfwShowWindow(windowPointer);
		
		
		// this would normally be the end of the "init" step of the program, and we would now move onto
		// the "loop" step of the program
		
		// The following comment is taken from the LWJGL getting started page https://www.lwjgl.org/guide
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		// what this line does, is it creates the necessary internal C level code to allow us to use
		// OpenGL code and methods. without this line, trying to render anything with OpenGL will fail
		
		// so like this makes it so that open GL will try to figure out which triangle is on top relative
		// to the other triangles and like itll make it so that you dont see multiple faces on top you only
		// see what you can see so ya
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// honestly this should probably be static too but still like, this is the way the tutorial did it so like
		// thats how imma do it
		ModelLoader loader = new ModelLoader();
		MainRenderer renderer = new MainRenderer(loader);
		List<Entity> entities = new ArrayList<Entity>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		
		// load a tree model and its texture
		ModelData treeData = OBJLoader.loadObjModel("res/provided/pine.obj");
		RawModel treeModel = loader.loadToVAO(treeData);
		Texture treeTex = TextureLoader.loadTexture("res/provided/pine.png");
		TexturedModel tree = new TexturedModel(treeModel, treeTex);
		
		// load a "low poly" tree model and its texture
//		ModelData lpTreeData = OBJLoader.loadObjModel("res/provided/lowPolyTree.obj");
//		RawModel lpTreeModel = loader.loadToVAO(lpTreeData);
//		Texture lpTreeTex = TextureLoader.loadTexture("res/provided/lowPolyTree.png");
//		lpTreeTex.setNumRows(2);
//		TexturedModel lpTree = new TexturedModel(lpTreeModel, lpTreeTex);

		// load a grass model and its texture
		ModelData grassData = OBJLoader.loadObjModel("res/provided/grassModel.obj");
		RawModel grassModel = loader.loadToVAO(grassData);
		Texture grassTex = TextureLoader.loadTexture("res/provided/grassTexture.png");
		grassTex.setHasTransparency(true);
		grassTex.setUseFakedLighting(true);
		TexturedModel grass = new TexturedModel(grassModel, grassTex);
		
		// load a fern model and its texture
		ModelData fernData = OBJLoader.loadObjModel("res/provided/fern.obj");
		RawModel fernModel = loader.loadToVAO(fernData);
		Texture fernTex = TextureLoader.loadTexture("res/provided/fern.png");
		fernTex.setNumRows(2);
		fernTex.setHasTransparency(true);
		fernTex.setUseFakedLighting(true);
		TexturedModel fern = new TexturedModel(fernModel, fernTex);
		
		// load a lamp
		RawModel lampModel = loader.loadToVAO(OBJLoader.loadObjModel("res/provided/lamp.obj"));
		Texture lampTex = TextureLoader.loadTexture("res/provided/lamp.png");
		TexturedModel lamp = new TexturedModel(lampModel, lampTex);
		
		// load an icoSphere
		RawModel icoModel = loader.loadToVAO(OBJLoader.loadObjModel("res/TestModels/icoSphere.obj"));
		Texture icoTex = TextureLoader.loadTexture("res/provided/white.png");
		TexturedModel icoSphere = new TexturedModel(icoModel, icoTex);
		
		// loads the rocks for the water tuts
		RawModel rocksModel = loader.loadToVAO(OBJLoader.loadObjModel("res/provided/rocks.obj"));
		Texture rocksTex = TextureLoader.loadTexture("res/provided/rocks.png");
		TexturedModel rocks = new TexturedModel(rocksModel, rocksTex);
		
		// setup some lights
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0.0f, 100.0f, -100.0f), new Vector3f(1.0f, 1.0f, 1.0f))); // water sun
		lights.add(new Light(new Vector3f(0.0f, -1000.0f, -100.0f), new Vector3f(1.0f, 1.0f, 1.0f))); // water bottom sun
//		lights.add(new Light(new Vector3f(0.0f, 1000.0f, -7000.0f), new Vector3f(0.4f, 0.4f, 0.4f))); // sun
//		lights.add(new Light(new Vector3f(185.0f, 6.2f, -293.0f), new Vector3f(2.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
//		lights.add(new Light(new Vector3f(370.0f, 15.1f, -300.0f), new Vector3f(0.0f, 2.0f, 2.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
//		lights.add(new Light(new Vector3f(293.0f, 4.1f, -305.0f), new Vector3f(2.0f, 2.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
		
		// add entities at the light positions to make it easier to know where the light is
		entities.add(new Entity(icoSphere, 0, lights.get(0).getPosition(), 0.0f, 0.0f, 0.0f, 4.0f, true));
//		entities.add(new Entity(icoSphere, 0, lights.get(1).getPosition(), 0.0f, 0.0f, 0.0f, 4.0f, true));
//		entities.add(new Entity(icoSphere, 0, lights.get(2).getPosition(), 0.0f, 0.0f, 0.0f, 4.0f, true));
//		entities.add(new Entity(icoSphere, 0, lights.get(3).getPosition(), 0.0f, 0.0f, 0.0f, 4.0f, true));
		
//		entities.add(new Entity(lamp, 0, new Vector3f(185.0f, -4.7f, -293.0f), 0.0f, 0.0f, 0.0f, 1.0f, true));
//		entities.add(new Entity(lamp, 0, new Vector3f(370.0f, 4.2f, -300.0f), 0.0f, 0.0f, 0.0f, 1.0f, true));
//		entities.add(new Entity(lamp, 0, new Vector3f(293.0f, -6.8f, -305.0f), 0.0f, 0.0f, 0.0f, 1.0f, true));
		
		// setup terrain textures
		TerrainTexture baseTex = new TerrainTexture(TextureLoader.loadTexture("res/provided/grassy.png"));
		TerrainTexture rTex = new TerrainTexture(TextureLoader.loadTexture("res/provided/dirt.png"));
		TerrainTexture gTex = new TerrainTexture(TextureLoader.loadTexture("res/provided/grassFlowers.png"));
		TerrainTexture bTex = new TerrainTexture(TextureLoader.loadTexture("res/provided/path.png"));
		
		TerrainTexturePack pack = new TerrainTexturePack(baseTex, rTex, gTex, bTex);
//		TerrainTexture blendMap = new TerrainTexture(TextureLoader.loadTexture("res/provided/blendMap.png"));
		TerrainTexture blendMap = new TerrainTexture(TextureLoader.loadTexture("res/provided/blendMapForWater.png"));
				
//		Terrain terrain = new Terrain(-1, -1, loader, pack, blendMap, "res/provided/heightmap.png");
		Terrain terrain2 = new Terrain(0, -1, loader, pack, blendMap, "res/provided/heightmap.png");
		Terrain waterT = new Terrain(0, -1, loader, pack, blendMap, "res/provided/heightmapForWater.png");
		terrains.add(waterT);
		
		ModelData playerData = OBJLoader.loadObjModel("res/provided/person.obj");
		RawModel playerModel = loader.loadToVAO(playerData);
		Texture playerTex = TextureLoader.loadTexture("res/provided/playerTexture.png");
		TexturedModel player = new TexturedModel(playerModel, playerTex);
//		Player playa = new Player(player, new Vector3f(160.0f, terrain2.getHeightOfTerrain(160.0f, -260.0f), -260.0f), 0.0f, 90.0f, 0.0f, 0.5f);
		Player playa = new Player(player, new Vector3f(10.0f, waterT.getHeightOfTerrain(10.0f, -10.0f), -10.0f), 0.0f, 90.0f, 0.0f, 0.5f);
		Camera camera = new Camera(playa);
		// new: add player to list of entites because it is one and it will always be rendered
		entities.add(playa);
		
		// store the random objects into a list
		/*
		Random r = new Random();
		for (int i = 0; i < 500; i++) {
			if (i % 2 == 0) {
				float x = r.nextFloat() * Terrain.SIZE;
				float z = r.nextFloat() * -Terrain.SIZE;
				float y = terrain2.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, r.nextInt(4),  new Vector3f(x, y, z), 0.0f, r.nextFloat() * 360.0f, 0.0f, 0.9f));
			}
			if (i % 5 == 0) {
				float x = r.nextFloat() * Terrain.SIZE;
				float z = r.nextFloat() * -Terrain.SIZE;
				float y = terrain2.getHeightOfTerrain(x, z);
//				entities.add(new Entity(lpTree, r.nextInt(4),  new Vector3f(x, y, z), 0.0f, r.nextFloat() * 360.0f, 0.0f, r.nextFloat() * 0.1f + 0.6f));
//				x = r.nextFloat() * 800.0f;
//				z = r.nextFloat() * -800.0f;
//				y = terrain2.getHeightOfTerrain(x, z);
				entities.add(new Entity(tree,  new Vector3f(x, y, z), 0.0f, 0.0f, 0.0f, r.nextFloat() * 1.0f + 1.0f));
			}
		}
		*/
		
		// the rocks entity for under the little island type deal
		entities.add(new Entity(rocks, new Vector3f(75.0f, 4.6f, -75.0f), 0.0f, 0.0f, 0.0f, 75.0f));
		
		entities.add(new Entity(tree, new Vector3f(38.0f, waterT.getHeightOfTerrain(38.0f, -21.0f), -21.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(12.0f, waterT.getHeightOfTerrain(12.0f, -48.0f), -48.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(13.0f, waterT.getHeightOfTerrain(13.0f, -25.0f), -25.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(97.0f, waterT.getHeightOfTerrain(97.0f, -28.0f), -28.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(113.0f, waterT.getHeightOfTerrain(113.0f, -20.0f), -20.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(138.0f, waterT.getHeightOfTerrain(138.0f, -23.0f), -23.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(92.0f, waterT.getHeightOfTerrain(92.0f, -9.0f), -9.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(139.0f, waterT.getHeightOfTerrain(139.0f, -128.0f), -128.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(125.0f, waterT.getHeightOfTerrain(125.0f, -143.0f), -143.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(15.0f, waterT.getHeightOfTerrain(15.0f, -135.0f), -135.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(tree, new Vector3f(39.0f, waterT.getHeightOfTerrain(39.0f, -123.0f), -123.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		
		entities.add(new Entity(fern, new Vector3f(18.0f, waterT.getHeightOfTerrain(18.0f, -80.0f), -80.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(fern, new Vector3f(99.0f, waterT.getHeightOfTerrain(99.0f, -42.0f), -42.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(fern, new Vector3f(28.0f, waterT.getHeightOfTerrain(28.0f, -36.0f), -36.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		entities.add(new Entity(fern, new Vector3f(130.0f, waterT.getHeightOfTerrain(130.0f, -131.0f), -131.0f), 0.0f, 0.0f, 0.0f, 1.0f));
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(TextureLoader.loadTexture("res/provided/socuwan.png").glTextureID(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture gui2 = new GuiTexture(TextureLoader.loadTexture("res/provided/thinmatrix.png").glTextureID(), new Vector2f(0.30f, 0.58f), new Vector2f(0.4f, 0.4f));

//		guis.add(gui);
//		guis.add(gui2);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		// really dont like what this is called
		MousePicker picker = new MousePicker(camera, renderer.getProjMatrix(), waterT);
//		Entity test = new Entity(tree, new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f, 0.5f);
		
		// water stuff that really shouldnt be in this location but this project is already a mess so whats some more
		WaterShader wShader = new WaterShader();
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterRenderer wRenderer = new WaterRenderer(loader, wShader, renderer.getProjMatrix(), fbos);
		List<WaterTile> water = new ArrayList<WaterTile>();
		WaterTile wt = new WaterTile(75.0f, -75.0f, 0.0f);
		water.add(wt);
		
		// THIS IS REALLY BAD NO BAD BUT THE TUT HAS IT IN A CLASS I DONT HAVE (because LWJGL2/3 reasons)
		// AND IDK WHERE ELSE TO PUT IT ALSO EW NO DELTA TIME IS NOT SOMETHING I LIKE I LIKE FIXED TIME
		// UPDATES NOT DELTA TIME UPDATES THANKS
		// NOTE: TUT uses Sys.getTime() which is not available in LWJGL 3, how ever, as mentioned on 
		// this (http://forum.lwjgl.org/index.php?topic=5601.0) forum post, you can use GLFW.glfwGetTime(), 
		// which returns a double. an alternative, would be replacing Sys in the TUT with GLFW.glfw[^] 
		// where [^] represents making that following letter capitalized
		double lastFrameTime = GLFW.glfwGetTime();
		double dDelta = 0.0;
		
		// this is the loop portion of our code. this is the "main game loop" area
		// it will continue to run until the window hint that the window should close is set to true
		// this can happen if the user hits the X on the window, or (as seen in the key call back)
		// the user hits escape
		while (!GLFW.glfwWindowShouldClose(windowPointer)) {			
			// move le player dude 
			playa.move(waterT);
			camera.move();
			
//			picker.update();
//			Vector3f terPoint = picker.getCurTerrainPoint();
//			if (terPoint != null) {
//				test.setPos(terPoint);
//				if (GLFW.glfwGetMouseButton(windowPointer, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
//					entities.add(test);
//					test = new Entity(tree, new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f, 0.5f);
//				}
//			}
			

			// tells open gl that we want to use the clip plane distance 0, just to make sure that it is enabled
			// clip distance is just how far a vertex is from the clipping plane (this is signed), so like a positive
			// clip distance means that it is "below" the plane (below is a relative term cause 3d planes and normals
			// and wierd calc 3 type stuff but basically on the side where the normal of the plane is positive)
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			// render reflection
			fbos.bindReflectionFrameBuffer();
			// set cam to below water
			float distance = 2 * (camera.getPosition().y - wt.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -wt.getHeight() + 0.15f));
			// reset cam
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			// render refraction
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, wt.getHeight() + 0.15f));
			
			// disables the cliping feature just so that nothing gets accidentally clipped by the shaders during the main pass
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			// render the entire scene with one call, makes everything simpler to an extent
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 0, 0, 0));
			
			// render water after scene but before gui
			wRenderer.render(water, camera, lights.get(0));
			
			// render the Gui items
			guiRenderer.render(guis);
			
			// this will swap which buffer is currently in the "front" and which is in the "back"
			// for more reading on why we do this and how it works, see 
			// https://www.glfw.org/docs/latest/quick_guide.html#quick_swap_buffers
			GLFW.glfwSwapBuffers(windowPointer);
			
			// reset mouse based input
			// yea so i forgot that maybe this needs to be done before we poll for the NEXT frame, rather than
			// after we poll for the next frame cause then there are literally no input values for the next frame
			// hello??
			GLFWHandler.frameReset();
			
			// this will poll the window for events. this is the only way events will be delivered to 
			// our program, as the event model is a poll not listen model (an example of a listen model 
			// would be the AWT event thread used in java.swing)
			GLFW.glfwPollEvents();
			
			// this is where we update how long this frame took to render
			double curFrame = GLFW.glfwGetTime();
			dDelta = curFrame - lastFrameTime;
			lastFrameTime = curFrame;
			delta = (float) dDelta;
		}
		
		// if we have reached this part of the program, we have exited the main loop
		// normally it is bad practice to have the loop and initialization all in one method, but
		// as mentioned earlier, there is no need for us to follow good practice when trying to 
		// connect these inter-weaving ideas together.
		
		// ^^^^ so that might have been a lie and a hinderance ^^^^
		
		wShader.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		renderer.cleanUp();
		TextureLoader.cleanUp();
		
		// this will free our call backs, as well as destroy the window we created
		Callbacks.glfwFreeCallbacks(windowPointer);
		GLFW.glfwDestroyWindow(windowPointer);
		
		// the termination of GLFW would go here, but i wanted to be extra earlier, so it is in its own thread
		
		// however, the last thing we will do is to set the error callback to null
		// if there are any native resources in use, free them, because we dont need them
		GLFW.glfwSetErrorCallback(null).free();
	}
}
