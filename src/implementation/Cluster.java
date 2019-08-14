package implementation;

import java.util.Set;
import java.util.stream.Collectors;

public class Cluster<V,E> {
	
	private String name;
	private UndirectedAffinityGraph<V, E> graph;
	
	public String getName() {
		return name;
	}
	
	public void setName(int num) {
		this.name = "cluster "+num;
	}
	
	public UndirectedAffinityGraph<V, E> getGraph() {
		return graph;
	}
	
	public void setGraph(UndirectedAffinityGraph<V, E> graph) {
		this.graph = graph;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graph == null) ? 0 : graph.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (graph == null) {
			if (other.graph != null)
				return false;
		} else if (!graph.equals(other.graph))
			return false;
		return true;
	}
	
	public Set<V> nodes(){
		return graph.getVertices().stream().collect(Collectors.toSet());
	}
	
	public boolean containsN(V v) {
		return nodes().contains(v);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(name);
		return sb.toString();
	}

}
