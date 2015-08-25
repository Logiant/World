package world.test;

import java.io.IOException;
import java.util.Random;

import generation.GenTest2;
import util.Vector3;
import world.*;

public class WorldManager {
	public static float VOXEL_SIZE = 1f;
	
	int width; //world width in regions
	int height; //world height in regions
	
	RegionManager[][] loadedRegions; //when the player changes regions, shift these around
									//iterate through and load any unloaded regions (bool load == false) ?
									//unload regions which are shifted out (List<RegionManager> regionsToRemove) ?
									//be prepared to write regions to disk if required... ?
									//use threading to load unload - okay as long as regions are out of sight
	GenTest2 mapgen;
	float[][] map;
	Random rGen;
	
	public void GenMap() {
		try {
			mapgen = new GenTest2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map = mapgen.getMap();
	}
	
	public void Build(Vector3 playerPos) {
		//calculate player x, z region location
		int regionX = (int)(playerPos.x / RegionManager.NUM_CHUNKS / Chunk.CHUNK_WIDTH);
		int regionZ = (int)(playerPos.z / RegionManager.NUM_CHUNKS / Chunk.CHUNK_DEPTH);
		
		//load chunks x+-n to z+-n
		
	}
	
}
