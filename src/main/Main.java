package main;

import calculations.Clustering;
import implementation.UndirectedAffinityGraph;
import test.RealNetwork;

public class Main {
	
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		UndirectedAffinityGraph<String, String> graph=RealNetwork.wikipedia();
		long end=System.currentTimeMillis();
		System.out.print("Reading of file: "+(end-start)/1000+ " seconds");
		
		start=System.currentTimeMillis();
		Clustering<String, String> clustering= new Clustering<String, String>(graph);
		end=System.currentTimeMillis();
		System.out.print("Clustering: "+(end-start)/1000+ " seconds");
		
		System.out.println("Graph has "+graph.getVertices().size()+ " nodes");
		System.out.println("Graph has "+graph.getEdges().size()+ " edges");
		System.out.println(clustering.isClusterable());
		
	}
}