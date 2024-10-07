package AST.Expressions;

import AST.*;
import AST.Types.*;
import Utilities.PokeVisitor;

// Something to do with constant values
public class CastExpr extends Expression {

    Type myType;
    Expression expr;

    public CastExpr(Type myType, Expression castExpr) {
        expr = castExpr;
        this.myType = myType;

        addChild(myType);
        addChild(expr);
    }

    public Type getCastType() { return myType; }
    public Expression getCastExpr() { return expr; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsCastExpr(this); }
}
