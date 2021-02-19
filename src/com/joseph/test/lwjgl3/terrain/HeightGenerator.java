package com.joseph.test.lwjgl3.terrain;

import java.util.Random;

/**
 * My overall thoughts on this are meh, i really am not a fan of it, i like the generalality
 * of it though, really the whole terrain system is kinda bonkersly designed to me but meh 
 * for now. This works and its a neat introduction into procedural. I think personally though
 * i would look for more complicated noise and height and other types of generation if i made
 * something with procedural generation of terrain. the comments of this should be read from bottom
 * to top
 *
 */
public class HeightGenerator {
	private static final float AMPLITUDE = 70.0f;
	private static final int OCTAVES = 3;
	private static final float ROUGHNESS = 0.3f;
	
	private Random rand = new Random(555);
	private int seed;
	
	public HeightGenerator() {
		this.seed = rand.nextInt(1000000000);
	}
	
	/**
	 * and this is repsonsible for actually generating everything, and it is general,
	 * and it does cool stuff so yay
	 * @param x
	 * @param z
	 * @return
	 */
	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, OCTAVES - 1);
		for (int i = 0; i < OCTAVES; i++) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
			total += getInterpolatedNoise(x * freq, z * freq) * amp;
		}
		return total;
	}
	
	/**
	 * and this interpolates the smoothed out noise which is very nice
	 * it allows for positions that are floats and it allows for some wacky stuff
	 * @param x
	 * @param z
	 * @return
	 */
	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}
	
	/**
	 * just finds the value between a and b using blend to pick
	 * @param a
	 * @param b
	 * @param blend
	 * @return
	 */
	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1.0f - Math.cos(theta)) * 0.5f;
		return a * (1.0f - f) + b * f;
	}
	
	/**
	 * and this one smooths out get noise, which is also nice, it makes it less agressive but it is sill very
	 * sharp and jaged
	 * @param x
	 * @param z
	 * @return
	 */
	private float getSmoothNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z - 1) + getNoise(x + 1, z + 1)) / 16.0f;
		float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8.0f;
		float center = getNoise(x, z) / 4;
		return corners + sides + center;
	}
	
	/**
	 * like this guy, this one irks me cause of the set seed all the time and idk, i just dont think it the best
	 * but this will in fact generate the same result ever time it is run with the same values which is nice
	 * @param x
	 * @param z
	 * @return
	 */
	private float getNoise(int x, int z) {
		rand.setSeed(x * 49632 + z * 32176 + seed);
		return rand.nextFloat() * 2.0f - 1.0f;
	}
}
