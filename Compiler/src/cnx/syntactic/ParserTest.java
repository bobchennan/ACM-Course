package cnx.syntactic;

import java.io.*;
import cnx.symbol.*;

final class ParserTest {

	private static void parse(String filename) throws IOException {
		InputStream inp = new FileInputStream(filename);
		Parser parser = new Parser(inp);
		java_cup.runtime.Symbol parseTree = null;
		try {
			parseTree = parser.parse();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.toString());
		} finally {
			inp.close();
		}
		System.out.println(parseTree.value);
	}

	public static String pathOf(String filename) {
		return ParserTest.class.getResource(filename).getPath();
	}

	public static void main(String argv[]) throws IOException {
		parse(pathOf("test.c"));
	}
}