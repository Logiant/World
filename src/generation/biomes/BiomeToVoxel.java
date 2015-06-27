package generation.biomes;

import world.voxels.*;

public class BiomeToVoxel {


	public static Voxel getVoxel(BIOME b, float voxelSize) {
		Voxel v = null;
		switch(b) {
		case OCEAN:
			v = new OceanVoxel(voxelSize);
			break;
		case RIVER:
			v = new WaterVoxel(voxelSize);
			break;
		default:
		case TEMP_RAINFOREST:
		case TEMP_DECIDUOUS:
		case TEMP_DESERT:
		case TAIGIA:
		case SHRUBLAND:
			v = new Voxel(voxelSize);
			break;
		case SNOW:
		case TUNDRA:
		case BARE: 
		case SCORCHED:
			v = new MountainVoxel(voxelSize);
			break;
		}
		return v;
	}

}
