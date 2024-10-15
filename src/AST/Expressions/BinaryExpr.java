package AST.Expressions;

import AST.*;
import AST.Operators.*;
import Token.*;
import Utilities.PokeVisitor;

public class BinaryExpr extends Expression {

    private Expression LHS;
    private Expression RHS;
    private BinaryOp op;

    public BinaryExpr(Token t, Expression LHS, Expression RHS, BinaryOp op) {
        super(t);
        this.LHS = LHS;
        this.RHS = RHS;
        this.op = op;

        addChild(this.LHS);
        addChild(this.RHS);
        addChild(this.op);
        setParent();
    }

    public Expression getLHS() { return LHS; }
    public Expression getRHS() { return RHS; }
    public BinaryOp getBinaryOp() { return op; }

    public boolean isBinaryExpr() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBinaryExpr(this); }
}
