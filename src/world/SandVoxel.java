package world;

public class SandVoxel extends Voxel {

	public SandVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				205/255f,	198/255f,	115/255f,1, //near LL
				205/255f,	198/255f,	115/255f,1, //near LR
				238/255f,	230/255f,	133/255f,1, //near UL
				238/255f,	230/255f,	133/255f,1, //near UR
				205/255f,	198/255f,	115/255f,1, //far LL
				205/255f,	198/255f,	115/255f,1, //far LR
				238/255f,	230/255f,	133/255f,1, //far UL
				238/255f,	230/255f,	133/255f,1};//far UR
	}
}
