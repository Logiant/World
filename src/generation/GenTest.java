package generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GenTest {

	long seed = 5;
	public World world;
	
	int width = 1024;
	int depth = 1024;
	
	int height = 125;
	
	int[][] map;
	
	//convert the 2d heightmap to an image
	public GenTest() {
	
		String directory = System.getProperty("user.dir") + "\\Images";
		System.out.println("Directory: " + directory);
		BufferedImage image = new BufferedImage(width, depth, BufferedImage.TYPE_INT_RGB);

		int a = 255;
		
		map = HeightMap.GeneratePlains(width, depth, 15, 32, seed, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < depth; j ++) {
				
				int r = (int)(map[i][j] * 255f / height);
				int g = (int)(map[i][j] * 255f / height);
				int b = (int)(map[i][j] * 255f / height);
						
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
