package generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GenTest {

	long seed = 5;
	public World world;
	
	int size = 257;
	
	int height = 65;
	
	int[][] map;
	
	//convert the 2d heightmap to an image
	public GenTest() {
	
		String directory = System.getProperty("user.dir") + "\\Images";
		System.out.println("Directory: " + directory);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		int a = 255;
		
		float[][] mapds = HeightMap.DiamondSquare(size, 1);
		float[][] mapp = HeightMap.PerlinNoise(size, size, 30, 44, seed);


		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j ++) {
				
				int r = (int)((float)mapp[i][j] * 255f);
				int g = (int)((float)mapp[i][j] * 255f);
				int b = (int)((float)mapp[i][j] * 255f);
						
				int color = (a << 24) | (r << 16) | (g << 8) | b;
				image.setRGB(i, j, color);
			}
		}

		try {
			ImageIO.write(image, "png", new File(directory, "WorldGen.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		new GenTest();
	}
	
}
