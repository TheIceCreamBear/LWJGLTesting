package com.joseph.test.lwjgl3.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.entity.Camera;

public class MathHelper {
	// TODO Create own vec2, vec3, vec4, matrix3, matrix4 and other similar classes
	// use JOML from LWJGL for now, becuase for some super 10000 iq reason there is
	// no classes like that inside LWJGL3. HOWEVER, as much as i may want to rant
	// about that, its actually good and smart because like less for them, and now
	// with JOML i can take an OVERLY WAY BEYOND OVERENGENERRED library and simplify
	// it to what i need when im ready.
	
	// ALSO, im gonna go on a rant here about people calling a Math class "Maths" like
	// in the tutorials, LIKE WHAT THE IUASHFOIU WHY IS IT MATH_S_. BRUV, idek, like
	// i guess it is shorted than math helper but like, WHY
	// side note, you cannot do just Math because ovbious conflict with java.lang.Math
	// is obvious
	
	/**
	 * Helper thingy mabober to make a transformation matrix out of a vector position 
	 * some rotational data
	 * @param pos
	 * @param rx
	 * @param ry
	 * @param rz
	 * @param scale
	 * @return
	 */
	public static Matrix4f createTransformationMatrix(Vector3f pos, float rx, float ry, float rz, float scale) {
		// make the matrix
		Matrix4f matrix = new Matrix4f();
		// skirt it to where it is
		matrix.translate(pos);
		// rotate things
		matrix.rotateX((float) Math.toRadians(rx));
		// more rotations
		matrix.rotateY((float) Math.toRadians(ry));
		// banana, rotate, uh
		matrix.rotateZ((float) Math.toRadians(rz));
		// make big (or small)
		matrix.scale(scale);
		
		return matrix;
	}
	
	/**
	 * Helper thingy mabober to make a transformation matrix out of a gui position and scale
	 * @param pos
	 * @param scale
	 * @return
	 */
	public static Matrix4f createTransformationMatrix(Vector2f pos, Vector2f scale) {
		// make the matrix
		Matrix4f matrix = new Matrix4f();
		// skirt it to where it is
		matrix.translate(new Vector3f(pos, 0.0f));
		// make big (or small)
		matrix.scale(new Vector3f(scale, 1.0f));
		
		return matrix;
	}
	
	/**
	 * makes a new view matrix from the camera, and like yay it makes a view transform thing yay
	 * @param camera
	 * @return
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		// make the matrix
		Matrix4f matrix = new Matrix4f();
		// rotate x
		matrix.rotateX((float) Math.toRadians(camera.getPitch()));
		// rotate y
		matrix.rotateY((float) Math.toRadians(camera.getYaw()));
		// rotate z
		matrix.rotateZ((float) Math.toRadians(camera.getRoll()));
		// move it opposite the direction of the camera
		matrix.translate(new Vector3f(camera.getPosition()).negate());
		// ret run
		return matrix;
	}
	
	/**
	 * An implementation of the barryCentric method for finding the height of a triangle 
	 * in 3 dimensions at a given point
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param pos
	 * @return
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x)* (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x)* (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
}
