package main;

import java.util.stream.Collectors;

import calculations.Clustering;
//import calculations.Clustering;
import implementation.UndirectedAffinityGraph;
import test.RandomGraph;
import test.RealNetwork;

public class Main {
	
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		RandomGraph r=new RandomGraph(10000,50);
		UndirectedAffinityGraph<Integer, String> graph=r.generateClusterableGraph();
		long end=System.currentTimeMillis();
		System.out.println("Random graph making: "+(end-start)/1000+ " seconds");

		start=System.currentTimeMillis();
		Clustering<Integer, String> clustering= new Clustering<>(graph);
		end=System.currentTimeMillis();
		System.out.println("Clustering: "+(end-start)/1000+ " seconds");
		
		System.out.println("Graph has "+graph.getVertices().size()+ " nodes");
		System.out.println("Graph has "+graph.getEdges().size()+ " edges");
		System.out.println("Number of positive links "+graph.getEdges().entrySet().stream().filter(x->x.getValue()).count());
		System.out.println("Graph "+(clustering.isClusterable()? "is":"is not")+" clusterable");
		System.out.println("Number of clusters is: "+clustering.getComponents().size());
//		System.out.println("Links for removal: \n"+clustering.forRemoval().stream().collect(Collectors.joining("\n")));
//		System.out.println("Cluster network: \n"+clustering.clusterNetwork());
		
		
	}
}
