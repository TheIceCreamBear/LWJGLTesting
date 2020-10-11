package com.joseph.test.lwjgl3.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	
	/**
	 * Helper thingy mabober to make a transfromation matrix out of a vector position 
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
		// make big
		matrix.scale(scale);
		
		return matrix;
	}
}
