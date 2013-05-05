package cnx.translate;

import cnx.syntactic.*;
import java.io.*;
import cnx.ast.*;
import cnx.error.Error;
import cnx.semant.*;

public class TranslateTest {
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
		
		Translate tran = new Translate();
		tran.tranProgram(tree);
		for(int i = 0; i < tran.ans.size(); ++i)
			System.out.println(tran.ans.get(i));
		return 0;
	}
	
	public static void main(String argv[]) {
		if (argv.length > 0) {
			try {
				semant(argv[0]);
				System.exit(0);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(1);
			}
		}
	}
}
