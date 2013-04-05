package javac.absyn;

public interface NodeVisitor {

	void visit(ArrayType arrayType);
	void visit(BinaryExpr binaryExpr);
	void visit(BreakStmt breakStmt);
	void visit(CharLiteral charLiteral);
	void visit(CharType charType);
	void visit(CompoundStmt compoundStmt);
	void visit(ContinueStmt continueStmt);
	void visit(ExprStmt exprStmt);
	void visit(FieldPostfix fieldPostfix);
	void visit(ForStmt forStmt);
	void visit(FunctionCall functionCall);
	void visit(FunctionDef functionDef);
	void visit(FunctionHead functionHead);
	void visit(Id id);
	void visit(IdList idList);
	void visit(IdType idType);
	void visit(IfStmt ifStmt);
	void visit(IntLiteral intLiteral);
	void visit(IntType intType);
	void visit(NewArray newArray);
	void visit(NewRecord newRecord);
	void visit(Null n);
	void visit(ParameterDecl parameterDecl);
	void visit(ParameterList parameterList);
	void visit(PrototypeDecl prototypeDecl);
	void visit(RecordDef recordDef);
	void visit(ReturnStmt returnStmt);
	void visit(StmtList stmtList);
	void visit(StringLiteral stringLiteral);
	void visit(StringType stringType);
	void visit(SubscriptPostfix subscriptPostfix);
	void visit(TranslationUnit translationUnit);
	void visit(UnaryExpr unaryExpr);
	void visit(VariableDecl variableDecl);
	void visit(VariableDeclList variableDeclList);
	void visit(WhileStmt whileStmt);
}
