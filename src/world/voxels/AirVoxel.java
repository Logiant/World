package world.voxels;

public class AirVoxel extends Voxel {

	public AirVoxel(float size) {
		super(size);
		colors = new float[] {//R, 		  G, 	   B,	   A
				255/255f,	255/255f,	255/255f,1, //near LL
				255/255f,	255/255f,	255/255f,1, //near LR
				255/255f,	255/255f,	255/255f,1, //near UL
				255/255f,	255/255f,	255/255f,1, //near UR
				255/255f,	255/255f,	255/255f,1, //far LL
				255/255f,	255/255f,	255/255f,1, //far LR
				255/255f,	255/255f,	255/255f,1, //far UL
				255/255f,	255/255f,	255/255f,1};//far UR
	}

	@Override
	public void setVisible(boolean visible) { }

	@Override
	public boolean isVisible() {
		return false;
	}
}
