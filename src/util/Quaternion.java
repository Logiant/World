package util;

public class Quaternion {

	public float x, y, z, w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x; this.y = y; this.z = z; this.w = w;
	}
	
	public Quaternion()  {
		this(0,0,0,1);
	}
	
	
	public static Quaternion eulerAngles(float xAngle, float yAngle, float zAngle) {
		
		
		float cosValX = (float)Math.cos(xAngle*Math.PI/360); //cos(theta/2)
		float sinValX = (float)Math.sin(xAngle*Math.PI/360); //cos(sin/2)
		float cosValY = (float)Math.cos(yAngle*Math.PI/360); //cos(theta/2)
		float sinValY = (float)Math.sin(yAngle*Math.PI/360); //cos(sin/2)
	//	float cosValZ = (float)Math.cos(zAngle*Math.PI/360); //cos(theta/2)
	//	float sinValZ = (float)Math.sin(zAngle*Math.PI/360); //cos(sin/2)
		
		Quaternion x = new Quaternion(sinValX, 0, 0, cosValX);
		Quaternion y = new Quaternion(0, sinValY, 0, cosValY);
	//	Quaternion z = new Quaternion(0, 0, sinValZ, cosValZ);
		
		
		Quaternion temp = Quaternion.mul(y, x);
	//	temp = Quaternion.mul(temp, z);
		return temp;
	}
	
	
	
	
	
	
	public static Quaternion mul(Quaternion a, Quaternion b) {
		
		float x = a.w*b.x + a.x*b.w + a.y*b.z - a.z*b.y;
		float y = a.w*b.y - a.x*b.z + a.y*b.w + a.z*b.x;
		float z = a.w*b.z + a.x*b.y - a.y*b.x + a.z*b.w;
		float w = a.w*b.w - a.x*b.x - a.y*b.y - a.z*b.z;
		
		
		return new Quaternion(x, y, z, w);
	}
	
	@Override
	public String toString() {
		return "x: " + x + " Y: " + y + " Z: " + z + " W: " + w;
	}
	
}
