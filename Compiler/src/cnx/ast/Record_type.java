package cnx.ast;

import cnx.env.Env;
import cnx.symbol.Symbol;
import cnx.type.*;
import cnx.semant.Semant;
import cnx.error.Error;

public class Record_type extends Type_specifier {
	public boolean _isSt = false;
	public Symbol _sym = null;
	public Structs _x = null;
	
	public Record_type(boolean isSt, Symbol sym){
		_isSt = isSt;
		_sym = sym;
	}
	public Record_type(boolean isSt, Structs x){
		_isSt = isSt;
		_x = x;
	}
	public Record_type(boolean isSt, Symbol sym, Structs x){
		_isSt = isSt;
		_sym = sym;
		_x = x;
	}

	@Override
	public Type toType(Env env) {
		env.beginScope();
		RECORD ret=new RECORD(_sym);
		Type tmp;
		while(_x != null){
			if(_x._ty instanceof Id_type){
				if(((Id_type)_x._ty)._sym.equals(_sym))
					tmp = ret;
				else{
					tmp = (Type) env.tEnv.get(((Record_type)_x._ty)._sym);
					if(tmp == null){
						System.out.println('1');
						System.exit(1);
					}
				}
			}
			else tmp = _x._ty.toType(env);
			
			Declarators x = _x._x;
			Type tmp2;
			while(x != null){
				Declarator y = x._x;
				tmp2 = tmp;
				Plain_declarator t = y._x;
				while(t._link != null){
					tmp2 = new POINTER(tmp2);
					t = t._link;
				}
				if(tmp2 == ret)
					Error.error();
				if(y._isFunc)
					Error.error();
				else{
					Constant_expressions s = y._cexp;
					while(s != null){
						/* to check */
						tmp2 = new ARRAY(tmp2,0);
						s = s._link;
					}
				}
				ret.fields.add(new RECORD.RecordField(tmp2,t._sym,0));
				if(!env.vEnv.put(t._sym, tmp2))
					Error.error();
				x = x._link;
			}
			_x = _x._link;
		}
		env.endScope();
		return ret;
	}

}
