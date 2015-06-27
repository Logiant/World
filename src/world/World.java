package world;

import java.io.IOException;
import java.util.Random;

import generation.GenTest2;
import main.VBORender;
import util.ArrayHelper;
import util.Vector3;

public class World {

	int width = 5;
	int height = 5;

	public static float VOXEL_SIZE = 1f;

	Random rGen;

	Chunk[][] chunk;
	float[][] map;
	GenTest2 gen;
	VBORender graphics;

	public void Build(VBORender graphics) {

		this.graphics = graphics;
		try {
			gen = new GenTest2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map = gen.getMap();
		rGen = new Random(gen.getSeed());
		width = map.length / Chunk.CHUNK_WIDTH; height = map.length / Chunk.CHUNK_DEPTH;
		System.out.println("# chunks: " + width + ", " + height);
		System.out.println("MAPSIZE: " + map.length);
		int[][] world = new int[map.length][map[0].length];
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				world[i][j] = Math.round(map[i][j] * Chunk.CHUNK_HEIGHT);
			}
		}

		chunk = new Chunk[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				chunk[i][j] = new Chunk(i, j);
				chunk[i][j].Build(world, gen.getBiomes(), graphics);
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


	public Vector3 getLand() {
		int x = rGen.nextInt(map.length); int z = rGen.nextInt(map.length);
		float y = map[x][z];

		while (y <= gen.getWaterThresh()) {
			x = rGen.nextInt(map.length); z = rGen.nextInt(map.length);
			y = map[x][z];
		}

		return new Vector3(VOXEL_SIZE*x, VOXEL_SIZE*y*Chunk.CHUNK_HEIGHT + 2*VOXEL_SIZE,VOXEL_SIZE*z);
	}


	public float sampleHeight(Vector3 position) {
		int xi = (int)(position.x/VOXEL_SIZE); int zi = (int)(position.z/VOXEL_SIZE);	
		int xc = xi/Chunk.CHUNK_WIDTH; int zc = zi/Chunk.CHUNK_DEPTH;	

				
		if (xc<width && zc<height && xi >= 0 && zi >= 0) {		
			int[][] m = chunk[xc][zc].getMap();
			xc = xi%Chunk.CHUNK_WIDTH;
			zc = zi%Chunk.CHUNK_DEPTH;			
			return m[xc][zc]*VOXEL_SIZE + VOXEL_SIZE/2;
		}

		return Math.round(gen.getWaterThresh()*Chunk.CHUNK_HEIGHT*VOXEL_SIZE);
	}
}
