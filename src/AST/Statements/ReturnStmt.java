package AST.Statements;

import AST.*;
import AST.Expressions.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;


public class ReturnStmt extends Statement {

    private Type type;

    Expression expr;

    public ReturnStmt(Token t, Expression expr) {
        super(t);
        this.expr = expr;

        children.add(this.expr);
        setParent();
    }

    public Expression getExpr() { return expr; }
    public Type getReturnType() { return type; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsReturnStmt(this); }
}
