package lwjgl3Test;

import generation.Voxel;
import generation.World;

public class Game {

	long window;
	VBORender triangle;
	Camera cam;
	World world;
	
	public void initialize(long window) {
		cam = new Camera(window);
		world = new World();
		this.window = window;
	/*	float[] tri = new float[]
                {
                0.0f,  1f,  0.0f, 1f,
                0.5f, -0.5f,  0.0f, 1f,
                -0.5f, -0.5f,  0.0f, 1f}; //x y z w
		float[] color = new float[] {
		1, 1, 1, 1,
		1, 1, 1, 1,
		1, 1, 1, 1 }; //r g b a
		
		int[] indices = new int[] {0, 1, 2};
     */
        world.Build();
		triangle = new VBORender();
        triangle.initialize(world.getVerts(), world.getColors(), world.getIndices());
	}
	
	public void update() {
		cam.update();
		
		triangle.update(cam.getView());
		render();
	}
	
	public void render() {
		triangle.render();
	}
}
