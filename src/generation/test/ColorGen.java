package generation.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ColorGen {

	Random rGen;

	int size;
	
	List<Landmass> lands;

	public ColorGen(long seed) {
		rGen = new Random(seed);
		lands = new LinkedList<Landmass>();
	}


	public float[][][] color(float[][] height, float[][] binary) {
		size = height.length;
		float[][][] colored = new float[size][size][3];
		fillOcean(binary, colored);
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				float colorSum = colored[i][j][0] + colored [i][j][1] + colored[i][j][2];
				if (colorSum == 0) { //begin filling!
					if (binary[i][j] == 0) { //fill lakes!
					//	float[] color = {0/255f, 178/255f, 238/255f};
						
						float[] color = {rGen.nextFloat()/10f, rGen.nextFloat(), 245/255f};

						
						Landmass l = new Landmass("Default Lake", color);

						floodFill(binary, colored, i, j, l);
					} else { //fill land
					//	float[] color = {34/255f, 139/255f, 34/255f};
						
						float[] color = {rGen.nextFloat()/2f, rGen.nextFloat(), rGen.nextFloat()/2f};
						
						
						Landmass l = new Landmass("Default name", color);
						
						
						floodFill(binary, colored, i, j, l);
					}
				}
			}
		}
		
		
		
		return colored;
	}


	void floodFill(float[][] binary, float[][][] colored, int x0, int y0, Landmass land) { //TODO fix this code!
		int[] position = {x0, y0};

		float[] color = land.color;
		float r = color[0]; float g = color[1]; float b = color[2];
		
		Queue<int[]> q = new LinkedList<int[]>();
		q.add(position);
		while(!q.isEmpty()) {
			position = q.poll();
			int x = position[0]; int y = position[1];
			float colorSum = colored[x][y][0] + colored [x][y][1] + colored[x][y][2];
			if (colorSum == 0) {
				colored[x][y][0] = r; colored[x][y][1] = g; colored[x][y][2] = b;
				if (x+1 < size && binary[x+1][y] == binary[x][y]) {
					int[] newPos = {x+1, y};
					q.add(newPos);
				}
				if (y+1 < size && binary[x][y+1] == binary[x][y]) {
					int[] newPos = {x, y+1};
					q.add(newPos);
				}
				if (x-1 >= 0 && binary[x-1][y] == binary[x][y]) {
					int[] newPos = {x-1, y};
					q.add(newPos);
				}
				if (y-1 >= 0 && binary[x][y-1] == binary[x][y]) {
					int[] newPos = {x, y-1};
					q.add(newPos);
				}
			}
		}
	}
	
	
	void fillOcean(float[][] binary, float[][][] colored) { //TODO fix this code!
		float r = 16/255f; float g = 78/255f; float b = 139/255f;
		int[] position = {0, 0};

		Queue<int[]> q = new LinkedList<int[]>();
		q.add(position);
		while(!q.isEmpty()) {
			position = q.poll();
			int x = position[0]; int y = position[1];
			if (colored[x][y][0] == 0) {
				colored[x][y][0] = r; colored[x][y][1] = g; colored[x][y][2] = b;
				if (x+1 < size && binary[x+1][y] == 0) {
					int[] newPos = {x+1, y};
					q.add(newPos);
				}
				if (y+1 < size && binary[x][y+1] == 0) {
					int[] newPos = {x, y+1};
					q.add(newPos);
				}
				if (x-1 >= 0 && binary[x-1][y] == 0) {
					int[] newPos = {x-1, y};
					q.add(newPos);
				}
				if (y-1 >= 0 && binary[x][y-1] == 0) {
					int[] newPos = {x, y-1};
					q.add(newPos);
				}
			}
		}
	}
}
