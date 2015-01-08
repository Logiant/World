package lwjgl3Test;

import input.Keyboard;

import org.lwjgl.glfw.GLFW;

import util.Matrix4;
import util.Time;
import util.Vector3;

public class Camera {
	
	public static float viewAngle = 45;
	public static float nearClip = 0.01f;
	public static float farClip = 500;
	
	private Vector3 position;
	private Vector3 rotation;
	
	private float speed = 10f;
	Vector3 linVel;
	Vector3 rotVel;
	
	private Matrix4 viewMatrix;
	
	private long window;
	
	public Camera(long window) {
		this.window = window;
		position = new Vector3(0, 0, 0);
		rotation = new Vector3(0, 180, 0);
	}
	
	public void update() {

		linVel = new Vector3();
		rotVel = new Vector3();
		
		if (Keyboard.keyDown(GLFW.GLFW_KEY_W, window)) {
			linVel.z += speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_S, window)) {
			linVel.z -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_A, window)) {
			linVel.x += speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_D, window)) {
			linVel.x -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_Z, window)) {
			linVel.y += speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_X, window)) {
			linVel.y -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_E, window)) {
			rotVel.y += speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_Q, window)) {
			rotVel.y -= speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_F, window)) {
			rotVel.x += speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_R, window)) {
			rotVel.x -= speed*10;
		}
		linVel.mul(Time.dt / 1000); //convert to m/s
		rotVel.mul(Time.dt / 1000); //convert to rad/s
		rotation.add(rotVel);
		position.add(Vector3.rotate(linVel, new Vector3(-rotation.x, -rotation.y, -rotation.z)));
		updateView();
	}
	
	private void updateView() {
		viewMatrix = Matrix4.Identity();
		viewMatrix.translate(position);
				
		Matrix4 rotationMat = Matrix4.Identity();
		rotationMat.m00 = (float)Math.cos(-rotation.y*Math.PI/180);
		rotationMat.m02 = (float)Math.sin(-rotation.y*Math.PI/180);
		rotationMat.m20 = (float)-Math.sin(-rotation.y*Math.PI/180);
		rotationMat.m22 = (float)Math.cos(-rotation.y*Math.PI/180);
				
		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);

		rotationMat = Matrix4.Identity();
		rotationMat.m00 = 1;
		rotationMat.m11 = (float)Math.cos(-rotation.x*Math.PI/180);
		rotationMat.m12 = (float)-Math.sin(-rotation.x*Math.PI/180);
		rotationMat.m21 = (float)Math.sin(-rotation.x*Math.PI/180);
		rotationMat.m22 = (float)Math.cos(-rotation.x*Math.PI/180);


		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);
	}
	
	public Matrix4 getView() {
		return Matrix4.mul(Matrix4.perspective(viewAngle, 4f/3, nearClip, farClip), viewMatrix, new Matrix4());
	}
}
