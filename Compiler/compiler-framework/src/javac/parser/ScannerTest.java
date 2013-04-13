package javac.parser;

import static javac.parser.Symbols.*;

public final class ScannerTest {

	public static void main(String[] args) throws Exception {
		scan("queens.java");
	}
	
	private static void scan(String filename) throws Exception {
		java.io.InputStream in = new java.io.FileInputStream(filename);
		java_cup.runtime.Scanner lexer = new Yylex(in);
		java_cup.runtime.Symbol tok = null;
		
		do {
			tok = lexer.next_token();
			if (tok.sym == STRING_LITERAL) {
				System.out.print("\"" + tok.value + "\"");
			} else if (tok.sym == INTEGER) {
				System.out.print(tok.value);
			} else if (tok.sym == CHARACTER) {
				System.out.print("'" + tok.value + "'");
			} else if (tok.sym == ID) {
				System.out.print(tok.value);
			} else {
				System.out.print(symnames[tok.sym]);
			}
		} while (tok.sym != EOF);
		
		in.close();
	}
	
	static String[] symnames = new String[1000];
	static {
		symnames[LRBRACKET] = "[]";
		symnames[DIVIDE] = " / ";
		symnames[LBRACKET] = "[";
		symnames[CHAR] = "char ";
		symnames[LPAREN] = "(";
		symnames[GREATER] = " > ";
		symnames[CONTINUE] = "continue";
		symnames[INT] = "int ";
		symnames[LESS] = " < ";
		symnames[FOR] = "for ";
		symnames[MINUS] = " - ";
		symnames[RPAREN] = ") ";
		symnames[NOT] = "!";
		symnames[SEMICOLON] = ";\n";
		symnames[AND] = " && ";
		symnames[RECORD] = "record ";
		symnames[OR] = " || ";
		symnames[COMMA] = ", ";
		symnames[MULTIPLY] = " * ";
		symnames[PLUS] = " + ";
		symnames[ASSIGN] = " = ";
		symnames[IF] = "if ";
		symnames[DOT] = ".";
		symnames[EOF] = "<EOF>";
		symnames[RETURN] = "return ";
		symnames[RBRACKET] = "]";
		symnames[NEW] = "new ";
		symnames[error] = "<error>";
		symnames[MODULO] = " % ";
		symnames[NULL] = "null";
		symnames[NEQ] = " != ";
		symnames[BREAK] = "break";
		symnames[EQ] = " == ";
		symnames[LBRACE] = "{\n";
		symnames[NATIVE] = "native ";
		symnames[ELSE] = "else ";
		symnames[WHILE] = "while ";
		symnames[LESS_EQ] = " <= ";
		symnames[RBRACE] = "}\n";
		symnames[STRING] = "string ";
		symnames[GREATER_EQ] = " >= ";
	}
}
