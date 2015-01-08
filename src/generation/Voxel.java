package generation;

import util.Vector3;

public class Voxel {
	
	public float[] verts;
	public float[] colors;
	public int[] indices;
	
	public Voxel(float size) {
		verts = new float[] {//X, 	Y, 		Z,		 W
							0, 		0, 		0,		 1, //near LL - 0
							size, 	0, 		0,		 1, //near LR - 1
							0, 		size, 	0,		 1, //near UL - 2
							size, 	size, 	0,		 1, //near UR - 3
							0, 		0, 		size,	 1, //far LL  - 4
							size,	0, 		size,	 1, // far LR - 5
							0, 		size,	size,	 1, // far UL - 6
							size,	size,	size,	 1}; //far UR - 7
		colors = new float[] {//R, 		  G, 	   B,	   A
								85/255f,	107/255f,	47/255f,1, //near LL
								85/255f,	107/255f,	47/255f,1, //near LR
								107/255f,142/255f,	35/255f,1, //near UL
								107/255f,142/255f,	35/255f,1, //near UR
								85/255f,	107/255f,	47/255f,1, //far LL
								85/255f,	107/255f,	47/255f,1, //far LR
								107/255f,142/255f,	35/255f,1, //far UL
								107/255f,142/255f,	35/255f,1};//far UR
		
		indices = new int[] {
							0, 1, 2, //near lower
							1, 2, 3, //near upper
							2, 3, 6, //top near
							3, 6, 7, //top far
							6, 7, 4, //far upper
							4, 5, 7, //far lower
							4, 5, 1, //bottom far
							1, 0, 4, //bottom near
							6, 4, 0, //left far
							0, 2, 6, //left near
							3, 5, 7, //right far
							1, 3, 5};//right near
	}
	
	public void translate(Vector3 transform) {
		for (int i = 0; i < verts.length; i+=4) {
			verts[i] += transform.x;
			verts[i+1] += transform.y;
			verts[i+2] += transform.z;
		}
	}
	
//	public void color(Vector3 color) {
//		
//	}
	
	public void indexOffset(int amt) {
		for (int i = 0; i < indices.length; i++) {
			indices[i] += amt;
		}
	}

}
