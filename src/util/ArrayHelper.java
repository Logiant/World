package util;

public class ArrayHelper {

	public static float[] CopyArray(float[] a1, float[] a2) {
		float[] combined = new float[a1.length + a2.length];
		System.arraycopy(a1, 0, combined, 0, a1.length);
		System.arraycopy(a2, 0, combined, a1.length, a2.length);
		return combined;
	}
	
	public static int[] CopyArray(int[] a1, int[] a2) {
		int[] combined = new int[a1.length + a2.length];
		System.arraycopy(a1, 0, combined, 0, a1.length);
		System.arraycopy(a2, 0, combined, a1.length, a2.length);
		return combined;
	}
}
