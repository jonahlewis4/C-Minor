package AST.Expressions;

import AST.*;
import AST.Types.*;
import Token.*;
import Utilities.PokeVisitor;

// Something to do with constant values
public class CastExpr extends Expression {

    private Type targetType;
    private Expression expr;

    public CastExpr(Token t, Type tt, Expression e) {
        super(t);
        this.targetType = tt;
        this.expr = e;

        addChild(this.targetType);
        addChild(this.expr);
        setParent();
    }

    public Type getCastType() { return targetType; }
    public Expression getCastExpr() { return expr; }

    public boolean isCastExpr() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCastExpr(this); }
}
