package generation.test;

import java.util.Random;

public class Heightmap {

	Random rGen;
	int size;

	public Heightmap(long seed) {
		rGen = new Random(seed);
	}


	public float[][] generateHeightmap(int size) {
		float[][] heightmap = new float[size][size];
		float[][] base = diamondSquare(size, 0.8f);
		float[][] second = diamondSquare(size, 0.4f);
		float[][] third = diamondSquare(size, 0.2f);
		float[][] fourth = diamondSquare(size, 0.1f);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				heightmap[i][j] = (base[i][j]*8 + second[i][j]*4 + third[i][j]*2 + fourth[i][j])/15;
			}
		}
		radialGradient(heightmap);
		return heightmap;
	}


	public float[][] diamondSquare (int size, float roughness) {
		this.size = size;
		float[][] heightMap = new float[size][size];
		//initial map seeding
		heightMap[0][0] = rGen.nextFloat();
		heightMap[0][size-1] = rGen.nextFloat();
		heightMap[size-1][0] = rGen.nextFloat();
		heightMap[size-1][size-1] = rGen.nextFloat();

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

		float avg = (heightmap[((xMin+size)%size)][((yMid+size)%size)] + heightmap[((xMid+size)%size)][((yMax+size)%size)] +
				heightmap[((xMid+size)%size)][((yMin+size)%size)] + heightmap [((xMax+size)%size)][((yMid+size)%size)])/4;

		heightmap[xMid][yMid] = Math.max(Math.min(avg + (roughness*(rGen.nextFloat()-0.5f)*2), 1), 0);
	}
	
	
	private void square(int xMin, int xMax, int yMin, int yMax, float[][] heightmap, float roughness) {
		int xMid = (xMin+xMax)/2; int yMid = (yMin + yMax)/2;
		
		float avg = (heightmap[xMin][yMin] + heightmap[xMin][yMax] +
				heightmap[xMax][yMin] + heightmap [xMax][yMax])/4;

		heightmap[xMid][yMid] = Math.max(Math.min(avg + (roughness*(rGen.nextFloat()-0.5f)*2), 1), 0);
	}


	private void radialGradient(float[][] world) { //TODO fix this
		float maxDist = size/2f;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				float dx = (i-maxDist)/maxDist; float dy = (j-maxDist)/maxDist;
				float distance = (float)Math.sqrt(dx*dx+dy*dy);
				float scale = Math.max(1-distance, 0);
				
				world[i][j] *= scale;
			}
		}
	}


}
