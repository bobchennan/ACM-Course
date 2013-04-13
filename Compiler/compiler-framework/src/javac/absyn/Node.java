package javac.absyn;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javac.util.Position;

public abstract class Node {
	
	protected Position pos;

	protected Node(Position pos) {
		this.pos = pos;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public abstract void accept(NodeVisitor visitor);
}
