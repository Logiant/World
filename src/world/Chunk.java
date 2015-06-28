package world;

import main.VBORender;
import util.ArrayHelper;
import util.Vector3;
import world.voxels.Voxel;
import generation.biomes.BIOME;
import generation.biomes.BiomeToVoxel;

public class Chunk {
	public static final int CHUNK_WIDTH = 2;
	public static final int CHUNK_DEPTH = 2;
	public static final int CHUNK_HEIGHT = 16;

	private int drawId;
	private int indicesCount;

	int numVoxels;
	Voxel[][][] world;
	int[][] height;
	
	boolean drawable = false;

	int dx, dz;
	float xOff, zOff;
	boolean wasLoaded = false;

	public Chunk(int xPos, int zPos, float xOff, float zOff) {
		dx = xPos * CHUNK_WIDTH;
		dz = zPos * CHUNK_DEPTH;
		this.xOff = xOff; this.zOff = zOff;
	}

	public int[][] getMap() {
		return height;
	}


	public void Build(int[][] map, BIOME[][] biomes, VBORender graphics) {
		int[] maxPos = {0, 0};
		int maxHeight = 0;
		height = new int[CHUNK_WIDTH][CHUNK_DEPTH];

		world = new Voxel[CHUNK_WIDTH][CHUNK_DEPTH][CHUNK_HEIGHT];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				int zoneHeight = map[i][j];
				if (zoneHeight > maxHeight) {
					maxHeight = zoneHeight;
					maxPos[0] = i; maxPos[1] = j;
				}
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					world[i][j][h] = BiomeToVoxel.getVoxel(biomes[i][j], World.VOXEL_SIZE);

					world[i][j][h].translate(new Vector3(i*World.VOXEL_SIZE + xOff, h*World.VOXEL_SIZE, j*World.VOXEL_SIZE + zOff));
					world[i][j][h].setVisible(false);
					numVoxels ++;
					if (h == zoneHeight || (h == (CHUNK_HEIGHT-1) && h < zoneHeight) || h < zoneHeight && (i==0||j==0||i==CHUNK_WIDTH-1||j==CHUNK_DEPTH-1)) {
						world[i][j][h].setVisible(true);
						height[i][j] = h;
					}
				}
			}
		}
	}

	public void draw(VBORender graphics) {
		if (drawable)
			graphics.render(drawId, indicesCount);
	}


	public float[] getVerts() {
		float[] verts = new float[0];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_DEPTH; j++) {
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					if (world[i][j][h].isVisible()) {
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
					if (world[i][j][h].isVisible()) {
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
					if (world[i][j][h].isVisible()) {
						if (!wasLoaded) {
							world[i][j][h].indexOffset(offset);
						}
						inds = ArrayHelper.CopyArray(inds, world[i][j][h].indices);
						offset += 8;
					}
				}
			}
		}
		wasLoaded = true;
		return inds;
	}

	public void createVAO(VBORender graphics) {
		drawable = true;
		drawId = graphics.createVBO(getVerts(), getColors(), getIndices());
		indicesCount = getIndices().length;		
	}

	public void destroyVAO(VBORender g) {
		drawable = false;
		g.deleteVAO(drawId);
	}

}