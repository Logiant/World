package world.test;

import java.util.Random;

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

	Random rGen;
	
}
