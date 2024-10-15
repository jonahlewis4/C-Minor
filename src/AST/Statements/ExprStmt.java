package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Token.*;
import Utilities.PokeVisitor;

public class ExprStmt extends Statement {

    private Expression expr;

    public ExprStmt(Token t, Expression e) {
        super(t);
        this.expr = e;

        addChild(this.expr);
        setParent();
    }

    public Expression getExpression() { return expr; }

    public boolean isExprStmt() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsExprStmt(this); }
}
