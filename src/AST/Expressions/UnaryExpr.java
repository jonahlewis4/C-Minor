package AST.Expressions;

import AST.*;
import Utilities.PokeVisitor;

public class UnaryExpr extends Expression {

    public UnaryExpr(Token t) {
        super(t);
    }
    
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsUnaryExpr(this); }
}
