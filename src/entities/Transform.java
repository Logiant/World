package entities;

import input.Keyboard;

import org.lwjgl.glfw.GLFW;

import util.Quaternion;
import util.Time;
import util.Vector3;

public class Transform {

	public Vector3 position;
	public Vector3 rotation;
	
	Vector3 linVel;
	Vector3 rotVel;
	
	public Transform() {
		position = new Vector3();
		rotation = new Vector3();
	}
	
	public void update(float speed, long window) {

		linVel = new Vector3();
		rotVel = new Vector3();

		if (Keyboard.keyDown(GLFW.GLFW_KEY_S, window)) {
			linVel.z = speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_W, window)) {
			linVel.z -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_D, window)) {
			linVel.x += speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_A, window)) {
			linVel.x -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_X, window)) {
			linVel.y += speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_Z, window)) {
			linVel.y -= speed;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_Q, window)) {
			rotVel.y += speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_E, window)) {
			rotVel.y -= speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_R, window)) {
			rotVel.x += speed*10;
		}if (Keyboard.keyDown(GLFW.GLFW_KEY_F, window)) {
			rotVel.x -= speed*10;
		}
		linVel.mul(Time.dt / 1000); //convert to m/s
		rotVel.mul(Time.dt / 1000); //convert to rad/s
		rotation.add(rotVel);
		position.add(Vector3.rotate(linVel, new Vector3(rotation.x, rotation.y, rotation.z)));
	}

	public Quaternion getQuaternion() {
		return Quaternion.eulerAngles(rotation.x, rotation.y, rotation.z);
	}
	
	
	public Vector3 getPosition() {
		return new Vector3(position);
	}
	
	public Vector3 getRotation() {
		return new Vector3(rotation);
	}
}
