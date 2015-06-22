package generation.test;

import java.util.*;

public class WaterGen {

	Random rGen;

	List<int[]> waterSources;

	public WaterGen(long seed) {
		rGen = new Random(seed);
	}


	public float[][] moistureMap(float[][] binary, float[][] water) {
		int size = binary.length-1;
		float[][] moisture = new float[size][size];

		int moistureZone = size/25;


		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (water[i][j] > 0 || binary[i][j] == 0) {
					moisture[i][j] = 1;
					for (int dx = -moistureZone; dx < moistureZone; dx++) {
						for (int dy = -moistureZone; dy < moistureZone; dy++) {

							if (i+dx < size && i+dx >= 0 && j+dy < size && j+dy >=0 && moisture[i+dx][j+dy] != 1) {
								
								float scaleX = (moistureZone-Math.abs(dx))/(float)moistureZone;
								float scaleY = (moistureZone-Math.abs(dy))/(float)moistureZone;
								
								float scale = Math.min(scaleX, scaleY);
								
								moisture[i+dx][j+dy] = Math.max(scale, moisture[i+dx][j+dy]);
							}
						}

					}
				}					
			}
		}

		return moisture;
	}



	public float[][] generateWater(float[][] heightMap, float[][] binaryMap, float lowThresh) {

		waterSources = new LinkedList<int[]>();

		int size = heightMap.length;
		float[][] waterMap = new float[size][size];
		List<int[]> highPoints = new ArrayList<int[]>();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (heightMap[i][j] > lowThresh) {
					int[] pos = {i, j};
					highPoints.add(pos);
				}
			}
		}

		int numRivers = (size/20);
		for (int i = 0; i < numRivers; i++) {
			int index = rGen.nextInt(highPoints.size());
			int[] pos = highPoints.get(index);
			highPoints.remove(index);
			waterMap[pos[0]][pos[1]] = 0.25f;
			waterSources.add(pos);
		}

		flow(waterMap, binaryMap, heightMap, waterSources);

		return waterMap;
	}


	private void flow(float[][] waterMap, float[][] binary, float[][] heightMap, List<int[]> waterSources) {
		for (int[] pos : waterSources) {
			int x = pos[0]; int y = pos[1];

			waterMap[x][y] += 0.5f;


			Node end = trickle(heightMap, binary, x, y, waterMap);

			while (end.parent != null) {
				waterMap[end.x][end.y] += 0.1f;
				end = end.parent;
			}

		}
	}


	private Node trickle(float[][] heightMap, float[][] binary, int x, int y, float[][] waterMap) {

		float movementCost = 0f;

		HashMap<String,Node> existingNodes = new HashMap<String,Node>();

		HashMap<String, Node> openList = new HashMap<String, Node>();


		Node node = new Node(x, y); //starting node
		node.parent = null;

		openList.put(genKey(x,y),node);
		//search neighbors for lowest total cost
		// add neighbors to openList

		while(binary[x][y] > 0){//&& openList.size() > 0) {



			//Left
			Node L = null;
			float cost = heightMap[x-1][y];
			cost -= heightMap[x][y];
			if (!existingNodes.containsKey(genKey(x-1,y))) {
				L = new Node(x-1, y);
				L.parent = node;
				L.cost = node.cost + movementCost + cost;
				existingNodes.put(genKey(x-1, y), L);
			} else {
				Node oldL = existingNodes.get(genKey(x-1,y));
				if(oldL.cost > node.cost + movementCost + cost) {
					L = oldL;
					L.parent = node;
					L.cost = node.cost + movementCost + cost;
				}
			}

			//Right
			Node R = null;
			cost = heightMap[x+1][y];
			cost -= heightMap[x][y];
			if (!existingNodes.containsKey(genKey(x+1,y))) {
				R = new Node(x+1, y);
				R.parent = node;
				R.cost = node.cost + movementCost + cost;
				existingNodes.put(genKey(x+1, y), R);
			} else {
				Node oldR = existingNodes.get(genKey(x+1,y));
				if(oldR.cost > node.cost + movementCost + cost) {
					R = oldR;
					R.parent = node;
					R.cost = node.cost + movementCost + cost;
				}
			}		
			//up
			Node U = null;
			cost = heightMap[x][y+1];
			cost -= heightMap[x][y];
			if (!existingNodes.containsKey(genKey(x,y+1))) {
				U = new Node(x, y+1);
				U.parent = node;
				U.cost = node.cost + movementCost + cost;
				existingNodes.put(genKey(x, y+1), U);
			} else {
				Node oldU = existingNodes.get(genKey(x,y+1));
				if(oldU.cost > node.cost + movementCost + cost) {
					U = oldU;
					U.parent = node;
					U.cost = node.cost + movementCost + cost;
				}
			}
			//down
			Node D = null;
			cost = heightMap[x][y-1];
			cost -= heightMap[x][y];
			if (!existingNodes.containsKey(genKey(x,y-1))) {
				D = new Node(x, y-1);
				D.parent = node;
				D.cost = node.cost + movementCost + cost;
				existingNodes.put(genKey(x, y-1), D);
			} else {
				Node oldD = existingNodes.get(genKey(x,y-1));
				if(oldD.cost > node.cost + movementCost + cost) {
					D = oldD;
					D.parent = node;
					D.cost = node.cost + movementCost + cost;
				}
			}

			if (L!= null) openList.put(genKey(x-1,y), L);
			if (R!= null) openList.put(genKey(x+1,y), R);
			if (U!= null) openList.put(genKey(x,y+1), U);
			if (D!= null) openList.put(genKey(x,y-1), D);

			openList.remove(genKey(x,y));



			float min = 999999999999f;


			for (Node n:openList.values()) {
				if (n.cost < min) {
					waterMap[node.x][node.y] = 0.1f;
					min = n.cost;
					node = n;
				}
			}

			x = node.x; y = node.y;

		}



		return node;
	}


	private String genKey(int x, int y) {
		return (""+x+""+y);
	}

	class Node {

		public int x, y;
		public Node parent;
		public float cost;

		public Node(int x, int y) {
			this.x = x; this.y = y;
		}

	}

}
