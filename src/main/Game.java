package main;

import entities.player.Player;
import util.Quaternion;
import util.Vector3;
import world.World;

public class Game {

	long window;
	VBORender graphics;
	Camera cam;
	World world;

	Player p;

	public void initialize(long window) {
		this.window = window;

		p = new Player(window, 15);

		cam = new Camera(p.transform);
		world = new World();

		graphics = new VBORender();
		graphics.initialize();

		world.Build(graphics);
		p.setPosition(world.getLand());
		p.initialize(graphics);
	}

	public void update() {
		System.out.println();
		System.out.println("New Update");
		p.update();
		physics();
		cam.update();


		graphics.transform(new Vector3(), new Quaternion()); //undo all transformation
		graphics.update(cam.getView());
		render();
	}


	private void physics() {
		p.simpleGravity(world);
	}


	private void render() {
		world.render();
		graphics.transform(p.transform.position, p.transform.getQuaternion());
		p.draw();

	}
}
