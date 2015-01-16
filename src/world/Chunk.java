package world;

import lwjgl3Test.VBORender;
import util.ArrayHelper;
import util.Vector3;
import generation.HeightMap;
import generation.World;

public class Chunk {
	static int CHUNK_WIDTH = 16;
	static int CHUNK_DEPTH = 16;
	static int CHUNK_HEIGHT = 16;
	
	private int drawId;
	private int indicesCount;
	
	int numVoxels;
	Voxel[][][] world;
	
	int dx, dz;
	
	public Chunk(int xPos, int zPos) {
		dx = xPos * CHUNK_WIDTH * World.VOXEL_SIZE;
		dz = zPos * CHUNK_DEPTH * World.VOXEL_SIZE;
	}
	
	public void Build(Long seed, VBORender graphics) {
		CHUNK_WIDTH = 16;
		CHUNK_DEPTH = 16;
		CHUNK_HEIGHT = 16;
		int[][] heightmap = HeightMap.GeneratePlains(CHUNK_WIDTH, CHUNK_DEPTH, 4, 4, seed, CHUNK_HEIGHT);
		world = new Voxel[CHUNK_WIDTH][CHUNK_DEPTH][CHUNK_HEIGHT];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				int zoneHeight = (heightmap[i][j]);
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					world[i][j][h] = new Voxel(World.VOXEL_SIZE);
					world[i][j][h].translate(new Vector3(i*World.VOXEL_SIZE + dx, h*World.VOXEL_SIZE, j*World.VOXEL_SIZE + dz));
					world[i][j][h].hidden = true;
					numVoxels ++;
					if (h == zoneHeight || (h <= zoneHeight && heightmap[Math.abs(i-1)%CHUNK_WIDTH][j] < h)
							|| (h <= zoneHeight && heightmap[(i+1)%CHUNK_WIDTH][j] < h)
							|| (h <= zoneHeight && heightmap[i][Math.abs(j-1)%CHUNK_DEPTH] < h)
							|| (h <= zoneHeight && heightmap[i][(j+1)%CHUNK_DEPTH] < h)) {
						world[i][j][h].hidden = false;
					}
				}
			}
		}
		
		drawId = graphics.createVBO(getVerts(), getColors(), getIndices());
		indicesCount = getIndices().length;
	}
	
	public void draw(VBORender graphics) {
		graphics.render(drawId, indicesCount);
	}

	public float[] getVerts() {
		float[] verts = new float[0];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					if (!world[i][j][h].hidden) {
						verts = ArrayHelper.CopyArray(verts, world[i][j][h].verts);
					}
				}
			}
		}
		return verts;
	}
	
	public float[] getColors() {
		float[] cols = new float[0];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					if (!world[i][j][h].hidden) {
						cols = ArrayHelper.CopyArray(cols, world[i][j][h].colors);
					}
				}
			}
		}
		return cols;
	}
	
	public int[] getIndices() {
		int offset = 0;
		int[] inds = new int[0];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					if (!world[i][j][h].hidden) {
						world[i][j][h].indexOffset(offset);
						inds = ArrayHelper.CopyArray(inds, world[i][j][h].indices);
						offset += 8;
					}
				}
			}
		}
		return inds;
	}

}