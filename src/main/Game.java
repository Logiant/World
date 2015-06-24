package main;

import entities.player.Player;
import world.World;

public class Game {

	long window;
	VBORender graphics;
	Camera cam;
	World world;
	
	Player p;

	public void initialize(long window) {
		this.window = window;

		p = new Player(window, 10);

		cam = new Camera(p.transform);
		world = new World();

		graphics = new VBORender();
		graphics.initialize();

		world.Build(graphics);
	//	p.setPosition(world.getLand());
		p.initialize(graphics);
	}

	public void update() {
		p.update();
		cam.update();

		graphics.update(cam.getView());
		render();
	}

	public void render() {
		world.render();
		p.draw();
	}
}
