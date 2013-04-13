package cnx.ast;

public class Dec_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	
	public Dec_expression(Postfix_expression x){
		_x = x;
	}
}
