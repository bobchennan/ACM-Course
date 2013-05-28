package cnx.main;

import java.io.*;
import java.util.*;

import cnx.syntactic.*;
import cnx.semant.*;
import cnx.translate.CompilationUnit;
import cnx.translate.Translate;
import cnx.analysis.*;
import cnx.ast.Program;
import cnx.env.Constants;
import cnx.error.Error;
import cnx.quad.*;
import cnx.regalloc.*;
import cnx.assem.*;
import cnx.temp.*;
import cnx.translate.DataFrag;

public class Main {
	private static int compile(String filename, boolean opt) throws Exception{
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
		
		Beautifier formater = new Beautifier(filename+".cnx");
		formater.start(tree);
		
		Translate tran = new Translate();
		tran.ans.add(new LABEL(new Label(Constants.top_level)));
		tran.ans.add(new Enter(new Label(Constants.top_level), new LinkedList<Temp>()));
		tran.tranProgram(tree);
		tran.ans.add(new Leave(new Label(Constants.top_level)));
		
		Analyzer ana = new Analyzer();
		PrintStream mid = new PrintStream(new BufferedOutputStream(new FileOutputStream("cnx.tmp")));
		mid.println("----------Top Level----------");
		for(Quad p:tran.ans)
			mid.println(p);
		mid.println("---------!Top Level----------");
		mid.println("");
		for(CompilationUnit p:tran.fun){
			for(Quad q: p.getQuads())
				mid.println(q);
		}
		mid.close();
		
		PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("cnx.asm")));
		Codegen codegen = new Codegen();
		codegen.gen(new Assem(".data"));
		
		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem(".globl args"));
		codegen.gen(new Assem("!args:\t.space %", (tran.maxArg + 1) * Constants.pointerSize));
		
		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem(".globl disp"));
		codegen.gen(new Assem("!disp:\t.space %", (Constants.now.localSize() + 1) * Constants.pointerSize));

		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem("!gc_sp_limit:"));
		codegen.gen(new Assem(".word 0"));
		
		codegen.gen(new Assem(".align 2"));
		
		for (DataFrag dataFrag : tran.getDataFrags()) {
			codegen.gen(dataFrag.gen());
		}
		
		codegen.gen(new Assem(".text"));
		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem(".globl main"));
		for (CompilationUnit u : tran.fun) {
			u.findBlocks(ana);
			u.findLiveness(ana);
			codegen.gen(u, new LinearScan(u, ana));
		}
		
		CompilationUnit top = new CompilationUnit(tran.ans);
		top.findBlocks(ana);
		top.findLiveness(ana);
		codegen.gen(top, new LinearScan(top, ana));
		codegen.flush(out);
		printRuntime(out);
		out.close();
		
		return 0;
	}
	
	private static void printRuntime(PrintStream out) throws FileNotFoundException {
		String runtime_s = "runtime.asm";
		Scanner scanner = new Scanner(new BufferedInputStream(Main.class.getResourceAsStream(runtime_s)));
		while (scanner.hasNextLine()) {
			out.println(scanner.nextLine());
		}
		scanner.close();
	}
	
	public static void main(String argv[]) {
		if (argv.length > 0) {
			try {
				compile(argv[0], true);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
		}
	}
}
