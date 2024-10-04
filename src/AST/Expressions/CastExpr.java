package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

// Something to do with constant values
public class CastExpr extends Expression {

    public CastExpr(Token t, Type castType, Expression castExpr) {
        super(t);
        addChild(castType);
        addChild(castExpr);
        childCount = 2;
    }

    public Type getCastType() { return (Type) children.get(0); }
    public Expression getCastExpr() { return (Expression) children.get(1); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCastExpr(this); }
}
