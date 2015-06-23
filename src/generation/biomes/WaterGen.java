package generation.biomes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


public class WaterGen {


	Random rGen;

	public WaterGen(long seed) {
		rGen = new Random(seed);
	}

	public float[][] generateRivers(float[][] heightmap) {
		// TODO Auto-generated method stub

		int numPts = (int)(heightmap.length * heightmap[0].length * 0.2);

		List<Coordinate> highPts = getHighestPoints(heightmap, numPts);

		int riverMouths = rGen.nextInt(5) + 10;

		List<Coordinate> mouths = new ArrayList<Coordinate>(riverMouths);

		for (int i = 0; i < numPts; i++) {
			if (highPts.get(i) == null) {
				System.err.println("WARNING! NULL POINT!");
				System.exit(-1);
			}
		}
		
		
		for (int i = 0; i < riverMouths; i++) {
			int index = rGen.nextInt(highPts.size());
			mouths.add(highPts.get(index));
			highPts.remove(index);

			System.out.println(mouths.get(i));
		}
		
		int offset = 10;
		
		for (int i = 0; i < riverMouths; i++) {
			mouths.get(i).x += rGen.nextInt(offset) - offset/2;;
			mouths.get(i).y += rGen.nextInt(offset) - offset/2;;
		}




		float[][] rivers = buildRivers(heightmap, mouths);

		/*
		 * Get the n highest points
		 * chose m points (m <=n) to be river starts
		 * flow rivers downhill
		 * form mega-rivers
		 * form/join existing lakes if a local minima is hit
		 * flow into the sea
		 * tributaries???
		 * 
		 */
		return rivers;
	}



	public float[][] buildRivers(float[][] heightmap, List<Coordinate> starts) {
		int width = heightmap.length; int height = heightmap[0].length;
		float[][] riverMap = new float[width][height];



		Queue<Coordinate> q = new LinkedList<Coordinate>();

		q.addAll(starts);




		while (!q.isEmpty()) {

			Coordinate current = q.poll();
			int x = current.x; int y = current.y;

			float min = 1;
			Coordinate lowest = null;

			riverMap[x][y] += 1f;

			for (int dx = -1; dx <= 1; dx++) {
				if (x + dx >=0 && x + dx < width) {
					for (int dy = -1; dy <= 1; dy++) {
						if (y + dy >= 0 && y + dy < height) {
							if (heightmap[x+dx][y+dy] < min) {
								min = heightmap[x+dx][y + dy];
								lowest = new Coordinate(x+dx, y+dy);
							}
						}
					}
				}
			}

			if (heightmap[x][y] > 0) {
				if (lowest != null && !(lowest.x == x && lowest.y == y)) { //if there is something lower
					if (min < heightmap[x][y]) {
						q.add(lowest);
					} else if (riverMap[x][y] > 0) {
						q.add(lowest);
						riverMap[lowest.x][lowest.y] = riverMap[x][y] - 1.1f;
					}

				} else if (riverMap[x][y] > 0) { //we're in a hole. Fill up!
					for (int dx = -1; dx <= 1; dx++) {
						if (x + dx >=0 && x + dx < width) {
							for (int dy = -1; dy <= 1; dy++) {
								if (y + dy >= 0 && y + dy < height) {
									if (!(dx == 0 && dy == 0) && riverMap[x+dx][y+dy] == 0) {
										q.add(new Coordinate(x+dx, y+dy));
										riverMap[x+dx][y+dy] = riverMap[x][y] - 1.25f;
									}
								}
							}
						}
					}

				}
			}
		}

		return riverMap;

	}




	public List<Coordinate> getHighestPoints(float[][] heightmap, int numPts) {

		//create list of highest points in the world
		List<Coordinate> highestPts = new ArrayList<Coordinate> (numPts);

		//float[] holding the currest highest points
		float[] maxHeights = new float[numPts];
		float minMax = 0; //the smallest high point

		for (int k = 0; k < numPts; k++) {
			highestPts.add(null); //add initial null dummy points
		}

		//for each square
		for (int i = 0; i < heightmap.length; i++) {
			for (int j = 0; j < heightmap[0].length; j++) {

				float h = heightmap[i][j]; //get the square's height

				if (h > minMax) { //if h > the min high point
					float min = 1; //min val
					int mini = 0; //min index
					for (int k = 0; k < numPts; k++) {
						if (maxHeights[k] < min) { //get minimum point and index
							min = maxHeights[k];
							mini = k;
						}
					}
					minMax = Math.min(min, h); //get new minMax
					maxHeights[mini] = h;
					highestPts.set(mini, new Coordinate(i, j));
				}

			}
		}

		return highestPts;
	}


	class Coordinate {

		public int x, y;

		public Coordinate(int x, int y) {
			this.x = x; this.y = y;
		}

		public String toString() {
			return x + ", " + y;
		}
	}
}
