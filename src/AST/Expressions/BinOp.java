package AST.Expressions;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf node
public class BinOp extends AST {

    public enum BinOpType {
        EQEQ,
        NEQ,
        GT,
        GTEQ,
        LT,
        LTEQ,
        UFO,
        PLUS,
        MINUS,
        MULT,
        DIV,
        MOD,
        EXP
    }

    private int op;

    public BinOp(Token t, int op) {
        super(t);
        this.op = op;
    }
    
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBinOp(this); }
}
