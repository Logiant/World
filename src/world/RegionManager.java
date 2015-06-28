package world;

import generation.biomes.BIOME;
import main.VBORender;

public class RegionManager {


	public static int NUM_CHUNKS = 1;
	int xi, yi;

	Chunk[][] chunks;



	public RegionManager(int x, int y) {
		chunks = new Chunk[NUM_CHUNKS][NUM_CHUNKS];
		xi = x; yi = y;
	}



	public void Build(int[][] world, BIOME[][] biomes, VBORender graphics) {
		int h = world[xi][yi];
		BIOME b = biomes[xi][yi];

		int[][] map = new int[Chunk.CHUNK_WIDTH][Chunk.CHUNK_DEPTH];
		BIOME[][] biome = new BIOME[Chunk.CHUNK_WIDTH][Chunk.CHUNK_DEPTH];

		for (int i = 0; i < Chunk.CHUNK_WIDTH; i++) {
			for (int j = 0; j < Chunk.CHUNK_DEPTH; j++) {	
				map[i][j] = h;
				biome[i][j] = b;
			}
		}

		for (int i = 0; i < NUM_CHUNKS; i++) {
			for (int j = 0; j < NUM_CHUNKS; j++) {
				chunks[i][j] = new Chunk(i, j,
						i*Chunk.CHUNK_WIDTH*World.VOXEL_SIZE + xi*NUM_CHUNKS*Chunk.CHUNK_WIDTH*World.VOXEL_SIZE,
						j*Chunk.CHUNK_DEPTH*World.VOXEL_SIZE + yi*NUM_CHUNKS*Chunk.CHUNK_DEPTH*World.VOXEL_SIZE);
				chunks[i][j].Build(map, biome, graphics);
			}
		}

	}



	public void draw(VBORender g) {
		for (int i = 0; i < NUM_CHUNKS; i++) {
			for (int j = 0; j < NUM_CHUNKS; j++) {
				chunks[i][j].draw(g);
			}
		}
	}



	public int[][] getHeight(int xV, int zV) {
		int xC = xV / Chunk.CHUNK_WIDTH; int zC = zV / Chunk.CHUNK_WIDTH;

		
		return chunks[xC][zC].height;
	}
}
