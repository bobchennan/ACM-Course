package cnx.ast;

public class Function_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	public Arguments _y = null;
	
	public Function_expression(Postfix_expression x){
		_x = x;
	}
	public Function_expression(Postfix_expression x, Arguments y){
		_x = x;
		_y = y;
	}
}
