package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class BinExpr extends Expression {

    public BinExpr(Token t, Expression LHS, Expression RHS, BinOp op) {
        super(t);
        addChild(LHS);
        addChild(RHS);
        addChild(op);
        childCount = 3;
    }

    public Expression getLHS() { return (Expression) children.get(0); }
    public Expression getRHS() { return (Expression) children.get(1); }
    public BinOp getBinOp() { return (BinOp) children.get(2); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBinExpr(this); }
}
