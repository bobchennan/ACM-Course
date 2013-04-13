package cnx.ast;

public class Inc_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	
	public Inc_expression(Postfix_expression x){
		_x = x;
	}
}
