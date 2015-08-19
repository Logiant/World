package generation.biomes;

import world.voxels.*;

public class BiomeToVoxel {


	public static Voxel getVoxel(BIOME b) {
		Voxel v = null;
		switch(b) {
		case OCEAN:
			v = new OceanVoxel();
			break;
		case RIVER:
			v = new WaterVoxel();
			break;
		default:
		case TEMP_RAINFOREST:
		case TEMP_DECIDUOUS:
		case TEMP_DESERT:
		case TAIGIA:
		case SHRUBLAND:
			v = new Voxel();
			break;
		case SNOW:
		case TUNDRA:
		case BARE: 
		case SCORCHED:
			v = new MountainVoxel();
			break;
		}
		return v;
	}

}
