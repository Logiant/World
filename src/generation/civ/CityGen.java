package generation.civ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CityGen {

	Random rGen;
	List<City> cities;

	public int maxID;
	
	public CityGen(long seed) {
		rGen = new Random(seed);
		cities = new ArrayList<City>();
	}

	public float[][] buildCities(float[][] heightmap, float[][] moisture, float[][] binary) {

		int size = moisture.length;
		int numCities = size;

		int[][] civMap = new int[size][size];

		List<int[]> coords = new ArrayList<int[]>();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (moisture[i][j] > 0.5 && moisture[i][j] < 1 && binary[i][j] > 0.5) {
					int[] c = {i, j};
					coords.add(c);
				}
			}
		}

		int cityNum = 1;

		for (int i = 0; i < numCities; i++) {
			if (coords.size() == 0)
				break;
			int index = rGen.nextInt(coords.size());
			int[] c = coords.get(index);
			coords.remove(index);
			civMap[c[0]][c[1]] = cityNum;
			cities.add(new City(c[0], c[1], cityNum++));
		}

		return growCities(civMap, binary);
	}

	private float[][] growCities(int[][] civMap, float[][] binaryMap) {
		int size = civMap.length;
		int openTiles = 0;
		List<Territory> territories = new ArrayList<Territory>();
		float[][] map = new float[size][size];
		
		int maxID = 0;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (binaryMap[i][j] > 0) openTiles ++;
				if (civMap[i][j] > 0) {
					territories.add(new Territory(civMap[i][j], i, j));
					maxID = Math.max(maxID, civMap[i][j]);
				}
			}
		}
		System.out.println(maxID + " max");
		
		int limit = 100000;
		
		List<Territory> toAdd = new LinkedList<Territory>();
		
		while (openTiles > 0 && limit > 0) {
			System.out.println(limit);
			for (Territory t: territories) {
				//pick a neighbor
				int i = t.x; int j = t.y;
				if (t.strength <= 1) {
					t.strength++;
				} else if (t.strength >=5 ) {
				//donothing
				} else {
					switch(rGen.nextInt(6)) {
					default:
					case 0: //fortify!
						t.strength ++;
						break;
					case 1:
						if (binaryMap[i+1][j] > 0) {
							if (civMap[i+1][j] == 0) {
								openTiles--;
								civMap[i+1][j] = t.id;
								t.strength --;
								toAdd.add(new Territory(t.id, i+1,j));
							}
						}
						break;
					case 2:
						if (binaryMap[i-1][j] > 0) {
							if (civMap[i-1][j] == 0) {
								openTiles--;
								civMap[i-1][j] = t.id;
								t.strength --;
								toAdd.add(new Territory(t.id, i-1,j));
							}						}
						break;
					case 3:
						if (binaryMap[i][j+1] > 0) {
							if (civMap[i][j+1] == 0) {
								openTiles--;
								civMap[i][j+1] = t.id;
								t.strength --;
								toAdd.add(new Territory(t.id, i,j+1));

							}						}
						break;
					case 4:
						if (binaryMap[i][j-1] > 0) {
							if (civMap[i][j-1] == 0) {
								openTiles--;
								civMap[i][j-1] = t.id;
								t.strength --;
								toAdd.add(new Territory(t.id, i,j-1));
							}						}
						break;
					}
				}
			}
			territories.addAll(toAdd);
			toAdd.clear();
			limit--;
		}
		
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (binaryMap[i][j] > 0) openTiles ++;
				if (civMap[i][j] > 0) {
					territories.add(new Territory(civMap[i][j], i, j));
					maxID = Math.max(maxID, civMap[i][j]);
				}
				map[i][j] = civMap[i][j];
			}
		}
		
		this.maxID = maxID;
		return map;
	}

}
