package generation;

import util.Vector3;

public class World {

	long seed = 5;

	float voxelSize = 2;
	
	int width;
	int depth;
	int height;

	int numVoxels;

	Voxel[][][] world;
	
	public void Build() {
		width = 31;
		depth = 31;
		height = 25;
		int[][] heightMap = HeightMap.GeneratePlains(width, depth, 3, 3, seed, height);
		world = new Voxel[width][depth][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j++) {
				int zoneHeight = (heightMap[i][j]);
				for (int h = 0; h < height; h++) {
					world[i][j][h] = new Voxel(voxelSize);
					world[i][j][h].translate(new Vector3(i*voxelSize, h*voxelSize, j*voxelSize));
					world[i][j][h].hidden = true;
		//			world[i][j][h].indexOffset(numVoxels * 8);
					numVoxels ++;
					if (h == zoneHeight || (h <= zoneHeight && i > 0 && heightMap[i-1][j] < h)
							|| (h <= zoneHeight && i < width-1 && heightMap[i+1][j] < h)
							|| (h <= zoneHeight && j > 0 && heightMap[i][j-1] < h)
							|| (h <= zoneHeight && j < depth-1 && heightMap[i][j+1] < h)) {
						world[i][j][h].hidden = false;
					}
				}
			}
		}
	}

	public float[] getVerts() {
		float[] verts = new float[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j++) {
				for (int h = 0; h < height; h++) {
					if (!world[i][j][h].hidden) {
						verts = combine(verts, world[i][j][h].verts);
					}
				}
			}
		}
		return verts;
	}
	
	public float[] getColors() {
		float[] cols = new float[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j++) {
				for (int h = 0; h < height; h++) {
					if (!world[i][j][h].hidden) {
						cols = combine(cols, world[i][j][h].colors);
					}
				}
			}
		}
		return cols;
	}
	
	public int[] getIndices() {
		int offset = 0;
		int[] inds = new int[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j++) {
				for (int h = 0; h < height; h++) {
					if (!world[i][j][h].hidden) {
						world[i][j][h].indexOffset(offset);
						inds = combine(inds, world[i][j][h].getIndices(Face.ALL));
						offset += 8;
					}
				}
			}
		}
		return inds;
	}

	
	private int[] combine(int[] a, int[] b){
        int length = a.length + b.length;
        int[] result = new int[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
	
	private float[] combine(float[] a, float[] b){
        int length = a.length + b.length;
        float[] result = new float[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }


}
