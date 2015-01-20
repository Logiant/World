package world;

import lwjgl3Test.VBORender;
import util.ArrayHelper;
import util.Vector3;
import generation.World;

public class Chunk {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;
	public static final int CHUNK_HEIGHT = 16;
	
	private int drawId;
	private int indicesCount;
	
	int numVoxels;
	Voxel[][][] world;
	
	int dx, dz;
	int dxc, dzc;
	
	public Chunk(int xPos, int zPos) {
		dx = xPos * CHUNK_WIDTH;
		dz = zPos * CHUNK_DEPTH;
		dxc = dx * World.VOXEL_SIZE;
		dzc = dz * World.VOXEL_SIZE;
	}
	
	public void Build(int[][] map, VBORender graphics) {
		world = new Voxel[CHUNK_WIDTH][CHUNK_DEPTH][CHUNK_HEIGHT];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				int zoneHeight = map[i + dx][j + dz];
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					if (zoneHeight == 0) {
						world[i][j][h] = new WaterVoxel(World.VOXEL_SIZE);
					} else if (zoneHeight <= .25*20 + 1) {
						world[i][j][h] = new SandVoxel(World.VOXEL_SIZE);
					} else if (zoneHeight >= .75*20 - 1) {
						world[i][j][h] = new MountainVoxel(World.VOXEL_SIZE);
					} else {world[i][j][h] = new Voxel(World.VOXEL_SIZE);}
					world[i][j][h].translate(new Vector3(i*World.VOXEL_SIZE + dxc, h*World.VOXEL_SIZE, j*World.VOXEL_SIZE + dzc));
					world[i][j][h].hidden = true;
					numVoxels ++;
					if (h == zoneHeight || (zoneHeight == 0 && h <= .25*20) || (h < zoneHeight && map[dx+Math.abs(i-1)%map.length][dz+j] < h)
							|| (h <= zoneHeight && map[dx+(i+1)%map.length][dz+j] < h)
							|| (h <= zoneHeight && map[dx+i][Math.abs(dz+j-1)%map.length] < h)
							|| (h <= zoneHeight && map[dx+i][(dz+j+1)%map.length] < h)) {
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