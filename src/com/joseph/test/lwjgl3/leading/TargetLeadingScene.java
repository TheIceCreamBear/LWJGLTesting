package com.joseph.test.lwjgl3.leading;

import java.util.ArrayList;
import java.util.Iterator;
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
		
		dirIndicator = new Entity(loadModel("res/target/targetcylinder.obj", "res/redgreen.png"), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
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
		
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entity instanceof VelocityEntity) {
				VelocityEntity ve = ((VelocityEntity) entity);
				ve.move();
				// delete entity if its too far away
				if (ve.getPos().lengthSquared() > 62500) {
					it.remove();
				}
			}
		}
		
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_G]) {
			VelocityEntity e = new VelocityEntity(shellModel, new Vector3f(-25.0f, 10.0f, -25.0f), new Vector3f(5.0f, 0.0f, 0.0f), 0.0f, 90.0f, 0.0f, 1.0f);
			entities.add(e);
			last = e;
		}
		
		this.alive += Main.delta;
		Vector3f angle = angleToTarget(new Vector3f(0.0f, 0.0f, 0.0f), 25.0f, last.getPos(), last.getVelocity());
		float rho = 1.0f;
		float phi = (float) java.lang.Math.acos(angle.y);
		float theta = (float) java.lang.Math.atan(angle.x / angle.z);
		float pitch = (float) (90 - Math.toDegrees(phi));
		float yaw = (float) Math.toDegrees(theta);
		if (angle.z < 0) {
			yaw += 180;
		}
		dirIndicator.setRotx(pitch);
		dirIndicator.setRoty(yaw);
		
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_P]) {
			QuaternionEntity e = new QuaternionEntity(shellModel, new Vector3f(0,0,0), angle.mul(25, new Vector3f()), 1.0f);
			e.lookAlong(angle);
			entities.add(e);
		}
		
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
	
	/**
	 * method to find a firing angle given a constant speed projectile, the targets current position and velocity, and where
	 * the projectile will be fired from, to ensure that the projectile will hit the target given that the target has a constant
	 * velocity
	 * <br>
	 * based on math found at <br>
	 * https://math.stackexchange.com/questions/1603637/where-to-shoot-to-hit-a-moving-target-in-3d-space<br>
	 * and code written in an old project available here<br>
	 * https://github.com/TheIceCreamBear/TurretTest/blob/master/src/com/joseph/gametemplate/gameobject/Turret.java#L222
	 * @param shootingPosition
	 * @param projectileSpeed
	 * @param targetPosition
	 * @param targetVelocity
	 * @return
	 */
	private Vector3f angleToTarget(Vector3f shootingPosition, float projectileSpeed, Vector3f targetPosition, Vector3f targetVelocity) { 
		// get targets position if we considered where we were shooting from the origin
		Vector3f targetP0 = targetPosition.sub(shootingPosition, new Vector3f());
		
		float speedSquared = projectileSpeed * projectileSpeed;
		float velocitySquared = targetVelocity.dot(targetVelocity);	
		
		// setup up the quadratic equation
		float a = (speedSquared - velocitySquared);
		float b = -2.0f * (targetP0.dot(targetVelocity));
		float c = -targetP0.dot(targetP0);
		
		// test determinant of the quadratic equation
		float d = b * b - 4 * a * c;
		
		if (d < 0) {
			// this is illegal, as you will have a negative in a sqrt which wont work
			// return dummy value
			System.err.println("wtf");
			return new Vector3f(0.0f, 0.0f, 0.0f);
		}
		
		// solve the quadratic equation
		float t0 = (float) ((-b - Math.sqrt(d)) / (2 * a));
		float t1 = (float) ((-b + Math.sqrt(d)) / (2 * a));
		
		// take the answer that is positive and greater than 0
		float t = (t0 < 0) ? t1 : (t1 < 0) ? t0 : Math.min(t1, t0);
		
		targetP0.div(t);
		
		// return result
		Vector3f temp = targetP0.add(targetVelocity).normalize();
//		System.out.println(temp);
		return temp;
	}
}
