package AST.Operators;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf node
public class BinaryOp extends Operator {

    public static enum BinaryType { EQEQ, NEQ, GT, GTEQ, LT, LTEQ, LTGT,
                              UFO, PLUS, MINUS, MULT, DIV, MOD, EXP }
    public static String[] names = { "==", "!=", ">", ">=", "<", "<=", "<>", "<=>", "+", "-", "*", "/", "%", "**"};

    private BinaryType myOp;

    public BinaryOp(Token t, BinaryType op) {
        super(t);
        this.myOp = op;
    }

    public BinaryType getBinaryOp() { return myOp; }
    public boolean isBinaryOp() { return true; }

    @Override
    public String toString() { return names[myOp.ordinal()]; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBinOp(this); }
}
