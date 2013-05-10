package cnx.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import tiger.RegAlloc.LinearScan;

import cnx.syntactic.*;
import cnx.semant.*;
import cnx.translate.CompilationUnit;
import cnx.translate.Translate;
import cnx.analysis.*;
import cnx.ast.Program;
import cnx.error.Error;
import cnx.quad.*;
import cnx.regalloc.*;
import cnx.assem.*;

public class Main {
	private static int compile(String filename) throws Exception{
		InputStream inp = new BufferedInputStream(new FileInputStream(filename));
		Parser parser = new Parser(inp);
		java_cup.runtime.Symbol parseTree = null;
		try {
			parseTree = parser.parse();
		} catch (Throwable e) {
			throw e;
		} finally {
			inp.close();
		}

		if (parser.error || parser.fatalError) Error.error();

		Program tree = (Program) parseTree.value;

		Semant semant = new Semant();
		semant.checkProgram(tree);
		
		Beautifier formater = new Beautifier();
		formater.start(tree);
		
		Translate tran = new Translate();
		tran.tranProgram(tree);
		
		Analyzer ana = new Analyzer();
		System.out.println("----------Top Level----------");
		for(Quad p:tran.ans)
			System.out.println(p);
		System.out.println("---------!Top Level----------");
		System.out.println("");
		for(CompilationUnit p:tran.fun){
			System.out.println("----------" + p.getLabel() + "----------");
			p.findBlocks(ana);
			p.findLiveness(ana);
			LinearScan sc = new LinearScan(p, ana);
			for(Block q:p.getBlocks()){
				System.out.print(q);
				System.out.println(q.IN);
				System.out.println(sc.map(q.IN));
				System.out.println(q.OUT);
				System.out.println(sc.map(q.OUT));
				System.out.println("");
			}
			System.out.println("---------!" + p.getLabel() + "----------");
			System.out.println("");
		}
		
		Codegen codegen = new Codegen();
		codegen.gen(new Assem(".text"));
		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem(".global main"));
		codegen.gen(new Assem("main:"));
		codegen.gen(new Assem("j top_level"));
		for (CompilationUnit u : tran.fun) {
			codegen.gen(u, new LinearScan(u, ana));
		}
		codegen.gen(new Assem("top_level:"));
		
		return 0;
	}
	
	public static void main(String argv[]) {
		if (argv.length > 0) {
			try {
				compile(argv[0]);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
		}
	}
}
