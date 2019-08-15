package main;

import java.util.Scanner;

import calculations.Clustering;
import implementation.UndirectedAffinityGraph;
import test.RandomGraph;
import test.RealNetwork;

public class SocialNetworkAnalysisProgram {
	
	private static Scanner sc=new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Welcome to program for analysis of social network.");
		System.out.println("Please choose option number: ");
		System.out.println("1 - small network analysis");
		System.out.println("2 - random network analysis");
		System.out.println("3 - real network analysis");
		System.out.println("4 - back");
		
		String input=sc.nextLine();
		while(!"4".equals(input)) {
			System.out.println();
			firstOption(input.trim());
			System.out.println();
			System.out.println("1 - small network analysis");
			System.out.println("2 - random network analysis");
			System.out.println("3 - real network analysis");
			System.out.println("4 - back");
			input=sc.nextLine();
		}
	}

	private static void firstOption(String input) {
		switch (input) {
		case "1":
			smallNetworkAnalysis();
			break;
		case "2":
			randomNetworkAnalysis();
			break;
		case "3":
			realNetworkAnalysis();
			break;
		default:
			break;
		}
		
	}
	
	private static void realNetworkAnalysis() {
		System.out.println("Chose network you want to analyse");
		System.out.println("1 - Bitcoin Alpha trust weighted signed network");
		System.out.println("2 - Wikipedia Requests for Adminship");
		String in=sc.nextLine().trim();
		switch (in) {
		case "1":
			UndirectedAffinityGraph<Integer, String> bitcoin=RealNetwork.bitcoin();
			System.out.println("This may take a while...");
			analyse(bitcoin);
			break;
		case "2":
			UndirectedAffinityGraph<String, String> wikipedia=RealNetwork.wikipedia();
			System.out.println("This may take a while...");
			analyse(wikipedia);
			break;
		default:
			break;
		}
	}

	private static void randomNetworkAnalysis() {
		System.out.println("Input number of nodes: ");
		String nON=sc.nextLine().trim();
		System.out.println("Input number of clusters: ");
		String nOC=sc.nextLine().trim();
		int numberOfNodes=0;
		int numberOfClusters=0;
		try {
			numberOfNodes=Integer.parseInt(nON);
		}catch (Exception e) {
			System.err.println("Bad format for number of nodes.");
		}
		try {
			numberOfClusters=Integer.parseInt(nOC);
		}catch (Exception e) {
			System.err.println("Bad format for number of clusters.");
		}
		if(numberOfNodes==0 || numberOfClusters==0 || numberOfClusters>numberOfNodes) {
			System.err.println("You made a mistake during input.Number of nodes or clusters cant be 0\nand number of nodes must be greater than number of clusters");
			return;
		}else {
			RandomGraph randomGraph=new RandomGraph(numberOfNodes, numberOfClusters);
			UndirectedAffinityGraph<Integer, String> graph=randomGraph.generateClusterableGraph();
			analyse(graph);
		}
		
		
	}

	private static void smallNetworkAnalysis() {
		UndirectedAffinityGraph<Integer, String> graph=RealNetwork.smallNetwork();
		analyse(graph);
	}
	
	private static<V,E> void analyse(UndirectedAffinityGraph<V,E> graph) {
		long start=System.currentTimeMillis();
		Clustering<V, E> calculations= new Clustering<V, E>(graph);
		System.out.println("Number of nodes: " + calculations.numberOfNodes());
		System.out.println("Number of edges: " + calculations.numberOfEdges());
		System.out.println("Percentage of positive edges: " + calculations.percenteOfPositive() + " %");
		System.out.println("Clusterable: " + (calculations.isClusterable()? "YES":"NO"));
		System.out.println("Number of clusters: " + calculations.numberOfClusters());
		System.out.println("Number of links for removal: " +calculations.forRemoval().size());
		System.out.println("Number of coalitions: " +calculations.numberOfCoalitions());
		System.out.println("Number of anticoalitions: " +calculations.numberOfAnticoalitions());
		System.out.println("Averege degree in coalitions: " + String.format("%.2f", calculations.averageDegreeInCoalition()));
		System.out.println("Averege degree in anticoalitions: " + String.format("%.2f", calculations.averageDegreeInAnticoalition()));
		System.out.println("Average density in coalitions: " + String.format("%.2f",calculations.averageDensityOfCoalitions()));
		System.out.println("Average density in anticoalitions: " + String.format("%.2f",calculations.averageDensityOfAnticoalitions()));

		System.out.println("Graph contains giant component: " + (calculations.existenceOfGiant()? "YES":"NO"));
		System.out.println("Cluster network: \n" +calculations.formatClusterNetwork());
		System.out.println("Links for removal: \n"+ calculations.formatRemoval());
		System.out.println(calculations.formatCoalitions());
		
		long end=System.currentTimeMillis();
		System.out.println("Time: "+(end-start)/1000+ " seconds");
		
	}
}
