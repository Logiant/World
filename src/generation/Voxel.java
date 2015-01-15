package generation;

import util.Vector3;

enum Face {
	TOP, BOTTOM, NEAR, FAR, LEFT, RIGHT, ALL;
}

public class Voxel {
	
	public float[] verts;
	public float[] colors;
	public int[] indices;
	
	public boolean hidden;
	
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
							1, 0, 2, //near lower
							1, 2, 3, //near upper
							3, 2, 6, //top near
							3, 6, 7, //top far
							7, 6, 4, //far upper
							4, 5, 7, //far lower
			//				5, 4, 1, //bottom far
			//				0, 1, 4, //bottom near
							4, 6, 0, //left far
							2, 0, 6, //left near
							5, 3, 7, //right far
							1, 3, 5};//right near
	}
	
	public int[] getIndices(Face f) {
		int[] indices = null;
		switch (f) {
		default:
		case ALL:
			indices = new int[this.indices.length];
			System.arraycopy(this.indices, 0, indices, 0, this.indices.length);
			break;
		case TOP:
			indices = new int[] {0};
			break;
		case BOTTOM:
			indices = new int[] {0};
			break;
		case NEAR:
			indices = new int[] {0};
			break;
		case FAR:
			indices = new int[] {0};
			break;
		case LEFT:
			indices = new int[] {0};
			break;
		case RIGHT:
			break;
		}
		return indices;
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
