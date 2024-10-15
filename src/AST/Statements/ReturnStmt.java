package AST.Statements;

import AST.*;
import AST.Expressions.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;


public class ReturnStmt extends Statement {

    private Type type;
    private Expression expr;

    public ReturnStmt(Token t, Type rt) { this(t,rt,null); }

    public ReturnStmt(Token t, Type rt, Expression e) {
        super(t);
        this.type = rt;
        this.expr = e;

        addChild(this.type);
        addChild(this.expr);
        setParent();
    }

    public Type getReturnType() { return type; }
    public Expression getExpression() { return expr; }

    public boolean isReturnStmt() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsReturnStmt(this); }
}
