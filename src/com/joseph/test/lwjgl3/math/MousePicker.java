package com.joseph.test.lwjgl3.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.terrain.Terrain;
import com.joseph.test.lwjgl3.util.Point;

/**
 * Really dont like what this is called, like mouse picker, wtf is that? its so
 * weird, also idk if i like this class fully, but for now its fine
 * 
 * class from TUT is based on this article
 * https://antongerdelan.net/opengl/raycasting.html
 * 
 * @author Joseph
 *
 */
public class MousePicker {
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vector3f curRay;
	private Matrix4f projMat;
	private Matrix4f viewMat;
	private Camera cam;
	
	private Terrain terrain;
	private Vector3f curTerrainPoint;
	
	public MousePicker(Camera cam, Matrix4f proj, Terrain t) {
		this.cam = cam;
		this.projMat = proj;
		this.viewMat = MathHelper.createViewMatrix(cam);
		this.terrain = t;
	}
	
	public Vector3f getCurTerrainPoint() {
		return this.curTerrainPoint;
	}
	
	public Vector3f getCurRay() {
		return this.curRay;
	}
	
	public void update() {
		viewMat = MathHelper.createViewMatrix(cam);
		this.curRay = this.calcRay();
		if (intersectionInRange(0, RAY_RANGE, this.curRay)) {
			curTerrainPoint = binarySearch(0, 0, RAY_RANGE, this.curRay);
		} else {
			curTerrainPoint = null;
		}
	}
	
	private Vector3f calcRay() {
		// find screen coords
		Point<Float> mousePos = GLFWHandler.getMousePos();
		// normalize those pixel coords to be inside [-1,1] in both dirs
		Vector2f normDeviceCoords = this.getNormalizedDeviceCoords(mousePos);
		// changed them down one level, where the vec is pointing into the screen
		Vector4f clipCoords = new Vector4f(normDeviceCoords, -1.0f, 1.0f);
		// ray out of the eyes
		Vector4f eyeCoords = this.toEyeCoords(clipCoords);
		// ray to the world in world space
		Vector3f worldRay = this.toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedViewMat = new Matrix4f(this.viewMat);
		invertedViewMat.invert();
		Vector4f eyeCord = invertedViewMat.transform(eyeCoords);
		return new Vector3f(eyeCord.x, eyeCord.y, eyeCord.z).normalize();
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjMat = new Matrix4f(this.projMat);
		invertedProjMat.invert();
		Vector4f eyeCord = invertedProjMat.transform(clipCoords);
		return new Vector4f(eyeCord.x, eyeCord.y, -1.0f, 0.0f);
	}
	
	private Vector2f getNormalizedDeviceCoords(Point<Float> pos) {
		float x = (2.0f * pos.getX()) / GLFWHandler.SCREEN_WIDTH - 1.0f;
		float y = (2.0f * pos.getY()) / GLFWHandler.SCREEN_HEIGHT - 1.0f;
		return new Vector2f(x, -y);
	}
	
	//**********************************************************
	// so yea uhhhhhhhhh this is straight out of the tutorial guy's code, and TBH idk that i really vibe with
	// how this is done and setup, but like not much i can really do, but it does work pretty well IMO
	// so this is both cool but also poopy
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = cam.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return start.add(scaledRay);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.x, endPoint.z);
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}
	
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.x, testPoint.z);
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}
	
	private Terrain getTerrain(float worldX, float worldZ) {
		return terrain;
	}
}
