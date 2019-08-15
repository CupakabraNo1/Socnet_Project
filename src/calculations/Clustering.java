package calculations;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import implementation.Cluster;
import implementation.UndirectedAffinityGraph;


public class Clustering<V,E> {
	
	private UndirectedAffinityGraph<V, E> graph;
	private Set<V> visited;
	private Set<UndirectedAffinityGraph<V, E>> clusters;
	private Set<Set<V>> components;
	public List<E> list;
	Map<Boolean, List<UndirectedAffinityGraph<V, E>>> map;
	
	public Clustering(UndirectedAffinityGraph<V, E> graph) {
		this.graph=graph.clone();
		clusters=new HashSet<>();
		components=new HashSet<Set<V>>();
		list=new LinkedList<E>();
		map=new TreeMap<>();
		components();
		clustering();
	}

	public Set<Set<V>> getComponents() {
		return components;
	}
	
	public Set<UndirectedAffinityGraph<V, E>> getClusters(){
		return clusters;
	}
	
	private void clustering() {
		for(Set<V> s:components) {
			UndirectedAffinityGraph<V, E> cluster=new UndirectedAffinityGraph<>();
			s.stream().forEach(x->cluster.addVertex(x));
			Map<E, Boolean> map=graph.getEdges();
			for(E l:map.keySet()) {
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
		for(UndirectedAffinityGraph<V, E> g:clusters) {
			 if(g.getEdges().values().stream().collect(Collectors.toList()).contains(false)) return false;
		}
		return true;
	}
	
	public Map<Boolean, List<UndirectedAffinityGraph<V, E>>> coalitions(){
		Map<Boolean,List<UndirectedAffinityGraph<V, E>>> map=new TreeMap<>();
		List<UndirectedAffinityGraph<V, E>> t=new ArrayList<>();
		List<UndirectedAffinityGraph<V, E>> f=new ArrayList<>();
		for(UndirectedAffinityGraph<V, E> c:clusters) {
			if(coalition(c)) {
				t.add(c);
			}else {
				f.add(c);
			}
		}
		map.put(true, t);
		map.put(false, f);
		this.map=map;
		return map;
	}
	
	public static<V,L> boolean coalition(UndirectedAffinityGraph<V, L> cluster) {
		return !cluster.getEdges().values().contains(false);
	}
	
	public List<E> forRemoval(){
		return clusters.stream()
				.flatMap(x->x.getEdges()
						.entrySet()
						.stream())
				.filter(x->!x.getValue())
				.map(x->x.getKey())
				.collect(Collectors.toList());
	}
	
	public UndirectedSparseGraph<Cluster<V, E>, String> clusterNetwork(){
		UndirectedSparseGraph<Cluster<V,E>, String> network=new UndirectedSparseGraph<Cluster<V,E>, String>();
		int br=0;
		Set<Cluster<V, E>> clus=new HashSet<>();
		for(UndirectedAffinityGraph<V, E> g:clusters) {
			Cluster<V, E> cluster=new Cluster<>();
			cluster.setName(br++);
			cluster.setGraph(g);
			clus.add(cluster);
		}
		
		for(Cluster<V, E> s:clus) {
			Set<V> nei=s.nodes().stream().flatMap(x->graph.getNeighbours(x, false).stream()).
					filter(y->!s.nodes().contains(y)).collect(Collectors.toSet());
			for(Cluster<V,E> t:clus) {
				for(V v:nei) {
					if(t.nodes().contains(v)) network.addEdge(s.getName()+" "+t.getName(),s, t);
				}
			}
		}
		
		return network;
	}
	
	public int numberOfNodes() {
		return graph.getVertices().size();
	}
	
	public int numberOfEdges() {
		return graph.getEdges().entrySet().size();
	}
	
	public String percenteOfPositive() {
		double numOfNodes=graph.getEdges().size()*1.0;
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format((graph.getEdges().entrySet().stream().filter(x->x.getValue()).count()*100)/numOfNodes);
	}
	
	public int numberOfClusters() {
		return clusters.size();
	}
	
	public String formatRemoval() {
		StringBuilder sb=new StringBuilder();
		List<E> fr=forRemoval();
		for (int i = 0; i < fr.size(); i++) {
			if(i==fr.size()-1) sb.append(fr.get(i));
			else sb.append(fr.get(i)+", ");
			if(i%2==1) sb.append("\n");	
		}
		return sb.toString();
	}
	
	public int numberOfCoalitions() {
		return coalitions().get(true).size();
	}
	
	public int numberOfAnticoalitions() {
		return (int)map.get(false).size();
	}
	
	public String formatCoalitions() {
		StringBuilder sb=new StringBuilder();
		Map<Boolean, List<UndirectedAffinityGraph<V, E>>> coal=coalitions();
		sb.append("Coalitions: \n");
		coal.get(true).stream().forEach(x->sb.append(x+"\n"));
		sb.append("\nAnticoalitions: \n");
		coal.get(false).stream().forEach(x->sb.append(x+"\n"));
		return sb.append("\n").toString();
	}
	
	public double averageDegreeInCoalition() {
		return map.get(true).stream().mapToDouble(x->averageDegree(x)).average().orElse(0);
	}
	
	public double averageDegreeInAnticoalition() {
		return map.get(false).stream().mapToDouble(x->averageDegree(x)).average().orElse(0);
	}
	
	public static<V,E> double averageDegree(UndirectedAffinityGraph<V, E> clus) {
		return clus.getVertices().stream().mapToInt(x->clus.getDegree(x)).average().orElse(0.00);
	}
	
	public boolean existenceOfGiant() {
		double efp=(graph.getVertices().size()*85)/100;
		return clusters.stream().filter(x->x.getVertices().size()>=efp).collect(Collectors.toList()).size()>0;
	}
	
	public double averageDensityOfCoalitions() {
		return map.get(true).stream().mapToDouble(x->density(x)).average().orElse(0.00);
	}
	
	public double averageDensityOfAnticoalitions() {
		return map.get(false).stream().mapToDouble(x->density(x)).average().orElse(0.00);
	}
	
	private static<V,E> double density(UndirectedAffinityGraph<V, E> clust) {
		int max=(clust.getVertices().size()*(clust.getVertices().size()-1))/2;
		if(max==0) return 0;
		return clust.getEdges().size()/max;
	}
	
	public String formatClusterNetwork() {
		StringBuffer sb=new StringBuffer();
		sb.append("</graph>\n");
		UndirectedSparseGraph<Cluster<V,E>, String> cn=clusterNetwork();
		cn.getVertices().stream().forEach(x->sb.append("\t<node>"+x+"</node>\n"));
		sb.append("\n");
		cn.getEdges().stream().forEach(x->sb.append("\t<edge>"+x+"</edge>\n"));
		sb.append("</graph>");
		return sb.toString();
	}
	
	

}
