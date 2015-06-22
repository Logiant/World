package generation.test;

import java.util.LinkedList;
import java.util.List;

public class Landmass {

	List<Coordinate> positions;

	String name;
	
	float[] color;
	
	public Landmass(String name, float[] color) {
		this.name = name;
		positions = new LinkedList<Coordinate>();
		this.color = color;
	}
	
	
	public void addPoint(int x, int y) {
		positions.add(new Coordinate(x, y));
	}
	
	
	public int getSize() {
		return (positions.size());
	}
	
	public List<Coordinate> getPositions() {
		return new LinkedList<Coordinate>(positions);
	}
	
	
	class Coordinate {
		int x,y;
		
		public Coordinate(int x, int y) {
			this.x=x; this.y=y;
		}
	}
}
