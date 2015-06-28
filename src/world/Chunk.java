package world;

import main.VBORender;
import util.ArrayHelper;
import util.Vector3;
import world.voxels.Voxel;
import world.voxels.WoodVoxel;
import generation.biomes.BIOME;
import generation.biomes.BiomeToVoxel;

public class Chunk {
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;
	public static final int CHUNK_HEIGHT = 16;

	private int drawId;
	private int indicesCount;

	int numVoxels;
	Voxel[][][] world;
	int[][] height;

	int dx, dz;
	int dxc, dzc;

	public Chunk(int xPos, int zPos) {
		dx = xPos * CHUNK_WIDTH;
		dz = zPos * CHUNK_DEPTH;
		dxc = (int)(dx * World.VOXEL_SIZE);
		dzc = (int)(dz * World.VOXEL_SIZE);
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
				int zoneHeight = map[i + dx][j + dz];
				if (zoneHeight > maxHeight) {
					maxHeight = zoneHeight;
					maxPos[0] = i; maxPos[1] = j;
				}
				for (int h = 0; h < CHUNK_HEIGHT; h++) {
					world[i][j][h] = BiomeToVoxel.getVoxel(biomes[i+dx][j+dz], World.VOXEL_SIZE);
					
					world[i][j][h].translate(new Vector3(i*World.VOXEL_SIZE + dxc, h*World.VOXEL_SIZE, j*World.VOXEL_SIZE + dzc));
					world[i][j][h].setVisible(false);
					numVoxels ++;
					if (h == zoneHeight || (h == (CHUNK_HEIGHT-1) && h < zoneHeight) 
							|| (h < zoneHeight && map[dx+Math.abs(i-1)%map.length][dz+j] < h)
							|| (h <= zoneHeight && map[dx+(i+1)%map.length][dz+j] < h)
							|| (h <= zoneHeight && map[dx+i][Math.abs(dz+j-1)%map.length] < h)
							|| (h <= zoneHeight && map[dx+i][(dz+j+1)%map.length] < h)) {
						world[i][j][h].setVisible(true);
						height[i][j] = h;
					}
				}
			}
		}
//		int xMax = Math.max(6, Math.min(maxPos[0], 12)); int zMax = Math.max(6, Math.min(maxPos[1], 12)); int yMax = 1+map[xMax + dx][zMax + dz];
		if (dx == 128 && dz == 128) {
	//		buildHouse(xMax, yMax, zMax, world);

		}
		drawId = graphics.createVBO(getVerts(), getColors(), getIndices());
		indicesCount = getIndices().length;
	}

	public void draw(VBORender graphics) {
		graphics.render(drawId, indicesCount);
	}


	void buildHouse(int x0, int y0, int z0, Voxel[][][] world) {
		int[] xVerts = {x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3,
				x0, x0+3, x0, x0+3, x0, x0+3, x0, x0+3,
				x0, x0+3, x0, x0+3, x0, x0+3, x0, x0+3,
				x0, x0+3, x0, x0+3, x0, x0+3, x0, x0+3,
				x0, x0+3, x0, x0+3, x0, x0+3, x0, x0+3,
				x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3, x0, x0+1, x0+2, x0+3};
		
		int[] yVerts = {y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0, y0,
						y0+1, y0+1, y0+1, y0+1, y0+1, y0+1, y0+1, y0+1,
						y0+2, y0+2, y0+2, y0+2, y0+2, y0+2, y0+2, y0+2,
						y0+3, y0+3, y0+3, y0+3, y0+3, y0+3, y0+3, y0+3, 
						y0+4, y0+4, y0+4, y0+4, y0+4, y0+4, y0+4, y0+4,
						y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5, y0+5,};
		
		int[] zVerts = {z0, z0, z0, z0, z0+1, z0+1, z0+1, z0+1, z0+2, z0+2, z0+2, z0+2, z0+3, z0+3, z0+3, z0+3,
				z0, z0, z0+1, z0+1, z0+2, z0+2, z0+3, z0+3,
				z0, z0, z0+1, z0+1, z0+2, z0+2, z0+3, z0+3,
				z0, z0, z0+1, z0+1, z0+2, z0+2, z0+3, z0+3,
				z0, z0, z0+1, z0+1, z0+2, z0+2, z0+3, z0+3,
				z0, z0, z0, z0, z0+1, z0+1, z0+1, z0+1, z0+2, z0+2, z0+2, z0+2, z0+3, z0+3, z0+3, z0+3};
		
		int size = xVerts.length;
		

		for (int i = 0; i < size; i++) {
			int xi = xVerts[i]%CHUNK_WIDTH;
			int yi = yVerts[i]%CHUNK_HEIGHT;
			int zi = zVerts[i]%CHUNK_DEPTH;
			int x = xVerts[i];
			int y = yVerts[i];
			int z = zVerts[i];
			System.out.println(x + ", " + y + " " + z);
			world[xi][yi][zi] = new WoodVoxel(World.VOXEL_SIZE);
			world[xi][yi][zi].translate(new Vector3(x*World.VOXEL_SIZE + dxc, y*World.VOXEL_SIZE, z*World.VOXEL_SIZE + dzc));
			world[xi][yi][zi].setVisible(true);
		}


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