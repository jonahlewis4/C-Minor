package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class BinExpr extends Expression {

    Expression LHS;
    Expression RHS;
    BinOp op;

    public BinExpr(Expression LHS, Expression RHS, BinOp op) {
        this.LHS = LHS;
        this.RHS = RHS;
        this.op = op;

        addChild(this.LHS);
        addChild(this.RHS);
        addChild(this.op);
    }

    public Expression getLHS() { return (Expression) children.get(0); }
    public Expression getRHS() { return (Expression) children.get(1); }
    public BinOp getBinOp() { return (BinOp) children.get(2); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBinExpr(this); }
}
