package world;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.system.linux.SysIOctl;

import generation.GenTest2;
import main.VBORender;
import util.ArrayHelper;
import util.Vector3;

public class World {

	int width = 5;
	int height = 5;

	public static float VOXEL_SIZE = 1f;

	Random rGen;

	RegionManager[][] regions;

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
		width = map.length-1; height = map.length-1;
		System.out.println("# chunks: " + width + ", " + height);
		System.out.println("MAPSIZE: " + map.length);
		int[][] world = new int[map.length][map[0].length];
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				world[i][j] = Math.round(map[i][j] * Chunk.CHUNK_HEIGHT);
			}
		}

		regions = new RegionManager[width][height];


		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regions[i][j] = new RegionManager(i, j);
				regions[i][j].Build(world, gen.getBiomes(), graphics);
			}
		}
	}


	public void render() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				regions[i][j].draw(graphics);
			}
		}	
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
		int xV = (int)(position.x/VOXEL_SIZE); int zV = (int)(position.z/VOXEL_SIZE);	
		int xR = xV/(RegionManager.NUM_CHUNKS)/Chunk.CHUNK_WIDTH; int zR = zV/(RegionManager.NUM_CHUNKS)/Chunk.CHUNK_DEPTH;	
		
		int xInR = xV%(RegionManager.NUM_CHUNKS*Chunk.CHUNK_WIDTH); int zInR = zV%(RegionManager.NUM_CHUNKS*Chunk.CHUNK_DEPTH);
	

		
		if (xR < width && zR < height && xR >= 0 && zR >= 0 &&
				xInR >= 0 && zInR >= 0) {	
			System.out.println(xInR + ", " + zInR);

			int[][] m = regions[xR][zR].getHeight(xInR, zInR);
			int xVinC = xInR%Chunk.CHUNK_WIDTH;
			int zVinC = zInR%Chunk.CHUNK_DEPTH;
						
			return m[xVinC][zVinC]*VOXEL_SIZE + VOXEL_SIZE/2;
		}

		return Math.round(gen.getWaterThresh()*Chunk.CHUNK_HEIGHT*VOXEL_SIZE + VOXEL_SIZE/2);
	}
}
