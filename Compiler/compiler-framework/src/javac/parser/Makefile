all: 
	# make lexer or make parser

lexer: javac.jflex
	jflex javac.jflex

parser: javac.cup
	java -jar $(CUP_HOME)/java-cup-11a.jar -parser Parser -symbols Symbols -interface < javac.cup
