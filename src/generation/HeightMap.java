package generation;

import java.util.LinkedList;
import java.util.Queue;
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

	/*SMOOTHING ALGORITHM*/
	public static float[][] Smooth(float[][] heights, float maxHeight) {
		int smoothness = 1; //gaussian kernel size
		int width = heights.length;
		int depth = heights[0].length;
		
		float[][] newVals = new float[width][depth];
		float maxOut = 0;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j++) {
				float sum = 0;
				int count = 0;
				
				for (int x = -smoothness; x < smoothness; x++) {
					if (i+x >= 0 && i + x < width) {
						for (int y = -smoothness; y < smoothness; y++) {
							if (j + y >= 0 && j + y < depth) {
								sum += heights[i+x][j+y];
								count++;
							}
						}
					}
				}
				maxOut = Math.max(maxOut, sum/count);
				newVals[i][j] = sum/count;
			}
		}
		if (maxOut > 0) {
			float scale = maxHeight/maxOut;
			for (int i = 0; i<width; i++) {
				for (int j = 0; j<depth; j++) {
					newVals[i][j] *= scale;
				}
			}
		}
		return newVals;
	}

	/*Voronoi Algorithm*/
	public static float[][] Voronoi(float[][] map, int numPts) {

		int[] x = new int[numPts];
		int[] y = new int[numPts];
		for (int i = 0; i < numPts; i++) {
			x[i] = rGen.nextInt(map.length - 2*numPts) + numPts;
			y[i] = rGen.nextInt(map[0].length - 2*numPts) + numPts;
		}

		float dColor = 1f/(numPts+1);
		float colorMod = 1;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {

				float color = 0;
				for (int k = 0; k < numPts; k++) {
					if (i == x[k] && j == y[k]) {
						color = colorMod;
						colorMod -= dColor;
						break;
					}
				}

				map[i][j] = color;
			}
		}
		return VoronoiFill(map, x, y);
	}

	private static float[][] VoronoiFill(float[][] map, int[] x0, int[] y0) {
		Queue<int[]> q = new LinkedList<int[]>();
		float[][] mtns = new float[map.length][map[0].length];
		for (int i = 0; i < mtns.length; i++) {
			for (int j = 0; j < mtns[0].length; j++) {
				mtns[i][j] = 0;
			}
		}
		//add all initial positions to the queue
		for (int k = 0; k < x0.length; k++) {
			int[] pos = {x0[k], y0[k]};
			q.add(pos);
		}
		//add the four neighbors of each point
		int[] pos;
		while(!q.isEmpty()) {
			pos = q.poll();
			int x = pos[0]; int y = pos[1];
			//add all uncolored neighbors
			//left
			if (x - 1 >= 0) {
				if (map[x-1][y] == 0) {
					map[x-1][y] = map[x][y];
					int[] nextPos = {x-1, y};
					q.add(nextPos);
				} else if (map[x-1][y] != map[x][y]) {
					mtns[x-1][y] = 1;
				}
			}
			//top
			if (y - 1 >= 0) {
				if (map[x][y-1] == 0) {
					map[x][y-1] = map[x][y];
					int[] nextPos = {x, y-1};
					q.add(nextPos);
				}
				else if (map[x][y-1] != map[x][y]) {
					mtns[x][y-1] = 1;
				}
			} 
			//right
			if (x + 1 < map.length && map[x+1][y] == 0) {
				map[x+1][y] = map[x][y];
				int[] nextPos = {x+1, y};
				q.add(nextPos);
			} 
			//bottom
			if (y + 1 < map[0].length && map[x][y+1] == 0) {
				map[x][y+1] = map[x][y];
				int[] nextPos = {x, y+1};
				q.add(nextPos);
			} 
		}
		return mtns;
	}


	/* STACK FLOOD FILL ALGORITHM */
	public static void FloodFill(float[][] original, int x, int y, float[][] target) {
		int[] position = {x, y}; //current square

		Queue<int[]> q = new LinkedList<int[]>();
		q.add(position);

		while (!q.isEmpty()) {
			int[] next;
			position = q.poll();
			x = position[0]; y = position[1];
			if (original[x][y] > 0.5f && target[x][y] != 1) { //if target node hasn't been filled
				target[x][y] = 1; //set target node to filled
				//check neighbors
				if (original[x+1][y] > 0.5 && target[x+1][y] != 1) { // if left node is valid
					next = new int[] {x+1, y};
					q.add(next);
				}if (original[x-1][y] > 0.5 && target[x-1][y] != 1) { // if left node is valid
					next = new int[] {x-1, y};
					q.add(next);
				}if (original[x][y+1] > 0.5 && target[x][y+1] != 1) { // if left node is valid
					next = new int[] {x, y+1};
					q.add(next);
				}if (original[x][y-1] > 0.5 && target[x][y-1] != 1) { // if left node is valid
					next = new int[] {x, y-1};
					q.add(next);
				}



			}



		}



	}



	/*RADIAL GRADIENT ALGORITHM*/
	public static float[][] RadGrad(int size) {
		float[][] grad = new float[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				double dx = i - size/2.0;
				double dy = j - size/2.0;

				float dist = (float)Math.sqrt(dx*dx + dy*dy);
				float val = 1 - Math.min((dist/size), 1);

				if (dist >= size/2f - 5) {
					val = 0;
				}

				grad[i][j] = val;

			}
		}



		return grad;
	}


	/*PERLIN NOISE ALGORITHM*/

	public static float[][] PerlinNoise(int width, int depth, int dx, int dz, long seed, float persistance){
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

				map[i - 2*dx][j - 2*dz] = val*persistance;

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
