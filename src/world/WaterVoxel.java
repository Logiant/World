package world;

public class WaterVoxel extends Voxel {

	public WaterVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				28f/255f,	134/255f,	238/255f,1, //near LL
				28f/255f,	134/255f,	238/255f,1, //near LR
				99f/255f,	184/255f,	255/255f,1, //near UL
				99f/255f,	184/255f,	255/255f,1, //near UR
				28f/255f,	134/255f,	238/255f,1, //far LL
				28f/255f,	134/255f,	238/255f,1, //far LR
				99f/255f,	184/255f,	255/255f,1, //far UL
				99f/255f,	184/255f,	255/255f,1};//far UR
	}
}
