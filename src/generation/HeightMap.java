package generation;

import java.util.Arrays;
import java.util.Random;

public class Heightmap {

	Random rGen;
	int size;

	public float[][] heightmap;
	public float[][] binaryMap;

	public float mtnThresh;
	public float waterThresh;
	
	
	public Heightmap(long seed) {
		rGen = new Random(seed);
	}


	public float[][] generateHeightmap(int size) {
		heightmap = new float[size][size];

		float[][] base = diamondSquare  (size, 1.0f);
		float[][] second = diamondSquare(size, 0.8f);
		float[][] third = diamondSquare (size, 0.5f);
		float[][] fourth = diamondSquare(size, 0.2f);

		
		float max = 0;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				heightmap[i][j] = (base[i][j]*8 + second[i][j]*4 + third[i][j]*2 + fourth[i][j])/15;
				if (heightmap[i][j] > max) max = heightmap[i][j];
			}
		}

		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				heightmap[i][j] /= max;
			}
		}
		
		
		float[] heights = sortValues(heightmap);

		//65% water
		int index = (int)(0.65 * size * size + 0.5);

		gradient(heightmap);

		binaryMap = new float[size][size];

		waterThresh = heights[index];		
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (heightmap[i][j] > waterThresh) {
					binaryMap[i][j] = 1;
				}
			}
		}
				
		//5% mountains
		index = (int)(0.95 * size * size + 0.5);
		mtnThresh = heights[index];
		
		
		
		return heightmap;
	}


	public float[][] diamondSquare (int size, float roughness) {
		this.size = size;
		float[][] heightMap = new float[size][size];
		//initial map seeding
		heightMap[0][0] = 0;// rGen.nextFloat();
		heightMap[0][size-1] = 0;//rGen.nextFloat();
		heightMap[size-1][0] = 0;//rGen.nextFloat();
		heightMap[size-1][size-1] = 0;//rGen.nextFloat();

		int xMin = 0; int xMax = size; int yMin = 0; int yMax = size;

		int dx = xMax - xMin-1;
		int dy = yMax - yMin-1;

		while (dx > 1 && dy > 1) {
			xMin = 0; xMax = dx;
			yMin = 0; yMax = dy;
			//interpolate squares
			while (xMax < size) {
				while (yMax < size) {
					square(xMin, xMax, yMin, yMax, heightMap, roughness);
					yMin += dy;
					yMax += dy;
				}
				xMin += dx;
				xMax += dx;
				yMin = 0;
				yMax = dy;
			}

			dx = Math.round(dx/2f);
			dy = Math.round(dy/2f);
			//interpolate first diamonds
			xMin = 0; xMax = 2*dx;
			yMin = -dy; yMax = dy;
			while (xMin < size-1) {
				while (yMin < size) {
					diamond(xMin, xMax, yMin, yMax, heightMap, roughness);
					yMin += 2*dy;
					yMax += 2*dy;
				}
				xMin += 2*dx;
				xMax += 2*dx;
				yMin = -dy; yMax = dy;
			}
			//interpolate second diamonds
			xMin = -dx; xMax = dx;
			yMin = 0; yMax = 2*dy;
			while (xMin < size) {
				while (yMin < size-1) {
					diamond(xMin, xMax, yMin, yMax, heightMap, roughness);
					yMin += 2*dy;
					yMax += 2*dy;
				}
				xMin += 2*dx;
				xMax += 2*dx;
				yMin = 0; yMax = 2*dy;
			}

			roughness = roughness/2;
		}
		return heightMap;

	}

	private void diamond(int xMin, int xMax, int yMin, int yMax, float[][] heightmap, float roughness) {
		int xMid = (xMin+xMax)/2; int yMid = (yMin + yMax)/2;


		float L = 0, D = 0, R = 0, U = 0;
		
		if (xMin < 0){
			L = 0;
		} else {
			L = heightmap[xMin][yMid];
		}
		if (yMin < 0){
			D = 0;
		} else {
			D = heightmap[xMid][yMin];
		}
		if (xMax >= size){
			R = 0;
		} else{
			R = heightmap[xMax][yMid];
		}
		if (yMax >= size){
			U = 0;
		} else {
			U = heightmap[xMid][yMax];
		}


		float avg = (L+R+U+D)/4;

		heightmap[xMid][yMid] = Math.max(Math.min(avg + (roughness*(rGen.nextFloat()-0.5f)*2), 1), 0);
	}


	private void square(int xMin, int xMax, int yMin, int yMax, float[][] heightmap, float roughness) {
		int xMid = (xMin+xMax)/2; int yMid = (yMin + yMax)/2;

		float avg = (heightmap[xMin][yMin] + heightmap[xMin][yMax] +
				heightmap[xMax][yMin] + heightmap [xMax][yMax])/4;

		heightmap[xMid][yMid] = Math.max(Math.min(avg + (roughness*(rGen.nextFloat()-0.5f)*2), 1), 0);
	}


	private float[] sortValues(float[][] world) {

		float[] h = new float[size*size];

		int index = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				h[index++] = world[i][j];
			}
		}
		Arrays.sort(h);
		return h;
	}
	


	private void gradient(float[][] world) { //TODO fix this
		for (int i = 0; i < size; i++) {
			float buffer = size/5f;
			for (int j = 0; j < size; j++) {
				
				float scale = 1;
				
				
				if (i < buffer) {
					scale *= i/buffer;
				} if (j < buffer ) {
					scale *= j/buffer;
				} if (i > size-buffer) {
					scale *= (size-i)/buffer;
				} if (j > size-buffer) {
					scale *= (size-j)/buffer;
				}
				
				world[i][j] *= scale;
			}
		}
	}


}
