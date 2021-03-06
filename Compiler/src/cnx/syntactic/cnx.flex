package cnx.syntactic;

import java.io.*;

%%

%unicode
%line
%column
%cup
%implements Symbols

%{
	private boolean commentCount = false;
	private boolean isString = false;
	private StringBuffer str = new StringBuffer();

	private void err(String message) {
		System.out.println("Scanning error in line " + yyline + ", column " + yycolumn + ": " + message);
	}

	private java_cup.runtime.Symbol tok(int kind) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn);
	}

	private java_cup.runtime.Symbol tok(int kind, Object value) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn, value);
	}
%}

%eofval{
	{
		if (yystate() == YYCOMMENT) {
			err("Comment symbol do not match (EOF)!");
		}
		return tok(EOF, null);
	}
%eofval}

LineTerm = \n|\r|\r\n
Identifier = [a-zA-Z_$][a-zA-Z0-9_$]*
DecInteger = [0-9]+|0[xX][a-zA-Z0-9]+
Whitespace = {LineTerm}|[ \t\f]

%state	YYCOMMENT
%state  YYSTRING
%state  YYPRE

%%

<YYINITIAL> {
    \"	 { isString=true;str.setLength(0); yybegin(YYSTRING);}
    \'   { isString=false;str.setLength(0); yybegin(YYSTRING);}
	"/*" { commentCount = true; yybegin(YYCOMMENT); }
	"*/" { err("Comment symbol do not match!"); }
	\#   {yybegin(YYPRE);}
	"//" {yybegin(YYPRE);}
	

	"typedef" { /*System.out.println(yytext());*/return tok(TYPEDEF); }
	"void" { /*System.out.println(yytext());*/return tok(VOID); }
	"char" { /*System.out.println(yytext());*/return tok(CHAR); }
	"int" { /*System.out.println(yytext());*/return tok(INT); }
	"struct" { /*System.out.println(yytext());*/return tok(STRUCT); }
	"union" { /*System.out.println(yytext());*/return tok(UNION); }
	"if" { /*System.out.println(yytext());*/return tok(IF); }
	"else" { /*System.out.println(yytext());*/return tok(ELSE); }
	"while" { /*System.out.println(yytext());*/return tok(WHILE); }
	"for" { /*System.out.println(yytext());*/return tok(FOR); }
	"continue" { /*System.out.println(yytext());*/return tok(CONTINUE); }
	"break" { /*System.out.println(yytext());*/return tok(BREAK); }
	"return" { /*System.out.println(yytext());*/return tok(RETURN); }
	"sizeof" { /*System.out.println(yytext());*/return tok(SIZEOF); }

	"(" { /*System.out.println(yytext());*/return tok(LPAREN); }
	")" { /*System.out.println(yytext());*/return tok(RPAREN); }
	";" { /*System.out.println(yytext());*/return tok(SEMICOLON); }
	"," { /*System.out.println(yytext());*/return tok(COMMA); }
	"=" { /*System.out.println(yytext());*/return tok(ASSIGN); }
	"{" { /*System.out.println(yytext());*/return tok(LBRACE); }
	"}" { /*System.out.println(yytext());*/return tok(RBRACE); }
	"[" { /*System.out.println(yytext());*/return tok(LBRACK); }
	"]" { /*System.out.println(yytext());*/return tok(RBRACK); }
	"|" { /*System.out.println(yytext());*/return tok(BITOR); }
	"^" { /*System.out.println(yytext());*/return tok(INDEX); }
	"&" { /*System.out.println(yytext());*/return tok(BITAND); }
	"<"  { /*System.out.println(yytext());*/return tok(LT); }
	">"  { /*System.out.println(yytext());*/return tok(GT); }
	"+" { /*System.out.println(yytext());*/return tok(PLUS); }
	"-" { /*System.out.println(yytext());*/return tok(MINUS);}
	"*" { /*System.out.println(yytext());*/return tok(ASTER); }
	"/" { /*System.out.println(yytext());*/return tok(DIVIDE); }
	"%" { /*System.out.println(yytext());*/return tok(MOD); }
	"~" { /*System.out.println(yytext());*/return tok(BITNOT); }
	"!" { /*System.out.println(yytext());*/return tok(NOT); }
	"." { /*System.out.println(yytext());*/return tok(DOT); }

	"||" { /*System.out.println(yytext());*/return tok(OR); }
	"&&" { /*System.out.println(yytext());*/return tok(AND); }
	"==" { /*System.out.println(yytext());*/return tok(EQ); }
	"!=" { /*System.out.println(yytext());*/return tok(NE) ;}
	"<=" { /*System.out.println(yytext());*/return tok(LE); }
	">=" { /*System.out.println(yytext());*/return tok(GE); }
	"<<" { /*System.out.println(yytext());*/return tok(SHL); }
	">>" { /*System.out.println(yytext());*/return tok(SHR); }
	"++" { /*System.out.println(yytext());*/return tok(INC); }
	"--" { /*System.out.println(yytext());*/return tok(DEC); }
	"->" { /*System.out.println(yytext());*/return tok(PTR); }
	"..." { /*System.out.println(yytext());*/return tok(ELLIPSIS); }

	"*=" { /*System.out.println(yytext());*/return tok(MULASS); }
	"/=" { /*System.out.println(yytext());*/return tok(DIVASS); }
	"%=" { /*System.out.println(yytext());*/return tok(MODASS); }
    "+=" { /*System.out.println(yytext());*/return tok(ADDASS); }
    "-=" { /*System.out.println(yytext());*/return tok(SUBASS); }
    "<<=" { /*System.out.println(yytext());*/return tok(SHLASS); } 
    ">>=" { /*System.out.println(yytext());*/return tok(SHRASS); }
    "&=" { /*System.out.println(yytext());*/return tok(ANDASS); }
    "^=" { /*System.out.println(yytext());*/return tok(XORASS); }
    "|=" { /*System.out.println(yytext());*/return tok(ORASS); }
    {Identifier} 
	{
		boolean ty=false;
		if(ParserTest.isTypeID(yytext()))ty=true;
		else ty=false;
		//System.out.println(yytext()+" "+(ty?"TYPE":"ID"));
		if(ty)return tok(TYPEID,yytext());
		else return tok(ID,yytext());
	}
	{DecInteger} { /*System.out.println(yytext());*/return tok(NUM, new Integer(yytext())); }
	{Whitespace} { /* skip */ }

	[^] {throw new RuntimeException("Illegal character " + yytext() + " in line " + (yyline + 1) + ", column " + (yycolumn + 1)); }
}

<YYCOMMENT> {
	"/*" { commentCount=true; }
	"*/" { if (commentCount == true) {commentCount=false;yybegin(YYINITIAL);} }
	[^]  {}
}
<YYPRE>{
    \n {yybegin(YYINITIAL);}
    [^] {}
}
<YYSTRING> {
	\"|\'	{
		yybegin(YYINITIAL); 
		if(isString)return tok(STR, str.toString());
		else if(str.length()>1)err("char presentation error(length error)!");
		else return tok(CHR,str.toString().charAt(0));
	}
	\\[0-9]{3}	{
		int n = Integer.parseInt(yytext().substring(1, 4));
		if (n > 255) err("String presentation error (\\ddd exceeded 255)!");
		else str.append((char) n);
	}
	[^\n\t\"\'\\]+	{str.append(yytext());}
	\\t				{str.append("\t");}
	\\n				{str.append("\n");}
	\\\"			{str.append("\"");}
	\\\\			{str.append("\\");}
	{LineTerm}		{err("String presentation error (unexpected line terminator)!");}
	\\{Whitespace}+\\	{ /* do nothing */ }
}
