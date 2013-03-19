package appetizer.syntactic;

%%

%unicode
%line
%column
%cup
%implements Symbols

%{
	private boolean commentCount = false;

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
String = \".*\";
Char = \'.\'

%state	YYCOMMENT

%%

<YYINITIAL> {
	"/*" { commentCount = true; yybegin(YYCOMMENT); }
	"*/" { err("Comment symbol do not match!"); }

	"typedef" { return tok(TYPEDEF); }
	"void" { return tok(VOID); }
	"char" { return tok(CHAR); }
	"int" { return tok(INT); }
	"struct" { return tok(STRUCT); }
	"union" { return tok(UNION); }
	"if" { return tok(IF); }
	"else" { return tok(ELSE); }
	"while" { return tok(WHILE); }
	"for" { return tok(FOR); }
	"continue" { return tok(CONTINUE); }
	"break" { return tok(BREAK); }
	"return" { return tok(RETURN); }
	"sizeof" { return tok(SIZEOF); }

	"(" { return tok(LPAREN); }
	")" { return tok(RPAREN); }
	";" { return tok(SEMICOLON); }
	"," { return tok(COMMA); }
	"=" { return tok(ASSIGN); }
	"{" { return tok(LBRACE); }
	"}" { return tok(RBRACE); }
	"[" { return tok(LBRACK); }
	"]" { return tok(RBRACK); }
	"|" { return tok(BITOR); }
	"^" { return tok(INDEX); }
	"&" { return tok(BITAND); }
	"<"  { return tok(LT); }
	">"  { return tok(GT); }
	"+" { return tok(PLUS); }
	"-" { return tok(MINUS);}
	"*" { return tok(ASTER); }
	"/" { return tok(DIVIDE); }
	"%" { return tok(MOD); }
	"~" { return tok(BITNOT); }
	"!" { return tok(NOT); }
	"." { return tok(DOT); }

	"||" { return tok(OR); }
	"&&" { return tok(AND); }
	"==" { return tok(EQ); }
	"!=" { return tok(NE) ;}
	"<=" { return tok(LE); }
	">=" { return tok(GE); }
	"<<" { return tok(SHL); }
	">>" { return tok(SHR); }
	"++" { return tok(INC); }
	"--" { return tok(DEC); }
	"->" { return tok(PTR); }
	"..." { return tok(ELLIPSIS); }

	"*=" { return tok(MULASS); }
	"/=" { return tok(DIVASS); }
	"%=" { return tok(MODASS); }
    "+=" { return tok(ADDASS); }
    "-=" { return tok(SUBASS); }
    "<<=" { return tok(SHLASS); } 
    ">>=" { return tok(SHRASS); }
    "&=" { return tok(ANDASS); }
    "^=" { return tok(XORASS); }
    "|=" { return tok(ORASS); }

    {Identifier} { return tok(ID, yytext()); }
	{DecInteger} { return tok(NUM, new Integer(yytext())); }
	{String} { return tok(STR,new String(yytext())); }
	{Char} { return tok(CHR,new Char(yytext())); }
	{Whitespace} { /* skip */ }

	[^] { throw new RuntimeException("Illegal character " + yytext() + " in line " + (yyline + 1) + ", column " + (yycolumn + 1)); }
}

<YYCOMMENT> {
	"/*" { commentCount=true; }
	"*/" { if (commentCount == false) yybegin(YYINITIAL);commentCount=false; }
	[^]  {}
}
