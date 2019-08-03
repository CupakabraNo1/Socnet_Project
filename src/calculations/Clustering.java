package calculations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import implementation.Cluster;
import implementation.UndirectedAffinityGraph;


public class Clustering<V,L> {
	
	private UndirectedAffinityGraph<V, L> graph;
	private Set<V> visited;
	private Set<UndirectedAffinityGraph<V, L>> clusters;
	private Set<Set<V>> components;
	public List<L> list;
	
	public Clustering(UndirectedAffinityGraph<V, L> graph) {
		this.graph=graph.clone();
		clusters=new HashSet<>();
		components=new HashSet<Set<V>>();
		list=new LinkedList<L>();
		components();
		clustering();
	}

	public Set<Set<V>> getComponents() {
		return components;
	}
	
	public Set<UndirectedAffinityGraph<V, L>> getClusters(){
		return clusters;
	}
	
	private void clustering() {
		for(Set<V> s:components) {
			UndirectedAffinityGraph<V, L> cluster=new UndirectedAffinityGraph<>();
			s.stream().forEach(x->cluster.addVertex(x));
			Map<L, Boolean> map=graph.getEdges();
			for(L l:map.keySet()) {
				Pair<V> pair=graph.getPair(l,map.get(l));
				if(pair!=null && cluster.getVertices().contains(pair.getFirst()) && cluster.getVertices().contains(pair.getSecond())){
					cluster.addEdge(l, map.get(l), pair.getFirst(), pair.getSecond());
				}
			}
			clusters.add(cluster);
		}
	}

	
	private void components() {
		visited=new HashSet<>();
		components=new HashSet<Set<V>>();
		for (V v:graph.getVertices()) {
			if (!visited.contains(v)) {
				components.add(bfs(v));
			}
		}			
	}

	private	Set<V> bfs(V v) {
		Set<V> comp = new TreeSet<>();
		Queue<V> queue = new LinkedList<>();
		comp.add(v);
		visited.add(v);
		queue.add(v);
		while (!queue.isEmpty()) {
			V current = queue.poll();
			for (V x : graph.getNeighbours(current,true)) {
				if (!visited.contains(x)) {
					comp.add(x);
					visited.add(x);
					queue.add(x);
				}
			}		
		}
		return comp;
	}
	
	public boolean isClusterable() {
		for(UndirectedAffinityGraph<V, L> g:clusters) {
			 if(g.getEdges().values().stream().collect(Collectors.toList()).contains(false)) return false;
		}
		return true;
	}
	
	public Map<Boolean, List<UndirectedAffinityGraph<V, L>>> coalitions(){
		Map<Boolean, List<UndirectedAffinityGraph<V, L>>> map=new TreeMap<>();
		List<UndirectedAffinityGraph<V, L>> t=new ArrayList<>();
		List<UndirectedAffinityGraph<V, L>> f=new ArrayList<>();
		for(UndirectedAffinityGraph<V, L> c:clusters) {
			if(coalition(c)) {
				t.add(c);
			}else {
				f.add(c);
			}
		}
		map.put(true, t);
		map.put(false, f);
		return map;
	}
	
	public static<V,L> boolean coalition(UndirectedAffinityGraph<V, L> cluster) {
		return !cluster.getEdges().values().contains(false);
	}
	
	public List<L> forRemoval(){
		return clusters.stream()
				.flatMap(x->x.getEdges()
						.entrySet()
						.stream())
				.filter(x->!x.getValue())
				.map(x->x.getKey())
				.collect(Collectors.toList());
	}
	
	public UndirectedSparseGraph<Cluster<V, L>, String> clusterNetwork(){
		UndirectedSparseGraph<Cluster<V,L>, String> network=new UndirectedSparseGraph<Cluster<V,L>, String>();
		int br=0;
		Set<Cluster<V, L>> clus=new HashSet<>();
		for(UndirectedAffinityGraph<V, L> g:clusters) {
			Cluster<V, L> cluster=new Cluster<>();
			cluster.setName(br++);
			cluster.setGraph(g);
			clus.add(cluster);
		}
		
		for(Cluster<V, L> s:clus) {
			Set<V> nei=s.nodes().stream().flatMap(x->graph.getNeighbours(x, false).stream()).filter(y->!s.nodes().contains(y)).collect(Collectors.toSet());
			for(Cluster<V,L> t:clus) {
				for(V v:nei) {
					if(t.nodes().contains(v)) network.addEdge(s.getName()+" "+t.getName(),s, t);
				}
			}
		}
		
		return network;
	}
}
