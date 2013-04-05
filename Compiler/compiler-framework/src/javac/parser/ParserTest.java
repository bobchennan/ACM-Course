package javac.parser;

import java.io.FileInputStream;
import java.io.InputStream;

import javac.absyn.TranslationUnit;
import javac.util.AbsynFormatter;

public final class ParserTest {

	public static void main(String[] args) throws Exception {
		parse("queens.java");
	}
	
	private static void parse(String filename) throws Exception {
		final InputStream in = new FileInputStream(filename);
		final Parser parser = new Parser(new Yylex(in));
		final java_cup.runtime.Symbol parseTree = parser.parse();
		in.close();
		final TranslationUnit translationUnit = (TranslationUnit) parseTree.value;
		System.out.println(AbsynFormatter.format(translationUnit.toString()));
	}
}
