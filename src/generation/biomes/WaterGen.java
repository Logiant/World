package generation.biomes;

import java.util.Random;

public class WaterGen {

	
	Random rGen;
	
	public WaterGen(long seed) {
		rGen = new Random(seed);
	}

	public float[][] generateRivers(float[][] heightmap) {
		// TODO Auto-generated method stub
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
		return heightmap;
	}
}
