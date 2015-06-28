package generation.civ;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityGen {

	Random rGen;
	List<City> cities;

	public CityGen(long seed) {
		rGen = new Random(seed);
		cities = new ArrayList<City>();
	}

	public float[][] buildCities(float[][] heightmap, float[][] moisture, float[][] binary) {
		
		int size = moisture.length;
		int numCities = size;

		float[][] civMap = new float[size][size];

		List<int[]> coords = new ArrayList<int[]>();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (moisture[i][j] > 0.5 && moisture[i][j] < 1 && binary[i][j] > 0.5) {
					int[] c = {i, j};
					coords.add(c);
				}
			}
		}
		
		for (int i = 0; i < numCities; i++) {
			if (coords.size() == 0)
				break;
			int index = rGen.nextInt(coords.size());
			int[] c = coords.get(index);
			coords.remove(index);
			civMap[c[0]][c[1]] = 1;
			cities.add(new City(c[0], c[1]));
		}
		
		growCities(civMap, binary);
		
		return civMap;
	}
	
	private void growCities(float[][] civMap, float[][] binaryMap) {
		
	}

}
