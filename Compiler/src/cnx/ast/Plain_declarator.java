package cnx.ast;

import cnx.symbol.Symbol;

public class Plain_declarator {
	public Plain_declarator _link = null;
	public Symbol _sym = null;
	
	public Plain_declarator(Plain_declarator x){
		_link = x;
	}
	public Plain_declarator(Symbol sym){
		_sym = sym;
	}
}