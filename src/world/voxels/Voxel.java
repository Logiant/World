package world.voxels;

import util.Vector3;
import world.World;

enum Face {
	TOP, BOTTOM, NEAR, FAR, LEFT, RIGHT, ALL;
}

public class Voxel {
	//STATIC VARAIBLES
	static float size = World.VOXEL_SIZE;
	protected static float[] verts = new float[] {//X, 	Y, 		Z,		 W
											0, 		0,  	0,		1, //near LL - 0
											size,  	0,  	0,  	1, //near LR - 1
											0,  	size,  	0,	 	1, //near UL - 2
											size,  	size,  	0,	 	1, //near UR - 3
											0, 		0,		size,	1, //far LL  - 4
											size,	0,		size,	1, // far LR - 5
											0,  	size,	size,	1, // far UL - 6
											size,	size,	size,	1}; //far UR - 7
	
	protected static int[] indices = new int[] {//Xi,Yi,Zi
												1, 0, 2, //near lower
												1, 2, 3, //near upper
												3, 2, 6, //top near
												3, 6, 7, //top far
												7, 6, 4, //far upper
												4, 5, 7, //far lower
												5, 4, 1, //bottom far
												0, 1, 4, //bottom near
												4, 6, 0, //left far
												2, 0, 6, //left near
												5, 3, 7, //right far
												1, 3, 5};//right near
	//display variables
	protected float[] colors;
	protected boolean hidden;
	//offsets
	protected float dx, dy, dz;
	protected int indexOffset;
	
	public Voxel() {				
							//	R		G			B	    A
		colors = new float[] {85f/255f, 107/255f, 47f/255f, 1}; //bright color
	}
	
	//change voxel world position
	public void translate(Vector3 transform) {
		dx += transform.x; dy += transform.y; dz += transform.z;
	}
	
	public float[] getVerts() {
		float[] vertices = new float[verts.length];
		for (int i = 0; i < verts.length; i++) {
			vertices[i] = verts[i];
			if (i % 4  == 0) { //x
				vertices[i] += dx;
			} else if (i % 4 == 1) { // y
				vertices[i] += dy;
			} else if (i % 4 == 2) { //z
				vertices[i] += dz;
			}
		}
		
		return vertices;
	}
	
	
	public float[] getColor() {
		float[] cols = new float[colors.length * 8];
		
		for (int i = 0; i < cols.length; i++) {
			cols[i] = colors[i%4];
		}
		
		return cols;
	}
	
	public int[] getIndices() {
		int[] inds = new int[indices.length];
		
		for (int i = 0; i < indices.length; i++) {
			inds[i] = indices[i] + indexOffset;
		}
		
		return inds;
	}
	
	public void setVisible(boolean visible) {
		hidden = !visible;
	}
	
	public boolean isVisible() {
		return !hidden;
	}
	
	public void indexOffset(int amt) {
			indexOffset += amt;
	}

}
