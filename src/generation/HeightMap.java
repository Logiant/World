package generation;

import java.util.Random;

public class HeightMap {

	private static Random rGen = new Random();

	/**
	 * returns a heightmap with integer heights from [0, maxHeight] with plains generation
	 * @param width desired width of map
	 * @param height desired height of map
	 */
	public static int[][] GeneratePlains(int width, int height, int wRes, int vRes, long seed, int maxHeight) {
		rGen.setSeed(seed);

		int[][] map = new int[width][height];
		for (int i = 0; i < width; i += wRes) {
			for (int j = 0; j < height; j+= vRes) {
				map[i][j] = rGen.nextInt(maxHeight - 3*maxHeight/4) + maxHeight/4;
			}
		}
		for (int j = 0; j < height; j += 1) {
			for (int i = 0; i < width; i+= 1) {
				int xR = i % wRes;
				int x = (int) (i/wRes);
				int yR = j % vRes;
				int y = (int) (j/vRes);
				if (xR != 0 || yR != 0) { //space isn't defined
					int lx = x*wRes; //left defined x
					int rx = lx; //don't change if on defined
					int uy = y*vRes; //topmost initial y
					int ly = uy; //don't change if on defined
					if (xR!= 0 && rx + wRes < width) {
						rx += wRes;
					}if (yR != 0 && ly + vRes < height) {
						ly += vRes;
					}
					map[i][j] = (int)(interpolate(map, lx, rx, uy, ly, (float)xR/wRes, (float)yR/vRes) + .5);
				}
			}
		}
		return map;
	}

	private static float interpolate(int[][] map, int xl, int xr, int yu, int yl, float dx, float dy) {
		float topVal = (map[xl][yu]*(1-dx) + map[xr][yu]*(dx));
		float botVal = (map[xl][yl]*(1-dx) + map[xr][yl]*(dx));
		return (topVal*(1-dy) + botVal*(dy));
	}

	/*PERLIN NOISE ALGORITHM*/
	
	public static float[][] PerlinNoise(int width, int depth, int dx, int dz, long seed){
		rGen.setSeed(seed);
		float[][] buffermap = new float[width + 4*dx][depth + 4*dz]; //create map with a buffer
		for (int i = 0; i < buffermap.length; i += dx) {
			for (int j = 0; j < buffermap[0].length; j += dz) {
				buffermap[i][j] = rGen.nextFloat();
			}
		}
		float[][] map = new float[width][depth];
		
		for (int i = 2*dx; i < width + 2*dx; i ++) {
			for (int j = 2*dz; j < depth + 2*dz; j ++) {
				
				
				int xInt = i/dx;
				int zInt = j/dz;
				float xFraction = i%dx / (float)dx;
				float zFraction = j%dz / (float)dz;
				
				
				float below2Val = cubicInterpolation(buffermap[(xInt-1)*dx][(zInt-1)*dz], buffermap[xInt*dx][(zInt-1)*dz],
						buffermap[(xInt+1)*dx][(zInt-1)*dz], buffermap[(xInt+2)*dx][(zInt-1)*dz], xFraction);
				float belowVal = cubicInterpolation(buffermap[(xInt-1)*dx][zInt*dz], buffermap[xInt*dx][zInt*dz],
						buffermap[(xInt+1)*dx][zInt*dz], buffermap[(xInt+2)*dx][zInt*dz], xFraction);
				float aboveVal = cubicInterpolation(buffermap[(xInt-1)*dx][(zInt+1)*dz], buffermap[xInt*dx][(zInt+1)*dz],
						buffermap[(xInt+1)*dx][(zInt+1)*dz], buffermap[(xInt+2)*dx][(zInt+1)*dz], xFraction);
				float above2Val = cubicInterpolation(buffermap[(xInt-1)*dx][(zInt+2)*dz], buffermap[xInt*dx][(zInt+2)*dz],
						buffermap[(xInt+1)*dx][(zInt+2)*dz], buffermap[(xInt+2)*dx][(zInt+2)*dz], xFraction);
								
				float val = cubicInterpolation(below2Val, belowVal, aboveVal, above2Val, zFraction);
				
				map[i - 2*dx][j - 2*dz] = val;
				
			}
		}
		
		return map;
	}
	
	
	private static float cubicInterpolation(float y0, float y1, float y2, float y3, float x) {
		float P = (y3 - y2) - (y0 - y1);
		float Q = (y0 - y1) - P;
		float R = y2 - y0;
		float S = y1;		
		return Math.min(Math.max(P*x*x*x + Q*x*x + R*x + S, 0),1);
	}
	
	
	
	
	/* DIAMOND SQUARE ALGORITHM */

	public static float[][] DiamondSquare(int size, float roughness) {
		float[][] map = new float[size][size];
		map[0][0] = map[0][size-1] = map[size-1][0] = 
				map[size-1][size-1] = 0.5f;
		//generate each square
		for(int edgeLength = size -1; edgeLength >= 2; edgeLength /=2, roughness /=2) { //iterate for every square
			int halfSize = edgeLength/2;
			for (int x = 0; x < size - 1; x+= edgeLength) { //iterate through each square (x direction)
				for (int y = 0; y < size - 1; y+= edgeLength) { //iterate through each square (y direction)
					float avg = (map[x][y] + map[x+halfSize][y] + map[x][y+halfSize] + map[x+halfSize][y+halfSize])/4;
					map[x+halfSize][y+halfSize] = avg + (float)(Math.random()*2*roughness) - roughness;
				}	
			}
			//generate each diamond
			for (int x = 0; x < size - 1; x+= halfSize) { //iterate through each diamond (x direction)
				for(int y=(x+halfSize)%edgeLength;y<size-1;y+=edgeLength){
					float avg = 
							(map[(x-halfSize+size-1)%(size-1)][y] + //left of center
									map[(x+halfSize)%(size-1)][y] + //right of center
									map[x][(y+halfSize)%(size-1)] + //below center
									map[x][(y-halfSize+size-1)%(size-1)]) / 4f; //above center
					map[x][y] = avg + (float)(Math.random()*2*roughness) - roughness;
					//wrap values on the edges, remove
					//this and adjust loop condition above
					//for non-wrapping values.
					if(x == 0)  map[size-1][y] = avg;
					if(y == 0)  map[x][size-1] = avg;
				}
			}
		}
		return map;
	}
}
