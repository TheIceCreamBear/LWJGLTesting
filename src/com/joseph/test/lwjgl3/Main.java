package com.joseph.test.lwjgl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.models.obj.ModelData;
import com.joseph.test.lwjgl3.models.obj.OBJLoader;
import com.joseph.test.lwjgl3.renderer.MainRenderer;
import com.joseph.test.lwjgl3.terrain.Terrain;
import com.joseph.test.lwjgl3.textures.Texture;
import com.joseph.test.lwjgl3.textures.TextureLoader;

public class Main {
	// TEMPORARYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
	public static boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
	
	public static void main(String[] args) {
		// note this is created and explained later in the program
		// default value is null
		long windowPointer = MemoryUtil.NULL;
		
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
		windowPointer = GLFW.glfwCreateWindow(1600, 900, "LOLOL this is our main Window", MemoryUtil.NULL, MemoryUtil.NULL);
		
		// sets a key call back. all key input is handled via key call backs, and the key, action, and mods 
		// determine what was pressed this specific callback will use a lambda, however, a method reference 
		// (:: operator) is perfectly valid here as well. because this is a test environment and we are exploring
		// the feature set of LWJGL 3, this will be better as a lambda expression
		GLFW.glfwSetKeyCallback(windowPointer, (window, key, scancode, action, mods) -> {
			// this is what will be called when the poolEvents() method is called in the loop portion of the code
			// all key presses will be passed here
			// an example is using the escape key as the determining factor of when the window should close
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				// this will tell the window it should close. it DOES NOT actually close the window
				// this will cause our main loop to exit and stop.
				GLFW.glfwSetWindowShouldClose(window, true);
			}
			
			// THIS IS TEMPORARY GROSS CODE
			if (action == GLFW.GLFW_PRESS) {
				keyDown[key] = true;
			}

			// THIS IS TEMPORARY GROSS CODE
			if (action == GLFW.GLFW_RELEASE) {
				keyDown[key] = false;
			}
		});
		
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
		
		// this will set the clear color of the current open GL context. Meaning, when the screen is cleared,
		// this is the color it will use. the current color is a sky blue
		GL11.glClearColor(0.45f, 89.0f, 0.98f, 1.0f);
		
		// so like this makes it so that open GL will try to figure out which triangle is on top relative
		// to the other triangles and like itll make it so that you dont see multiple faces on top you only
		// see what you can see so ya
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// honestly this should probably be static too but still like, this is the way the tutorial did it so like
		// thats how imma do it
		ModelLoader loader = new ModelLoader();
		MainRenderer renderer = new MainRenderer();
		
		// load a tree model and its texture
		ModelData treeData = OBJLoader.loadObjModel("res/provided/tree.obj");
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
		Texture treeTex = TextureLoader.loadTexture("res/provided/tree.png");
		TexturedModel tree = new TexturedModel(treeModel, treeTex);

		// load a grass model and its texture
		ModelData grassData = OBJLoader.loadObjModel("res/provided/grassModel.obj");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		Texture grassTex = TextureLoader.loadTexture("res/provided/grassTexture.png");
		grassTex.setHasTransparency(true);
		grassTex.setUseFakedLighting(true);
		TexturedModel grass = new TexturedModel(grassModel, grassTex);
		
		// load a fern model and its texture
		ModelData fernData = OBJLoader.loadObjModel("res/provided/fern.obj");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		Texture fernTex = TextureLoader.loadTexture("res/provided/fern.png");
		fernTex.setHasTransparency(true);
		fernTex.setUseFakedLighting(true);
		TexturedModel fern = new TexturedModel(fernModel, fernTex);
		
		// store the random objects into a list
		List<Entity> entities = new ArrayList<Entity>();
		Random r = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(tree,  new Vector3f(r.nextFloat() * 800.0f - 400.0f, 0.0f, r.nextFloat() * -600.0f), 0.0f, 0.0f, 0.0f, 3.0f));
			entities.add(new Entity(grass, new Vector3f(r.nextFloat() * 800.0f - 400.0f, 0.0f, r.nextFloat() * -600.0f), 0.0f, 0.0f, 0.0f, 1.0f));
			entities.add(new Entity(fern,  new Vector3f(r.nextFloat() * 800.0f - 400.0f, 0.0f, r.nextFloat() * -600.0f), 0.0f, 0.0f, 0.0f, 0.6f));
		}
		
		Light light = new Light(new Vector3f(3000.0f, 2000.0f, 3000.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		Camera camera = new Camera();
		
		// got shifted to be in cam view
		Terrain terrain = new Terrain(-1, -1, loader, TextureLoader.loadTexture("res/provided/grass.png"));
		Terrain terrain2 = new Terrain(0, -1, loader, TextureLoader.loadTexture("res/provided/grass.png"));
		
		// this is how you make it go brrrrrrr and display only wires
//		GL20.glPolygonMode(GL20.GL_FRONT_AND_BACK, GL20.GL_LINE);
		
		// this is the loop portion of our code. this is the "main game loop" area
		// it will continue to run until the window hint that the window should close is set to true
		// this can happen if the user hits the X on the window, or (as seen in the key call back)
		// the user hits escape
		while (!GLFW.glfwWindowShouldClose(windowPointer)) {
			camera.move();
			
			// load all of the objects (entities) that we are going to render into the main renderer
			for (Entity e : entities) {
				renderer.addEntity(e);
			}
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain2);
			
			// responsible for all the rendering, and while this is okay, i dont really like the structure
			// of how it was coded, like at all, so expect this to change significantly
			renderer.render(light, camera);
			
			// this will swap which buffer is currently in the "front" and which is in the "back"
			// for more reading on why we do this and how it works, see 
			// https://www.glfw.org/docs/latest/quick_guide.html#quick_swap_buffers
			GLFW.glfwSwapBuffers(windowPointer);
			
			// this will poll the window for events. this is the only way events will be delivered to 
			// our program, as the event model is a poll not listen model (an example of a listen model 
			// would be the AWT event thread used in java.swing)
			GLFW.glfwPollEvents();
		}
		
		// if we have reached this part of the program, we have exited the main loop
		// normally it is bad practice to have the loop and initialization all in one method, but
		// as mentioned earlier, there is no need for us to follow good practice when trying to 
		// connect these inter-weaving ideas together.
		
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
