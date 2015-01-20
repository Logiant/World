package world;

public class MountainVoxel extends Voxel {

	public MountainVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				139/255f,	134/255f,	130/255f,1, //near LL
				139/255f,	134/255f,	130/255f,1, //near LR
				205/255f,	197/255f,	191/255f,1, //near UL
				205/255f,	197/255f,	191/255f,1, //near UR
				139/255f,	134/255f,	130/255f,1, //far LL
				139/255f,	134/255f,	130/255f,1, //far LR
				205/255f,	197/255f,	191/255f,1, //far UL
				205/255f,	197/255f,	191/255f,1};//far UR
	}
}
