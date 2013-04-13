package cnx.ast;

public class Array_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	public Expression _y = null;
	
	public Array_expression(Postfix_expression x, Expression y){
		_x = x;
		_y = y;
	}
}
