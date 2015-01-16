package lwjgl3Test;

import generation.World;

public class Game {

	long window;
	VBORender graphics;
	Camera cam;
	World world;
	
	public void initialize(long window) {
		cam = new Camera(window);
		world = new World();
		this.window = window;

		graphics = new VBORender();
		graphics.initialize();
		
        world.Build(graphics);
	}
	
	public void update() {
		cam.update();
		
		graphics.update(cam.getView());
		render();
	}
	
	public void render() {
		world.render();
	}
}
