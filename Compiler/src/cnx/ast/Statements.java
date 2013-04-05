package cnx.ast;

import cnx.symbol.Symbol;

public class Statements {
	public Statement _x = null;
	public Statements _link = null;
	
	public Statements(){
	}
	public Statements(Statement x){
		_x = x;
	}
	public Statements(Statements y, Statement x){
		_x = x;
		_link = y;
	}
}