package lwjgl3Test;

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
