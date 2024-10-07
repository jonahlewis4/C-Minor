package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class UnaryExpr extends Expression {

    public enum PreOp { NOT, TILDE }
    Expression expr;
    PreOp op;

    public UnaryExpr(Expression expr, PreOp op) {
        this.expr = expr;
        this.op = op;
        //TODO: ADD OP as child
        addChild(this.expr);
    }

    public Expression getExpr() { return expr; }
    public PreOp getOp() { return op; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsUnaryExpr(this); }
}
