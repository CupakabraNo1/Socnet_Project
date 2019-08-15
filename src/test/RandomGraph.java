package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.random.RandomDataGenerator;

import implementation.UndirectedAffinityGraph;

public class RandomGraph{
	
	private UndirectedAffinityGraph<Integer, String> graph;
	private int numOfNodes;
	private int numOfClusters;
	private int numOfPlus;
	private int numOfMinus;
	private RandomDataGenerator random=new RandomDataGenerator();
	
	public RandomGraph(int size,int clusters) {
		numOfNodes=size;
		numOfClusters=clusters;
		numOfPlus=0;
		numOfMinus=0;
		graph=new UndirectedAffinityGraph<Integer, String>();
	}
	
	public UndirectedAffinityGraph<Integer, String> generateClusterableGraph(){
		generateGraphVertices();
		generateGraphEdges();
		return graph;
	}

	private void generateGraphVertices() {
		for(int i=0; i<numOfNodes; i++) {
			graph.addVertex(i);
		}	
	}
	
	private void generateGraphEdges() {
		
		//clusters
		List<Integer> nodes=graph.getVertices().stream().collect(Collectors.toList());
		Collections.shuffle(nodes);
		Set<Set<Integer>> groups=new HashSet<>();
		int breaks=numOfClusters-1;
		while(breaks>0) {
			Set<Integer> g=new HashSet<>();
			int n=random.nextInt(1, nodes.size()-breaks);
			breaks--;
			g.addAll(nodes.subList(0, n));
			nodes.removeAll(g);
			groups.add(g);
		}
		groups.add(nodes.stream().collect(Collectors.toSet()));
		
		//+edges
		groups.stream().forEach(x->makePlus(x));	
		numOfPlus=graph.getEdges().size();
		
		//-edges
		
		List<List<Integer>> l1=groups.stream().map(x->new ArrayList<>(x)).collect(Collectors.toList());
		List<List<Integer>> l2=groups.stream().map(x->new ArrayList<>(x)).collect(Collectors.toList());
		Collections.shuffle(l1);
		Collections.shuffle(l2);
		int i=0;
		while(i<l1.size()) {
			List<Integer> c1=l1.get(i);
			List<Integer> c2=l2.get(i);
			if(!c1.contains(c2.get(0)))
				makeMinus(c1,c2);
			i++;
		}
	}
	
	private void makeMinus(List<Integer> c1, List<Integer> c2) {
		int r=random.nextInt(1, 200);
		while(r>0) {
			int m=random.nextInt(0, Math.min(c1.size(), c2.size())-1);
			Collections.shuffle(c1);
			Collections.shuffle(c2);
			while(m>=0) {
				Integer s=c1.get(m);
				Integer t=c2.get(m);
				graph.addEdge(s+" "+t, false, s, t);
				m--;
			}
			r--;
		}
	}

	private void makePlus(Set<Integer> cluster) {
		LinkedList<Integer> list=new LinkedList<>(cluster);
		int r=random.nextInt(1, 5);
		while(r>0) {
			Collections.shuffle(list);
			for(int i=0; i<list.size()-1; i++) {
				Integer s=list.get(i);
				Integer t=list.get(i+1);
				if(!graph.getNeighbours(s, true).contains(t))
					graph.addEdge(s+" "+t, true, s, t);
			}
			r--;
		}
	}
	
	
}


