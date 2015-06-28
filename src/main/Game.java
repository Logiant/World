package main;

import entities.Transform;
import entities.player.Player;
import util.Quaternion;
import util.Time;
import util.Vector3;
import world.World;

public class Game {

	long window;
	VBORender graphics;
	Camera cam;
	World world;

	Player p;

	float timeOfDay;
	int dayLength = 10; //seconds
	
	int sunID;
	
	public void initialize(long window) {
		this.window = window;

		p = new Player(window, 15);

		cam = new Camera(p.transform);
		world = new World();

		graphics = new VBORender();
		graphics.initialize();

		world.Build(graphics);
	//	p.setPosition(world.getLand());
		p.initialize(graphics);
		
		
		float[] verts = {0, 0, 0, 1};
		float[] cols = {1, 1, 1, 1};
		int[] inds = {0};
		
		sunID = graphics.createVBO(verts, cols, inds);
		
	}

	public void update() {
		
		
		timeOfDay = (timeOfDay + Time.dt/1000f) % dayLength;
		
		float t = (float)(timeOfDay/dayLength * 2 * Math.PI);
		
		p.update();
		physics();
		cam.update();

		graphics.setSun(new Vector3((float)Math.cos(t), (float)Math.sin(t), 0));

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
		graphics.update(cam.genView(new Transform()));
		graphics.renderSun(sunID, 1);
	}
}
