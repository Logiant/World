package main;



import input.Mouse;
import entities.Transform;
import util.Matrix4;
import util.Time;
import util.Vector3;

public class Camera {

	public static float viewAngle = 45;
	public static float nearClip = 0.01f;
	public static float farClip = 500;

	private Vector3 position;
	private Vector3 rotation;
	
	private Vector3 desiredPosition;
	private Vector3 desiredRotation;
	
	private float bias = 0.01f;
	
	float scrollSpeed = 8f;
	float xLimit = 5;
	float yLimit = 5;
	
	
	Vector3 linVel;
	Vector3 rotVel;

	private Matrix4 viewMatrix;
	
	Transform target;

	
	Vector3 follow = new Vector3(0, 10, 30);

	public Camera(Transform target) {
		position = new Vector3();
		rotation = new Vector3();
		this.target = target;
	}
	
	
	public void move(Transform t) {
		position.add(t.position);
		rotation.add(t.rotation);
		updateView();
	}
	
	public void update() {

		changeFollow();
		
		desiredPosition = target.getPosition();
		desiredRotation = target.getRotation();
		
		Vector3 forward = Vector3.rotate(follow, desiredRotation);
				
		desiredPosition.add(forward);
		desiredPosition.mul(-1); desiredRotation.mul(-1);
				
		
		position.x = lerp(position.x, desiredPosition.x, bias*Time.dt*20);
		position.y = lerp(position.y, desiredPosition.y, bias*Time.dt);
		position.z = lerp(position.z, desiredPosition.z, bias*Time.dt);
		rotation.x = desiredRotation.x; rotation.y = desiredRotation.y; rotation.z = desiredRotation.z;

		updateView();
	}
	
	
	private void changeFollow() {
		if (Mouse.RightDown) {
			follow.x += Mouse.DX * scrollSpeed * Time.dt/1000;
			follow.y -= Mouse.DY * scrollSpeed * Time.dt/1000;
			follow.x = Math.min(Math.max(follow.x, -xLimit), xLimit);
			follow.y = Math.min(Math.max(follow.y, 0), 2*yLimit);

		}
	}
	
	
	private float lerp(float a, float b, float t) {
		t = Math.min(t, 1);
		t = Math.max(t, 0);
		
		return a + ((b-a)*t);
	}

	private void updateView() {
		viewMatrix = Matrix4.Identity();
		viewMatrix.translate(position);

		Matrix4 rotationMat = Matrix4.Identity();
		
		double yTheta = -rotation.y*Math.PI/180;
		
		rotationMat.m00 = (float)Math.cos(yTheta);
		rotationMat.m02 = (float)Math.sin(yTheta);
		rotationMat.m20 = (float)-Math.sin(yTheta);
		rotationMat.m22 = (float)Math.cos(yTheta);

		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);

		rotationMat = Matrix4.Identity();
		double xTheta = -rotation.x*Math.PI/180;
		rotationMat.m00 = 1;
		rotationMat.m11 = (float)Math.cos(xTheta);
		rotationMat.m12 = (float)-Math.sin(xTheta);
		rotationMat.m21 = (float)Math.sin(xTheta);
		rotationMat.m22 = (float)Math.cos(xTheta);


		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);
	}
	
	public Matrix4 genView(Transform t) {
		
		Matrix4 viewMatrix = Matrix4.Identity();
		viewMatrix.translate(new Vector3(t.position.x+position.x , t.position.y+position.y, t.position.z+position.z));

		Matrix4 rotationMat = Matrix4.Identity();
		
		double yTheta = -(t.rotation.y + rotation.y)*Math.PI/180;
		rotationMat.m00 = (float)Math.cos(yTheta);
		rotationMat.m02 = (float)Math.sin(yTheta);
		rotationMat.m20 = (float)-Math.sin(yTheta);
		rotationMat.m22 = (float)Math.cos(yTheta);

		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);

		rotationMat = Matrix4.Identity();
		
		double xTheta = -(t.rotation.x + rotation.x)*Math.PI/180;
		rotationMat.m00 = 1;
		rotationMat.m11 = (float)Math.cos(xTheta);
		rotationMat.m12 = (float)-Math.sin(xTheta);
		rotationMat.m21 = (float)Math.sin(xTheta);
		rotationMat.m22 = (float)Math.cos(xTheta);

		
		return Matrix4.mul(Matrix4.perspective(viewAngle, 4f/3, nearClip, farClip), viewMatrix, new Matrix4());
	}

	public Matrix4 getView() {
		return Matrix4.mul(Matrix4.perspective(viewAngle, 4f/3, nearClip, farClip), viewMatrix, new Matrix4());
	}


	public void unMove(Transform t) {
		position.sub(t.position);
		rotation.sub(t.rotation);		
	}
}
