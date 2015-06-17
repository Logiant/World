package generation;

import generation.biomes.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class BiomeDev {

	Random rGen;
		
	WindGen windGen;
	WaterGen waterGen;
	

	public BiomeDev(long seed) {
		rGen = new Random(seed);
		windGen = new WindGen(seed);
		waterGen = new WaterGen(seed);
	}

	public void CreateBiomes(float[][] heightmap, String directory) {
		int size = heightmap.length;
		//wind map
		//rainfall map
		//river map
		//temperature map
		//moisture map

		//temp + moisture = biome

		float[][] windMap = windGen.generateWind(heightmap);
		float[][] waterMap = waterGen.generateRivers(heightmap);

		BufferedImage wind = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		BufferedImage water = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		int a = 255;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {

				int c = 0;
				int r = c;
				int g = c;
				int b = c;
				int color = 0;


				c = Math.round((windMap[i][j]) * 255f);
				r = c;
				g = c;
				b = c;
				color = (a << 24) | (r << 16) | (g << 8) | b;
				wind.setRGB(i, j, color);
				
				c = Math.round((waterMap[i][j]) * 255f);
				r = c;
				g = c;
				b = c;
				color = (a << 24) | (r << 16) | (g << 8) | b;
				
				water.setRGB(i, j, color);

			}
		}


		try {
			ImageIO.write(wind, "png", new File(directory, "WindMap.png"));
			ImageIO.write(water, "png", new File(directory, "WaterMap.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}



	}

	
}
