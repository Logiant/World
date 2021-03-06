package generation.biomes;

import java.util.Random;

public class WindGen {
	
	Random rGen;
	
	public WindGen(long seed) {
		rGen = new Random(seed);
	}
	
	
	
	
	
	public float[][] generateWind(float[][] map) {
		int width = map.length;
		int height = map[0].length;

		boolean north = rGen.nextBoolean();
		boolean east = rGen.nextBoolean();
		boolean south = rGen.nextBoolean();
		boolean west = rGen.nextBoolean();
		
		
		west = (west) || (!north && !east && !south && !west);
		
		
		float[][] northWind = northWind(map, width, height);
		float[][] eastWind = eastWind(map, width, height);
		float[][] southWind = southWind(map, width, height);
		float[][] westWind = westWind(map, width, height);

		float[][] windMap = new float[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float mul = 1;
				if (north)
					mul *= (1-northWind[i][j]);
				if (east)
					mul *= (1-eastWind[i][j]);
				if (south)
					mul *= (1-southWind[i][j]);
				if (west)
					mul *= (1-westWind[i][j]);
					
				windMap[i][j] = 1 - mul;
			}
		}

		
		return windMap;
	}
	
	private float[][] northWind(float[][] map, int width, int height) {
		float[][] northWind = new float[width][height];
		int x0 = 0; int xf = width;
		int y0 = 0; int yf = height;
		int dx = 1;
		int dy = 1;

		int x = x0; int y = y0;
		while (y != yf) {
			while (x != xf) {
				//set wind speed based on map height
				if (y-dy < 0 || y-dy >= height) { //on edge
					northWind[x][y] = 1;
				} else {
					float dh = Math.abs(map[x][y] - map[x][y-dy]);
					northWind[x][y] = Math.max(northWind[x][y-dy] - dh, 0); //previous value
				}
				x = x + dx;
			}
			y = y + dy;
			x = x0;
		}
		return northWind;
	}
	
	private float[][] eastWind(float[][] map, int width, int height) {
		float[][] eastWind = new float[width][height];
		int x0 = width-1; int xf = -1;
		int y0 = 0; int yf = height;
		int dx = -1;
		int dy = 1;

		int x = x0; int y = y0;
		while (x != xf) {
			while (y != yf) {
				//set wind speed based on map height
				if (x-dx < 0 || x-dx >= width) { //on edge
					eastWind[x][y] = 1;
				} else {
					float dh = Math.abs(map[x][y] - map[x-dx][y]);
					eastWind[x][y] = Math.max(eastWind[x-dx][y] - dh, 0); //previous value
				}
				y = y + dy;
			}
			x = x + dx;
			y = y0;
		}
		return eastWind;
	}
	
	private float[][] southWind(float[][] map, int width, int height) {
		float[][] southWind = new float[width][height];
		int x0 = 0; int xf = width;
		int y0 = height - 1; int yf = -1;
		int dx = 1;
		int dy = -1;

		int x = x0; int y = y0;
		while (y != yf) {
			while (x != xf) {
				//set wind speed based on map height
				if (y-dy < 0 || y-dy >= height) { //on edge
					southWind[x][y] = 1;
				} else {
					float dh = Math.abs(map[x][y] - map[x][y-dy]);
					southWind[x][y] = Math.max(southWind[x][y-dy] - dh, 0); //previous value
				}
				x = x + dx;
			}
			y = y + dy;
			x = x0;
		}
		return southWind;
	}
	
	private float[][] westWind(float[][] map, int width, int height) {
		float[][] westWind = new float[width][height];
		int x0 = 0; int xf = width;
		int y0 = 0; int yf = height;
		int dx = 1;
		int dy = 1;

		int x = x0; int y = y0;
		while (x != xf) {
			while (y != yf) {
				//set wind speed based on map height
				if (x-dx < 0 || x-dx >= width) { //on edge
					westWind[x][y] = 1;
				} else {
					float dh = Math.abs(map[x][y] - map[x-dx][y]);
					westWind[x][y] = Math.max(westWind[x-dx][y] - dh, 0); //previous value
				}
				y = y + dy;
			}
			x = x + dx;
			y = y0;
		}
		return westWind;
	}

}
