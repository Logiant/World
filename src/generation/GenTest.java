package generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GenTest {

	long seed = System.nanoTime();//'L' + 'O' + 'G' + 'A' + 'N' + '!';
	public World world;
	
	BiomeDev biomes;

	int size = 256;

	int height = 65;

	float[][] filled;
	float[][] voronoi;

	//convert the 2d heightmap to an image
	public GenTest() {

		seed = System.nanoTime();
		
		String directory = System.getProperty("user.dir") + "\\Images";
		System.out.println("Directory: " + directory);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		BufferedImage colored = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		BufferedImage filledColored = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		BufferedImage binary = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		BufferedImage plates = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);



		int a = 255;

		float[][] map = PerlinMap();
		float[][] mask = new float[size][size];
		voronoi = new float[size][size];
		System.out.println("Generating Voronoi");
		float[][] mtns = HeightMap.Voronoi(voronoi, 10);
		for (int i = 0; i < 15; i++) {
			mtns = HeightMap.Smooth(mtns, 0.75f);
		}
		mtns = HeightMap.Smooth(mtns, 1);

		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {
				
				int c = 0;
				int r = c;
				int g = c;
				int b = c;
				int color = 0;
				
				
				c = Math.round((map[i][j]) * 255f);

				r = c;
				g = c;
				b = c;
				color = (a << 24) | (r << 16) | (g << 8) | b;
				image.setRGB(i, j, color);
				
				
				
				if (map[i][j] == 0) {
					color = (a << 24) | (61 << 16) | (89 << 8) | 171; //water
				} else if (map[i][j] > 0.2) {
					color = (a << 24) | (255 << 16) | (255 << 8) | 255; //mountains
					mask[i][j] = 1;
				} else if (map[i][j] < 0.02) {
					color = (a << 24) | (240 << 16) | (230 << 8) | 140; //beach
					mask[i][j] = 1;
				} else {
					color = (a << 24) | (110 << 16) | (139 << 8) | 61; //land
					mask[i][j] = 1;
				}
				
				
				colored.setRGB(i, j, color);
				
				
				
				
				c = Math.round((mtns[i][j]) * 255f);

				r = c;
				g = c;
				b = c;
				color = (a << 24) | (r << 16) | (g << 8) | b;
				
				plates.setRGB(i, j, color);


				//step through the island and check for connectivity
					//remove outliers
					//force steep drops to become beaches ?
				//check that island isn't too small
				//step through ocean and verify connectivity
					//tag disconnected water as lakes
				//simulate rainwater
					//create lakes and rivers - erosion?
					//caclulate wetness
				//calculate temperature
				//create biomes
				//smooth biomes

			}
		}
		System.out.println("Floodfilling");
		
		float[][] floodFill = new float[size][size];
		HeightMap.FloodFill(mask, size/2, size/2, floodFill);

		filled = new float[size][size];
		
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {
								
				filled[i][j] = Math.min(1.75f*(map[i][j] * floodFill[i][j]), 0.6f) ;//+ (mtns[i][j] * floodFill[i][j]), 1);
			//	filled[i][j] += (mtns[i][j]*floodFill[i][j]);

				
				
				
				

				//step through the island and check for connectivity
					//remove outliers
					//force steep drops to become beaches ?
				//check that island isn't too small
				//step through ocean and verify connectivity
					//tag disconnected water as lakes
				//simulate rainwater
					//create lakes and rivers - erosion?
					//caclulate wetness
				//calculate temperature
				//create biomes
				//smooth biomes

			}
		}
		//TODO redevelop erosion algorithm
		//filled = HeightMap.Erode(filled, 1.5f);
		
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {
				
				filled[i][j] *= floodFill[i][j];
				
				filled[i][j] = Math.min(1, filled[i][j]);
				
				int c = Math.round((mask[i][j]) * 255f);

				int r = c;
				int g = c;
				int b = c;
				int color = (a << 24) | (r << 16) | (g << 8) | b;
				binary.setRGB(i, j, color);
				
				
				
				if (filled[i][j] == 0) {
					color = (a << 24) | (61 << 16) | (89 << 8) | 171; //water
				} else if (filled[i][j] > 0.6) {
					color = (a << 24) | (255 << 16) | (255 << 8) | 255; //mountains
				//	filled[i][j] = 0.65f;
				} else if (filled[i][j] < 0.1) {
					color = (a << 24) | (240 << 16) | (230 << 8) | 140; //beach
				//	filled[i][j] = 0.25f;
				} else {
					color = (a << 24) | (110 << 16) | (139 << 8) | 61; //land
				//	filled[i][j] = 0.5f;
				}
				filledColored.setRGB(i, j, color);
			}
		}
		
		System.out.println("Writing Images");
		
		try {
			ImageIO.write(image, "png", new File(directory, "WorldGen.png"));
			ImageIO.write(colored, "png", new File(directory, "Colors.png"));
			ImageIO.write(filledColored, "png", new File(directory, "Filled.png"));
			ImageIO.write(binary, "png", new File(directory, "Binary.png"));
			ImageIO.write(plates, "png", new File(directory, "Voronoi.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		biomes = new BiomeDev(seed);
		biomes.CreateBiomes(filled, directory);

	}
	
	
	private float[][] PerlinMap() {
		float[][] map = new float[size][size];
		int octavex = size/6;
		int octavey = size/6;
		float persistance = 1.0f;


		//	float[][] mapds = HeightMap.DiamondSquare(size, 1);
		float[][] map1 = HeightMap.PerlinNoise(size, size, octavex, octavey, seed, persistance);
		float[][] map2 = HeightMap.PerlinNoise(size, size, octavex/2, octavey/2, seed, persistance/2);
		float[][] map3 = HeightMap.PerlinNoise(size, size, octavex/4, octavey/4, seed, persistance/4);
		float[][] map4 = HeightMap.PerlinNoise(size, size, octavex/8, octavey/8, seed, persistance/8);


		float[][] radGrad = HeightMap.RadGrad(size);
		
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				float value = map1[i][j];

				value += map2[i][j] + map3[i][j] + map4[i][j];
				
				value /= 3;
				
				value = Math.max(Math.min(value - (1 - radGrad[i][j]), 1), 0);
				
				map[i][j] = value;
				
			}
		}
		return map;
	}



	public static void main(String[] args) {
		new GenTest();
	}

	public float[][] getMap() {
		return filled;
	}
	
}
