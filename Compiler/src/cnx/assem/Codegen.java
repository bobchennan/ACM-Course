package cnx.assem;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import cnx.quad.*;
import cnx.translate.CompilationUnit;
import cnx.regalloc.*;



public class Codegen {

	private LinkedList<Assem> inits = new LinkedList<Assem>();
	
	public void gen(Assem assem) {
		lines.add(assem.toString());
	}

	public void gen(AssemList assems) {
		for (AssemList p = assems; p != null; p = p.tail) {
			gen(p.head);
		}
	}

	private ArrayList<String> lines = new ArrayList<String>();
	
	public void gen(CompilationUnit cu, RegAlloc allocator) {
		ArrayList<Quad> quads = new ArrayList<Quad>(cu.getQuads());
		
		ArrayList<Assem> assems = new ArrayList<Assem>();
		for (Quad q : quads) {
			for (AssemList p = q.gen(); p != null; p = p.tail) {
				assems.add(p.head);
			}
		}
		for (Assem assem : assems) {
			lines.add(assem.toString(allocator));
		}
	}
	
	public void flush(PrintStream out) {
		
		for (String x : lines)
			out.println(x);
	}
}
