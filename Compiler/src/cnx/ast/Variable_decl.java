package cnx.ast;

public class Variable_decl{
	
	public Type_specifier _type;
	public Id_list _ids;

	public Variable_decl(Type_specifier type, Id_list ids) {
		_type = type;
		_ids = ids;
	}
}
