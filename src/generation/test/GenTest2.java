package generation.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GenTest2 {

	
	BufferedImage heightImage;
	String directory;
	
	int size = 2048; //bufferedimage size
	
	Heightmap heightGen;
		
	public GenTest2() throws IOException {
		setupVars();
		
		//generate desired maps
		float[][] heightmap = heightGen.generateHeightmap(size+1);
		
		//create images
		int a = 255; //transparency
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				float val = heightmap[i][j];
				
				int r = (int)(val*255 + 0.5f);
				int g = (int)(val*255 + 0.5f);
				int b = (int)(val*255 + 0.5f);
				
				int color = (a << 24) | (r << 16) | (g << 8) | b;

				heightImage.setRGB(i, j, color);
			}
		}
		
		ImageIO.write(heightImage, "png", new File(directory, "Heightmap.png"));
	}
	
	
	void setupVars() {
		//seeds and generations
		long seed = System.nanoTime();
		heightGen = new Heightmap(seed);
		
		//system directory and images
		directory = System.getProperty("user.dir") + "\\TestImages";
		heightImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	}
	
	
	public static void main(String[] args) {
		try {
			new GenTest2();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
