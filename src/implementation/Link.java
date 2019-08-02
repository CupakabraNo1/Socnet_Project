package implementation;

import java.util.Random;

public class Link<E> {
	private E edge;
	private boolean sign;
	
	public Link(E e) {
		Random r = new Random();
		this.edge=e;
		this.sign=r.nextBoolean();
	}
	
	public Link(E edge, boolean sign) {
		this.edge=edge;
		this.sign=sign;
	}

	public E getEdge() {
		return edge;
	}

	public void setEdge(E edge) {
		this.edge = edge;
	}

	public boolean isSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}
	
	@Override
	public String toString() {
		return edge+"/"+(sign? "+":"-");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result + (sign ? 1231 : 1237);
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
		Link other = (Link) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (sign != other.sign)
			return false;
		return true;
	}
	
	
}
