all: Parser.java Symbols.java Yylex.java

Parser.java: cnx.cup
	java -jar appetizer/lib/java-cup-11a.jar -parser Parser -symbols Symbols -interface < cnx.cup

Symbols.java: cnx.cup
	java -jar appetizer/lib/java-cup-11a.jar -parser Parser -symbols Symbols -interface < cnx.cup

Yylex.java: cnx.flex
	java -cp appetizer/lib/JFlex.jar JFlex.Main cnx.flex

clean:
	rm -f Parser.java Symbols.java Yylex.java