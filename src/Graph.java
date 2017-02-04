import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 */
/**
 * @author samir
 *
 */
class Vertex {
	String name;
	List<Edge> edges;
	StringBuilder calledBy;
	StringBuilder calledByA;
	Double latitude,longitude;
	Vertex(String name,Double latitude,Double longitude){
		this.name = name;
		edges = new ArrayList<>();
		calledBy = new StringBuilder();
		calledByA = new StringBuilder();
		this.latitude = latitude;
		this.longitude = longitude;
	}


}
class HeuristicVertex implements Comparable<HeuristicVertex>{
	Vertex v1;
	int edgeCost;
	int heuristicMeasure;
	public HeuristicVertex(Vertex v, int heuristicCost,int edgeCost){
		v1 = v;
		heuristicMeasure = heuristicCost;
		this.edgeCost = edgeCost;
	}
	@Override
	public int compareTo(HeuristicVertex v2) {
		if(this.heuristicMeasure>v2.heuristicMeasure) return 1;
		else if(this.heuristicMeasure==v2.heuristicMeasure) return 0;
		else return -1;
		//return 1;
	}
	
}
class Edge {
	Vertex v1,v2;
	int cost;
	Edge(Vertex v1,Vertex v2,int cost){
		this.v1 = v1;
		this.v2 = v2;
		this.cost = cost;
	}

}

/**
 * @author samir
 *
 */

public class Graph {
	List<Vertex> vertexList;
	Map<Vertex,Integer> distanceStraightLine;
	public Graph(){
		vertexList = new LinkedList<>();
	}
	public Vertex getVertexByName(String nameToLook){
		for(Vertex v:vertexList){
			if(v.name.equals(nameToLook))
				return v;
		}
		System.out.println("Cannot find vertex by that name!!");
		System.exit(0);
		return null;
	}
	public void createHeuristicMap(Vertex v){
		this.distanceStraightLine = new HashMap<>();
		for(Vertex v1:vertexList){
			//System.out.println(" Value double = " + Math.sqrt(Math.pow(v1.latitude-v.latitude, 2)+Math.pow(v1.longitude-v.longitude, 2)));
			distanceStraightLine.put(v1, (int)(100*Math.sqrt(Math.pow(v1.latitude-v.latitude, 2)+Math.pow(v1.longitude-v.longitude, 2))));
			//System.out.println("Straight Line Distance: " + distanceStraightLine.get(v1) );
		}
		
	}
	public void DFS(Vertex v1, Vertex v2,List<String> path){
		Set<Vertex> visited = new HashSet<>();
		if(DFSUtil(v1,v2,visited,path)){
			System.out.print("Path->"+ v1.name+"->");
			for(int i = path.size()-1;i>=1;i--){
				System.out.print(path.get(i)+"->");
			}
			System.out.print(v2.name);
		}
		else System.out.println("Not found");
		
	}
	private boolean DFSUtil(Vertex v1, Vertex v2, Set<Vertex> visited, List<String> path) {
		//System.out.println(v1.name);
		if(v1==v2){
			//path.add(v1.name);
			visited.add(v1);
			System.out.println("Explored: " + v2.name);
			return true;
		}
		if(visited.add(v1)){
			System.out.println("Explored: " + v1.name);
		List<Edge> edgeVertex = v1.edges;
		List<Vertex> adjacentVertices = new LinkedList<>();
		for(Edge e:edgeVertex){
			adjacentVertices.add(e.v2);
		}
		for(Vertex adjV: adjacentVertices){
			if(DFSUtil(adjV,v2,visited,path)){
				path.add(adjV.name);
				return true;
			}
		}
		return false;
		}
		return false;
	}
	
	
	public void AStar(Vertex v1, Vertex v2) {
		createHeuristicMap(v2);
		Set<Vertex> visited = new HashSet<>();
		PriorityQueue<HeuristicVertex> unexplored = new PriorityQueue<>();
		System.out.println("Explored: " + v1.name);
		if(!AStarUtil(v1,v2,visited,unexplored,0)){
				System.out.println("Not found");
				return;
			}
		
		System.out.println("Path"+ v2.calledByA + " -> "+ v2.name);
		
		
		
	}
	
	
	private boolean AStarUtil(Vertex v1, Vertex v2, Set<Vertex> visited,PriorityQueue<HeuristicVertex> unexplored,int gcost) {
		if(v1==v2) return true;
		if(visited.add(v1))
		{
			List<Edge> vAdjacent = new LinkedList<>(v1.edges);
			for(Edge e:vAdjacent){
				if(!visited.contains(e.v2)){
					HeuristicVertex h = new HeuristicVertex(e.v2, gcost + distanceStraightLine.get(e.v2),e.cost);
					unexplored.add(h);
					e.v2.calledByA.append(v1.calledByA +"->" + v1.name);
					
				}
			}
			while(unexplored.size()!=0){
				HeuristicVertex hCur = unexplored.poll(); 
				if(hCur!=null){
					//if(vAdjacent.remove(next)) 
					System.out.println("Explored: " + hCur.v1.name);
					//else System.out.println("Some problem with removing");
					if(AStarUtil(hCur.v1,v2,visited,unexplored,gcost + hCur.edgeCost)){
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}
	/*
	private Edge heuristicFunction(List<Edge> vAdjacent,Vertex v) {
		// TODO Auto-generated method stub
		int min = Integer.MAX_VALUE;
		int cur;
		Edge eMin = null;
		for(Edge e: vAdjacent){
			cur = e.cost+ distanceStraightLine.get(e.v2);
			if(cur<min){
				min = cur;
				eMin = e;
			}
		}
			return eMin;
	}
	*/
	public void BFS(Vertex v1, Vertex v2){
		Set<Vertex> visited = new HashSet<>();
		if(BFSUtil(v1,v2,visited)){
			//System.out.println("Found");
		}
		else System.out.println("Not found");
		
	}
	private boolean BFSUtil(Vertex v1, Vertex v2,Set<Vertex> visited){
		LinkedList<Vertex> qVertex = new LinkedList<>();
		qVertex.add(v1);
		visited.add(v1);
		while(qVertex.size()!=0){
			Vertex cur = qVertex.removeFirst();
			System.out.println("Explored: " + cur.name);
				if(cur==v2){
					cur.calledBy.append(v2.name);
					System.out.println("Path->"+ cur.calledBy.toString());
					return true;
				}
				//List<Edge> edgeVertex = v1.edges;
				for(Edge e:cur.edges){
					if(visited.add(e.v2)){	
					String v2calledBy = (cur.calledBy.equals("")?cur.name+" ":cur.calledBy+" "+ cur.name+" ");
					e.v2.calledBy.append(v2calledBy+"->");
					qVertex.addLast(e.v2);
				}
				}
		}
		return false;
	}
	
	
	
	public static void main(String[] args) {
		List<String> path = new LinkedList<>();
		
		Graph g = new Graph();
		
		/*
		 * Add Vertices below this point with latitude and longitude.
		 */
		
		Vertex arad = new Vertex("arad",46.16667,21.3);
		Vertex bucharest = new Vertex("bucharest",44.41667,26.1);
		Vertex craiova = new Vertex("craiova",44.33333, 23.81667);
		Vertex dobreta = new Vertex("dobreta",44.5,     23.95);
		Vertex eforie = new Vertex("eforie",44.06667, 28.63333);
		Vertex fagaras = new Vertex("fagaras",45.84472, 24.97417);
		Vertex giurgiu = new Vertex("giurgiu",43.90083, 25.97389);
		Vertex hirsova = new Vertex("hirsova",46.68333, 27.53333);
		Vertex iasi = new Vertex("iasi",47.16222, 27.58889);
		Vertex lugoj = new Vertex("lugoj",45.68611, 21.90056);
		Vertex mehadia = new Vertex("mehadia",44.90083, 22.35028);
		Vertex neamt = new Vertex("neamt",46.9275,  26.37083);
		Vertex oradea = new Vertex("oradea",47.07222, 21.92111);
		Vertex pitesti = new Vertex("pitesti",44.86056, 24.86778);
		Vertex rimnicu_vilcea = new Vertex("rimnicu_vilcea",45.10472, 24.37556);
		Vertex sibiu = new Vertex("sibiu",45.79583, 24.15222);
		Vertex timisoara = new Vertex("timisoara",45.75972, 21.23);
		Vertex urziceni = new Vertex("urziceni",44.71806, 26.64528);
		Vertex vaslui = new Vertex("vaslui",46.63833, 27.72917);
		Vertex zerind = new Vertex("zerind",46.63333, 21.66667);
		
		/*
		 * 
		 * Add the created vertices to the graph
		 * 
		 */
		
		g.vertexList.add(arad);
		g.vertexList.add(bucharest);
		g.vertexList.add(craiova);
		g.vertexList.add(dobreta);
		g.vertexList.add(eforie);
		g.vertexList.add(fagaras);
		g.vertexList.add(giurgiu);
		g.vertexList.add(hirsova);
		g.vertexList.add(iasi);
		g.vertexList.add(lugoj);
		g.vertexList.add(mehadia);
		g.vertexList.add(neamt);
		g.vertexList.add(oradea);
		g.vertexList.add(pitesti);
		g.vertexList.add(rimnicu_vilcea);
		g.vertexList.add(sibiu);
		g.vertexList.add(timisoara);
		g.vertexList.add(urziceni);
		g.vertexList.add(vaslui);
		g.vertexList.add(zerind);
		
		
		/*
		 * 
		 * Add edges to the different vertices.
		 * For bidirectional roads, add the same in both direction to the 2 vertices!!
		 * 
		 */
		
		oradea.edges.add(new Edge(oradea,zerind,71));
		zerind.edges.add(new Edge(zerind,arad, 75));
		arad.edges.add(new Edge(arad, timisoara, 118));
		timisoara.edges.add(new Edge(timisoara, lugoj,111));
		lugoj.edges.add(new Edge(lugoj,mehadia,70));
		dobreta.edges.add(new Edge(dobreta,   mehadia,   75));
		oradea.edges.add(new Edge(oradea,sibiu,151));
		arad.edges.add(new Edge(arad, sibiu,140));
		dobreta.edges.add(new Edge(dobreta, craiova,   120));
		sibiu.edges.add(new Edge(sibiu,rimnicu_vilcea, 80));
		sibiu.edges.add(new Edge(sibiu,fagaras,99));
		rimnicu_vilcea.edges.add(new Edge(rimnicu_vilcea, craiova,  146));
		pitesti.edges.add(new Edge(pitesti,   craiova,   138));
		rimnicu_vilcea.edges.add(new Edge(rimnicu_vilcea, pitesti,   97));
		bucharest.edges.add(new Edge(bucharest, pitesti,   101));
		bucharest.edges.add(new Edge(bucharest, fagaras,  211));
		bucharest.edges.add(new Edge(bucharest, giurgiu,90));
		bucharest.edges.add(new Edge(bucharest, urziceni,  85));
		vaslui.edges.add(new Edge(vaslui,urziceni,  142));
		hirsova.edges.add(new Edge(hirsova, urziceni,  98));
		hirsova.edges.add(new Edge(hirsova,   eforie,86));
		vaslui.edges.add(new Edge(vaslui,iasi,92));
		neamt.edges.add(new Edge(neamt,iasi,  87));
		zerind.edges.add(new Edge(zerind,oradea,71));
		arad.edges.add(new Edge(arad,zerind,75));
		timisoara.edges.add(new Edge(timisoara,arad,118));
		lugoj.edges.add(new Edge(lugoj,timisoara,111));
		mehadia.edges.add(new Edge(mehadia,lugoj,70));
		mehadia.edges.add(new Edge(mehadia,dobreta,75));
		sibiu.edges.add(new Edge(sibiu,oradea,151));
		sibiu.edges.add(new Edge(sibiu,arad,140));
		craiova.edges.add(new Edge(craiova,dobreta,120));
		rimnicu_vilcea.edges.add(new Edge(rimnicu_vilcea,sibiu,80));
		fagaras.edges.add(new Edge(fagaras,sibiu,99));
		craiova.edges.add(new Edge(craiova,rimnicu_vilcea,146));
		craiova.edges.add(new Edge(craiova,pitesti,138));
		pitesti.edges.add(new Edge(pitesti,rimnicu_vilcea,97));
		pitesti.edges.add(new Edge(pitesti,bucharest,101));
		fagaras.edges.add(new Edge(fagaras,bucharest,211));
		giurgiu.edges.add(new Edge(giurgiu,bucharest,90));
		urziceni.edges.add(new Edge(urziceni,bucharest,85));
		urziceni.edges.add(new Edge(urziceni,vaslui,142));
		urziceni.edges.add(new Edge(urziceni,hirsova,98));
		eforie.edges.add(new Edge(eforie,hirsova,86));
		iasi.edges.add(new Edge(iasi,vaslui,92));
		iasi.edges.add(new Edge(iasi,neamt,87));
		
		/*
		 * 
		 * Code below takes 2 city names as input and prints DFS, BFS and A* traversal for these!
		 * 
		 */

		System.out.println("Enter vertex 1:");
		Scanner in = new Scanner(System.in);
		String v1Name = in.next();
		System.out.println("V1:" + v1Name);
		System.out.println("Enter vertex 2:");
		String v2Name = in.next();
		System.out.println("V2:" + v2Name);
		in.close();
		Vertex v1 = g.getVertexByName(v1Name);
		Vertex v2 = g.getVertexByName(v2Name);
		System.out.println("DFS");
		g.DFS(v1, v2, path);
		System.out.println();
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("BFS");
		g.BFS(v1, v2);
		path = new LinkedList<>();
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("A*");
		g.AStar(v1, v2);
		
		
	}
	
}
