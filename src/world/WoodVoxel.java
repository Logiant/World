package world;

public class WoodVoxel extends Voxel {

	public WoodVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				195/255f,	148/255f,	89/255f,1, //near LL
				195/255f,	148/255f,	89/255f,1, //near LR
				210/255f,	155/255f,	95/255f,1, //near UL
				210/255f,	155/255f,	95/255f,1, //near UR
				195/255f,	148/255f,	89/255f,1, //far LL
				195/255f,	148/255f,	89/255f,1, //far LR
				210/255f,	155/255f,	95/255f,1, //far UL
				210/255f,	155/255f,	95/255f,1};//far UR
	}
}
