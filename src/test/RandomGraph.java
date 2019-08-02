package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import implementation.UndirectedAffinityGraph;

public class RandomGraph{
	
	private UndirectedAffinityGraph<Integer, String> graph;
	private int numOfNodes;
	private int numOfClusters;
	private RandomDataGenerator random=new RandomDataGenerator();
	
	public RandomGraph(int size) {
		this.graph=new UndirectedAffinityGraph<>();
		numOfNodes=random.nextInt(1, size);
		int r=random.nextInt(0, numOfNodes);
		numOfClusters=(r==0? 1:r);
		System.out.println("Number of nodes "+numOfNodes);
		System.out.println("Number of clusters "+numOfClusters);
	}
	
	public UndirectedAffinityGraph<Integer, String> generateClusterableGraph(){
		generateGraphVertices();
		generateGraphEdges();
		System.out.println("Graph ready.");
		return graph;
	}

	private void generateGraphVertices() {
		for(int i=0; i<numOfNodes; i++) {
			graph.addVertex(i);
		}	
	}
	
	private void generateGraphEdges() {
		List<List<Integer>> clusters=new ArrayList<>();
		Collection<Integer> col=graph.getVertices().stream().collect(Collectors.toList());
		LinkedList<Integer> vertices=new LinkedList<Integer>(col);
		double prob=1.00/numOfClusters;
		//clusters
		System.out.println("C");
		for(int i = 1; i <= numOfClusters; i++) {
			List<Integer> list=new ArrayList<>();
			int clusAv=numOfClusters-i;
			list=makeCluster(vertices, i*prob, clusAv);
			vertices.removeAll(list);
			clusters.add(list);
		}
		//+ edges
		System.out.println("+");
		clusters.stream().forEach(x->makePlus(x));	
		System.out.println("+ ok");
		//- edges
		int c=clusters.size();
		int maxC=(c*(c-1))/2;
		int b=0;
		int r=random.nextInt(0, maxC);
		System.out.println("R= "+r);
		System.out.println("-");
		while(b<r) {		
			List<Integer> c1=clusters.get(random.nextInt(0,c-1));
			List<Integer> c2=clusters.get(random.nextInt(0,c-1));
			List<Integer> c1n=graph.getVertices().stream().filter(x->c1.contains(x)).flatMap(x->graph.getNeighbours(x,true).stream()).collect(Collectors.toList());
			if(c1!=c2 && !c1n.containsAll(c2)) {
				makeSomeMinus(c1,c2);
			}
			b++;
		}
		System.out.println("- ok");
	}

	private void makeSomeMinus(List<Integer> c1, List<Integer> c2) {
			int numOfM=random.nextInt(0, c1.size());
			int b=0;
			while(b<numOfM) {
				Integer n=c1.get(random.nextInt(0, c1.size()-1));
				Integer m=c2.get(random.nextInt(0, c2.size()-1));
				if(!graph.getNeighbours(m, false).contains(n)) {
					if(random.nextGaussian(0,1)<0.5) {
						graph.addEdge(n+" "+m,false, n, m);
					}
				}
				b++;
			}
		}
		

	private void makePlus(List<Integer> cluster) {
		LinkedList<Integer> visited=new LinkedList<>();
		LinkedList<Integer> toVisit=new LinkedList<>(cluster);
		int size=cluster.size();
		int minE=size-1;
		int maxE=(size*(size-1))/2;
		for(Integer i:toVisit) {
			if(visited.isEmpty()) visited.add(i);
			else {
				Integer j=visited.get(random.nextInt(0,visited.size()-1));
				graph.addEdge(i+" "+j,true, i, j);
				visited.add(i);
			}
		}
		int b=maxE-minE;
		int r=0;
		if(b!=0) r=random.nextInt(0,b-1);
		int i=0;
		while(i<r) {
			Integer n=visited.get(random.nextInt(0,visited.size()-1));
			Integer m=visited.get(random.nextInt(0,visited.size()-1));
			if(n!=m && !graph.getNeighbours(n,true).contains(m)) {
				graph.addEdge(n+" "+m,true, n, m);
				i++;
			}	
		}
	}

	private List<Integer> makeCluster(List<Integer> vert, double line, int clusAv) {
		List<Integer> list=new ArrayList<Integer>();
		list=vert.stream().filter(x->random.nextGaussian(0,1)<line).limit(vert.size()-clusAv).collect(Collectors.toList());
		if(list.isEmpty()) return vert.stream().limit(vert.size()-clusAv).collect(Collectors.toList());
		if(clusAv==0 && list.size()<vert.size()) return vert;
		return list;
	}
	
	
}
