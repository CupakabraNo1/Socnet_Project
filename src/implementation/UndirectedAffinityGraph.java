package implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class UndirectedAffinityGraph<V,E>{
	
	public UndirectedSparseGraph<V, Link<E>> graph;
	
	Transformer<E, Link> linkTrans =
		new Transformer<E,Link>() {
		
			public Link<E> transform(E edge) {
				return new Link<E>(edge);
			}
	};
	
	public UndirectedAffinityGraph() {
		graph=new UndirectedSparseGraph<>();
	}
	
	
	public void addVertex(V v) {
		graph.addVertex(v);
	}
	
		
	public void addEdge(E e,boolean b, V v1, V v2) {
		Link<E> link= linkTrans.transform(e);
		link.setSign(b);
		graph.addEdge(link, v1,v2);
	}
	
	
	public void addEdge(E e, V v1, V v2) {
		Link<E> link= linkTrans.transform(e);
		graph.addEdge(link, v1,v2);
	}
	
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("<graph> \n");
		graph.getVertices().stream().forEach(x->sb.append("\t<node>"+x+"</node>\n"));
		sb.append("\n");
		graph.getEdges().stream().forEach(x->sb.append("\t<edge>"+x+"</edge>\n"));
		return sb.append("</graph>").toString();
	}
	
	
	public Collection<V> getVertices() {
		return graph.getVertices();
	}
	
	
	public Map<E,Boolean> getEdges(){
		return graph.getEdges().stream().collect(Collectors.toMap(x->x.getEdge(), x->x.isSign()));
	}
	
	
	public Pair<V> getPair(E e, boolean s){
		Link<E> link=linkTrans.transform(e);
		link.setSign(s);
		return graph.getEndpoints(link);
	}
	
	
	public boolean getSign(E e) {
		for(Link<E> l:graph.getEdges()) {
			if(e==l.getEdge()) {
				return l.isSign();
			}
		}
		return false;
	}
	
	
	public UndirectedAffinityGraph<V, E> clone(){
		UndirectedAffinityGraph<V, E> clone=new UndirectedAffinityGraph<>();
		graph.getVertices().stream().forEach(x->clone.addVertex(x));
		graph.getEdges().stream().forEach(x->{
			Pair<V> pair=graph.getEndpoints(x);
			if(clone.getVertices().contains(pair.getFirst()) && clone.getVertices().contains(pair.getSecond())){
				clone.addEdge(x.getEdge(),x.isSign(), pair.getFirst(), pair.getSecond());
				
			}
		});
		return clone;
	}
	
	
	public Collection<V> getNeighbours(V v, Boolean s) {
		List<Link<E>> list=graph.getVertices().stream().flatMap(x->graph.getIncidentEdges(v).stream()).distinct().collect(Collectors.toList());
		List<V> vertices=new ArrayList<>();
		for(Link<E> link:list) {
			if(link.isSign()==s) {
				V vert=graph.getOpposite(v, link);
				vertices.add(vert);
			}
		}
		return vertices;
	}
	
	
	public void setSign(E e, boolean b) {
		for(Link<E> l:graph.getEdges()) {
			if(e==l.getEdge()) {
				l.setSign(b);
			}
		}
	}
	
	
	public void deleteEdge(E e) {
		for(Link<E> l:graph.getEdges()) {
			if(e==l.getEdge()) {
				graph.removeEdge(l);
			}
		}
	}
	
	public int getDegree(V v) {
		return graph.degree(v);
		
	}
	
	
}
