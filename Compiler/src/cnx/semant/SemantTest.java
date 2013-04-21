package cnx.semant;

import cnx.syntactic.*;
import java.io.*;
import cnx.ast.*;
import cnx.error.Error;

public class SemantTest {
	private static int semant(String filename) throws Exception{
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
		return 0;
	}
	
	public static void main(String argv[]) {
		if (argv.length > 0) {
			try {
				semant(argv[0]);
				System.out.println('0');
				System.exit(0);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
		}
	}
}
