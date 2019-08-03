package main;

import java.util.stream.Collectors;

import calculations.Clustering;
//import calculations.Clustering;
import implementation.UndirectedAffinityGraph;
import test.RealNetwork;

public class Main {
	
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		UndirectedAffinityGraph<Integer, String> graph=RealNetwork.bitcoin();
		long end=System.currentTimeMillis();
		System.out.println("Reading of file: "+(end-start)/1000+ " seconds");

		start=System.currentTimeMillis();
		Clustering<Integer, String> clustering= new Clustering<Integer, String>(graph);
		end=System.currentTimeMillis();
		System.out.println("Clustering: "+(end-start)/1000+ " seconds");
		
		System.out.println("Graph has "+graph.getVertices().size()+ " nodes");
		System.out.println("Graph has "+graph.getEdges().size()+ " edges");
		System.out.println("Number of positive links "+graph.getEdges().entrySet().stream().filter(x->x.getValue()).count());
		System.out.println("Graph "+(clustering.isClusterable()? "is":"is not")+" clusterable");
		System.out.println("Links for removal: \n"+clustering.forRemoval().stream().collect(Collectors.joining("\n")));
		
		
		
	}
}
