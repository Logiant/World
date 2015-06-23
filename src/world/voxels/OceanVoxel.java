package world.voxels;

public class OceanVoxel extends Voxel {

	public OceanVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				99f/255f,	107/255f,	160/255f,1, //near LL
				99f/255f,	107/255f,	160/255f,1, //near LR
				28f/255f,	99f/255f,	125/255f,1, //near UL
				28f/255f,	99f/255f,	125/255f,1, //near UR
				99f/255f,	107/255f,	160/255f,1, //far LL
				99f/255f,	107/255f,	160/255f,1, //far LR
				28f/255f,	99f/255f,	125/255f,1, //far UL
				28f/255f,	99f/255f,	125/255f,1};//far UR
	}
}
