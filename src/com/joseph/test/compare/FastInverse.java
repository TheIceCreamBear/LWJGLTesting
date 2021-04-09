package com.joseph.test.compare;

import java.util.Random;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.particle.cube.CubeParticleSystem;

public class FastInverse {
	public static void main(String[] args) {
		// setup storage
		int count = 100000;
		Info[] info = new Info[count];
		
		// setup random and dir
		Random r = new Random(156467);
		Vector3f dir = new Vector3f(0.0f, 1.0f, 0.0f);
		
		// loop all indicies
		for (int i = 0; i < info.length; i++) {
			// create the vector to get the length of to then take the inverse square root
			int scalar = r.nextInt(1000);
			Vector3f vec = CubeParticleSystem.generateRandomUnitVectorWithinCode(dir, 89.0f).mul(scalar);
			float number = vec.lengthSquared();
			
			// prevent doing the inverse square root on 0
			if (number <= 0.001f) {
				i--;
				continue;
			}
			
			// evaluate the fast inverse square root
			long start = System.nanoTime();
			float fastResult = fastInverseSquareRoot(number);
			long stop = System.nanoTime();
			long fastTime = stop - start;
			
			// evaluate regular inverse square root
			start = System.nanoTime();
			float result = (float) (1.0f / Math.sqrt(number));
			stop = System.nanoTime();
			long time = stop - start;
			
			// save the info
			Info in = new Info(number, fastResult, result, fastTime, time);
			info[i] = in;
		}
		
		// loop everything again and take the averages
		long fastTimeTotal = 0;
		long timeTotal = 0;
		double diffTotal = 0.0;
		for (int i = 0; i < info.length; i++) {
			fastTimeTotal += info[i].fastTime;
			timeTotal += info[i].time;
			
			double diff = Math.abs(info[i].result - info[i].fastResult);
			diffTotal += diff;
			
			if (diff > 0.01) {
				System.out.printf("%5d: %f & %f differ by %f on the number %f\n", i, info[i].result, info[i].fastResult, diff, info[i].number);
			}
			
			if (info[i].fastTime > info[i].time) {
				System.out.printf("%5d: The time for %f is slower for the fast algo by %fns\n", i, info[i].number, diff);
			}
		}
		
		System.out.printf("On average over %d trials,\n", count);
		System.out.printf("Fast invsqrt took %fns\n", (double) fastTimeTotal / count);
		System.out.printf("Inverse sqrt took %fns\n", (double) timeTotal / count);
		System.out.printf("And the difference in the result was %f\n", (double) diffTotal / count);
		
	}
	
	final static float threehalfs = 1.5f;
	public static float fastInverseSquareRoot(float number) {
		// obvious credits to the quake dudes for making this
		
		int i;
		float x2, y;
		
		x2 = number * 0.5f;
		y = number;
		i = Float.floatToIntBits(y);
		i = 0x5f3759df - (i >> 1);
		y = Float.intBitsToFloat(i);
		y = y * (threehalfs - (x2 * y * y));
		
		return y;
	}
	
	public static class Info {
		public final float number;
		public final float fastResult;
		public final float result;
		public final long fastTime;
		public final long time;
		
		public Info(float number, float fastResult, float result, long fastTime, long time) {
			this.number = number;
			this.fastResult = fastResult;
			this.result = result;
			this.fastTime = fastTime;
			this.time = time;
		}
	}
}
