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
				map[i][j] = rGen.nextInt(maxHeight - 3*maxHeight/4) + 3*maxHeight/4;
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
					if (xR!= 0) {
						rx += wRes;
					}if (yR != 0) {
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
}
