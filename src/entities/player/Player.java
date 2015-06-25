package entities.player;


import main.VBORender;
import util.Matrix4;
import util.Time;
import util.Vector3;
import world.World;
import entities.Transform;


public class Player {

	public Transform transform;

	long window;

	float gravity = -0.01f;

	VBORender graphics;
	int drawId; int vertId; int indicesCount;
	Matrix4 viewMatrix;


	float[] verts; float[] colors; int[] indices;
	float size = 2;
	float speed;


	public Player(long window, float speed) {
		this.window = window; this.speed = speed;
		transform = new Transform();
		verts = new float[] {//X, 	Y, 		Z,		 W
				-size/2, -size/2,  -size/2,	 1, //near LL - 0
				size/2,  -size/2,  -size/2,  1, //near LR - 1
				-size/2,  size/2,  -size/2,	 1, //near UL - 2
				size/2,   size/2,  -size/2,	 1, //near UR - 3
				-size/2, -size/2,	size/2,	 1, //far LL  - 4
				size/2,	 -size/2,	size/2,	 1, // far LR - 5
				-size/2,  size/2,	size/2,	 1, // far UL - 6
				size/2,	  size/2,	size/2,	 1}; //far UR - 7
		colors = new float[] {//R, 		  G, 	   B,	   A
				255/255f,	255/255f, 255/255f,1, //near LL
				255/255f,	255/255f, 255/255f,1, //near LR
				255/255f,	255/255f, 255/255f,1, //near UL
				255/255f,	255/255f, 255/255f,1, //near UR
				255/255f,	255/255f, 255/255f,1, //far LL
				255/255f,	255/255f, 255/255f,1, //far LR
				255/255f,	255/255f, 255/255f,1, //far UL
				255/255f,	255/255f, 255/255f,1,};//far UR

		indices = new int[] {
				1, 0, 2, //near lower
				1, 2, 3, //near upper
				3, 2, 6, //top near
				3, 6, 7, //top far
				7, 6, 4, //far upper
				4, 5, 7, //far lower
				5, 4, 1, //bottom far
				0, 1, 4, //bottom near
				4, 6, 0, //left far
				2, 0, 6, //left near
				5, 3, 7, //right far
				1, 3, 5};//right near
		viewMatrix = Matrix4.Identity();
	}


	public void initialize(VBORender graphics) {
		this.graphics = graphics;
		int[] drawData = graphics.createVBO(verts, colors, indices);
		drawId = drawData[0];
		vertId = drawData[1];
		indicesCount = indices.length;
	}

	public void setPosition(Vector3 pos) {
		transform.position = pos;
	}


	public void simpleGravity(World world) {
		Vector3 pos = new Vector3(transform.position);

		float h = world.sampleHeight(pos) + World.VOXEL_SIZE/2f;
		pos.x -= size/2; pos.z -= size/2;

		h = Math.max(world.sampleHeight(pos) + World.VOXEL_SIZE/2f, h);
		pos.x += size;

		h = Math.max(world.sampleHeight(pos) + World.VOXEL_SIZE/2f, h);
		pos.z += size;

		h = Math.max(world.sampleHeight(pos) + World.VOXEL_SIZE/2f, h);
		pos.x -= size;

		h = Math.max(world.sampleHeight(pos) + World.VOXEL_SIZE/2f, h);


		float dy = -(transform.position.y - h - size/2f);



		if (dy < 0) { //falling
			dy = Math.max(dy, gravity*Time.dt); //select smallest magnitude
			transform.position.y += dy;
		} else{// if (dy <= World.VOXEL_SIZE) {
			dy = Math.min(dy, Time.dt*(0.01f)); //select smallest magnitude
			transform.position.y += dy;
		}
		
		

	}



	public void draw() {		
		graphics.transform(transform.position, transform.getQuaternion());
		graphics.render(drawId, indicesCount);
	}



	public void update() {
		transform.update(speed, window);
	}


}
