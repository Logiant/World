package generation.test;

import generation.biomes.BIOME;

import java.util.Random;


public class BiomeGen {



	Random rGen;

	public BiomeGen(long seed) {
		rGen = new Random(seed);
	}


	public BIOME[][] createBiomes(float[][] heightmap, float[][] binary, float[][] moisture) {
		int size = moisture.length;
		BIOME[][] biomes = new BIOME[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (binary[i][j] > 0 && moisture[i][j] < 1) { //not water
					float height = heightmap[i][j];
					float wetness = moisture[i][j];
					if (height < 1/4f) {
						biomes[i][j] = lowlands(wetness);
					} else if (height < 2/4f) {
						biomes[i][j] = midlands(wetness);
					} else if (height < 3/4f) {
						biomes[i][j] = highlands(wetness);
					} else {
						biomes[i][j] = mountains(wetness);
					}


				} else if (binary[i][j] > 0) {
					biomes[i][j] = BIOME.RIVER;
				}else {
					biomes[i][j] = BIOME.OCEAN;
				}
			}
		}
		return biomes;
	}



	//TODO use sorted array with (1/6f*size) as threshold
	
	
	private BIOME lowlands(float wetness) {
		BIOME biome = BIOME.OCEAN;
		if (wetness < 1/6f) {
			biome = BIOME.DESERT;			
		} else if (wetness < 2/6f) {
			biome = BIOME.GRASSLAND;
		} else if (wetness < 4/6f) {
			biome = BIOME.TROP_SEASONAL;
		}  else  {
			biome = BIOME.TROP_RAINFOREST;
		}
		return biome;
	}

	private BIOME midlands(float wetness) {
		BIOME biome = BIOME.OCEAN;
		if (wetness < 1/6f) {
			biome = BIOME.TEMP_DESERT;			
		} else if (wetness < 3/6f) {
			biome = BIOME.GRASSLAND;
		} else if (wetness < 5/6f) {
			biome = BIOME.TEMP_DECIDUOUS;
		}  else  {
			biome = BIOME.TEMP_RAINFOREST;
		}
		return biome;
	}
	
	private BIOME highlands(float wetness) {
		BIOME biome = BIOME.OCEAN;
		if (wetness < 2/6f) {
			biome = BIOME.TEMP_DESERT;			
		} else if (wetness < 4/6f) {
			biome = BIOME.SHRUBLAND;
		}else  {
			biome = BIOME.TAIGIA;
		}
		return biome;
	}
	
	private BIOME mountains(float wetness) {
		BIOME biome = BIOME.OCEAN;
		if (wetness < 1/6f) {
			biome = BIOME.TUNDRA;			
		} else if (wetness < 2/6f) {
			biome = BIOME.TUNDRA;
		} else if (wetness < 3/6f) {
			biome = BIOME.SNOW;
		}  else  {
			biome = BIOME.SNOW;
		}
		return biome;
	}


}
