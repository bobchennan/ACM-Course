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
		inits.add(assem);
	}

	public void gen(AssemList assems) {
		for (AssemList p = assems; p != null; p = p.tail) {
			gen(p.head);
		}
	}

	public void gen(CompilationUnit cu, RegAlloc allocator) {
		ArrayList<Quad> quads = new ArrayList<Quad>(cu.getQuads());
		
		ArrayList<Assem> assems = new ArrayList<Assem>();
		for (Quad q : quads) {
			for (AssemList p = q.gen(); p != null; p = p.tail) {
				assems.add(p.head);
			}
		}
	}
	
	public void flush(PrintStream out, boolean opt) {
		
		for (Assem init : inits) {
			out.println(init);
		}
	}
}
