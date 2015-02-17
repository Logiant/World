package generation;

import lwjgl3Test.VBORender;
import util.ArrayHelper;
import world.Chunk;

public class World {

	int width = 5;
	int height = 5;
	
	long seed = 5;

	public static int VOXEL_SIZE = 2;
	
	Chunk[][] chunk;
	float[][] map;
	GenTest gen;
	VBORender graphics;
	
	public void Build(VBORender graphics) {
		this.graphics = graphics;
		gen = new GenTest();
		map = gen.getMap();
		width = map.length / Chunk.CHUNK_WIDTH; height = map.length / Chunk.CHUNK_DEPTH;
		int[][] world = new int[map.length][map[0].length];
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				world[i][j] = Math.round(map[i][j] * 20);
			}
		}

		chunk = new Chunk[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				chunk[i][j] = new Chunk(i, j);
				chunk[i][j].Build(world, graphics);
			}
		}
	}
	
	
	public void render() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				chunk[i][j].draw(graphics);
			}
		}	}
	
	public float[] getVerts() {
		float[] verts = new float[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				verts = ArrayHelper.CopyArray(verts, chunk[i][j].getVerts());
			}
		}
		return verts;
	}
	
	public float[] getColors() {
		float[] colors = new float[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				colors = ArrayHelper.CopyArray(colors, chunk[i][j].getColors());
			}
		}
		return colors;
	}

	public int[] getIndices() {
		int[] inds = new int[0];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				inds = ArrayHelper.CopyArray(inds, chunk[i][j].getIndices());
			}
		}
		return inds;
	}
}
