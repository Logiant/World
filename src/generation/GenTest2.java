package generation;

import generation.biomes.BIOME;
import generation.civ.CityGen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GenTest2 {

	long seed = System.nanoTime();

	BufferedImage heightImage;
	BufferedImage binaryImage;
	BufferedImage sortedImage;
	BufferedImage terrainImage;

	BufferedImage waterImage;
	BufferedImage moistureImage;
	BufferedImage biomeImage;
	
	BufferedImage cityImage;

	String directory;

	int size = 512; //bufferedimage size ~512 is recommended

	Heightmap heightGen;
	ColorGen colorGen;
	WaterGen waterGen;
	BiomeGen biomeGen;
	CityGen cityGen;

	
	float[][] heightmap;
	BIOME[][] biomes;
	float[][] cities;
	
	public float[][] getMap() {
		return heightmap;
	}
	
	public BIOME[][] getBiomes() {
		return biomes;
	}
	
	
	public float getMountainThresh() {
		return heightGen.mtnThresh;
	}
	
	public float getWaterThresh() {
		return heightGen.waterThresh;
	}
	
	
	public GenTest2() throws IOException {
		setupVars();

		//generate desired maps
		System.out.println("Generating heightmap...");
		long startTime = System.nanoTime();
		heightmap = heightGen.generateHeightmap(size+1);
		float dt = Math.round((System.nanoTime() - startTime)/1000000000f * 10000)/10000;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Generating Binary Map...");
		float[][] binary = heightGen.binaryMap;
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Coloring Map...");
		float[][][] colored = colorGen.color(heightmap, binary);
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Flowing Rivers...");
		float[][] water = waterGen.generateWater(heightmap, binary, (heightGen.mtnThresh + heightGen.waterThresh)/2f);
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Generating Moisture...");
		float[][] moisture = waterGen.moistureMap(binary, water);
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Building biomes...");
		biomes = biomeGen.createBiomes(heightmap, binary, moisture, heightGen.waterThresh, heightGen.mtnThresh);
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		startTime = System.nanoTime();
		
		System.out.println("Picking cities...");
		cities = cityGen.buildCities(heightmap, moisture, binary);
		dt = Math.round((System.nanoTime() - startTime)/1000000000f * 100)/100;
		System.out.println("Complete after " + dt + " seconds\n");
		
		System.out.println("Generating image data...");
		
		
		//create images
		int a = 255; //transparency

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if (heightmap[i][j] < heightGen.waterThresh) {
					heightmap[i][j] = heightGen.waterThresh;
				}
				
				float val = heightmap[i][j]*binary[i][j];

				int r = (int)(val*255 + 0.5f);
				int g = (int)(val*255 + 0.5f);
				int b = (int)(val*255 + 0.5f);

				int color = (a << 24) | (r << 16) | (g << 8) | b;
				heightImage.setRGB(i, j, color);

				val = binary[i][j] * 255;
				int c = Math.round(val);
				color = (a << 24) | (c << 16) | (c << 8) | c;
				binaryImage.setRGB(i, j, color);

				r = (int)(colored[i][j][0]*255 + 0.5);
				g = (int)(colored[i][j][1]*255 + 0.5);
				b = (int)(colored[i][j][2]*255 + 0.5);
				color = (a << 24) | (r << 16) | (g << 8) | b;
				sortedImage.setRGB(i, j, color);




				val = binary[i][j] * heightmap[i][j];
				if (val > heightGen.mtnThresh) { //mountain color
					r = 225;
					g = 225;
					b = 225;
				} else if (val > heightGen.waterThresh) { //land color
					r = 0;
					g = 92;
					b = 9;
				} else { //water color
					r = 28;
					g = 107;
					b = 160;
				}

				color = (a << 24) | (r << 16) | (g << 8) | b;
				terrainImage.setRGB(i, j, color);

				if (water[i][j] > 0) {
					r = 22; g = 60; b = (int)(255*(water[i][j]+0.3f));
				}
				color = (a << 24) | (r << 16) | (g << 8) | b;

				waterImage.setRGB(i, j, color);

				
				if (cities[i][j] > 0.5) {
					r = 0; g = 0; b = 0;
				} else if (cities[i][j] > 0) {
					r = 128; g = 128; b = 128;
				}
				
				color = (a << 24) | (r << 16) | (g << 8) | b;
				cityImage.setRGB(i, j, color);

				r = 0; g = (int)(255*(moisture[i][j])); b = (int)(255*(moisture[i][j]));
				if (moisture[i][j] == 1) r = 255;
				color = (a << 24) | (r << 16) | (g << 8) | b;
				moistureImage.setRGB(i, j, color);

				switch(biomes[i][j]) {
				default:
				case OCEAN:
					r = 61; g = 89; b = 171;
					break;
				case RIVER:
					r = 79; g = 148; b = 205;
					break;
				case TROP_RAINFOREST:
					r = 34; g = 139; b = 34;
					break;
				case TROP_SEASONAL:
					r = 110; g = 139; b = 61;
					break;
				case GRASSLAND:
					r = 60; g = 179; b = 116;
					break;
				case DESERT:
					r = 205; g = 190; b = 112;
					break;
				case TEMP_RAINFOREST:
					r = 105; g = 139; b = 34;
					break;
				case TEMP_DECIDUOUS:
					r = 162; g = 205; b = 90;
					break;
				case TEMP_DESERT:
					r = 128; g = 128; b = 0;
					break;
				case TAIGIA:
					r = 193; g = 205; b = 205;
					break;
				case SHRUBLAND:
					r = 205; g = 205; b = 0;
					break;
				case SNOW:
					r = 240; g = 248; b = 255;
					break;
				case TUNDRA:
					r = 193; g = 250; b = 205;
					break;
				case BARE:
					r = 132; g = 132; b = 132;
					break;
				case SCORCHED:
					r = 139; g = 90; b = 0;
					break;
				}

				color = (a << 24) | (r << 16) | (g << 8) | b;
				biomeImage.setRGB(i, j, color);

			}
		}

		System.out.println("Writing .png Files...");
		
		ImageIO.write(heightImage, "png", new File(directory, "Heightmap.png"));
		ImageIO.write(binaryImage, "png", new File(directory, "Binary.png"));
		ImageIO.write(sortedImage, "png", new File(directory, "Sorted.png"));
		ImageIO.write(terrainImage, "png", new File(directory, "Terrain.png"));
		ImageIO.write(waterImage, "png", new File(directory, "WaterMap.png"));
		ImageIO.write(moistureImage, "png", new File(directory, "Moisture.png"));
		ImageIO.write(biomeImage, "png", new File(directory, "Biomes.png"));
		ImageIO.write(cityImage, "png", new File(directory, "Cities.png"));

		System.out.println("Generation Complete!");
	}


	void setupVars() {
		//seeds and generations
		heightGen = new Heightmap(seed);
		colorGen = new ColorGen(seed);
		waterGen = new WaterGen(seed);
		biomeGen = new BiomeGen(seed);
		cityGen = new CityGen(seed);

		//system directory and images
		directory = System.getProperty("user.dir") + "\\TestImages";

		heightImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		binaryImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		sortedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		terrainImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		waterImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		moistureImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		biomeImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		cityImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);


	}


	public static void main(String[] args) {
		try {
			new GenTest2();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getSeed() {
		return seed;
	}
}
