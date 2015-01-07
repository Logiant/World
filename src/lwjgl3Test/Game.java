package lwjgl3Test;

public class Game {

	long window;
	VBORender triangle;
	Camera cam;
	
	public void initialize(long window) {
		cam = new Camera(window);
		this.window = window;
		float[] tri = new float[]
                {
                0.0f,  1f,  0.0f, 1f,
                0.5f, -0.5f,  0.0f, 1f,
                -0.5f, -0.5f,  0.0f, 1f}; //x y z w
		float[] color = new float[] {
		1, 1, 1, 1,
		1, 1, 1, 1,
		1, 1, 1, 1 }; //r g b a
		
		int[] indices = new int[] {0, 1, 2};
        triangle = new VBORender();
        triangle.initialize(tri, color, indices);
        
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
