package AST.Operators;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf node
public class AssignOp extends Operator {

    public static enum AssignType { EQ, PLUSEQ, MINUSEQ, MULTEQ, DIVEQ, MODEQ, EXPEQ }
    public static String[] names = { "=", "+=", "-=", "*=", "/=", "%=", "**=" };

    private AssignType myOp;

    public AssignOp(Token t, AssignType op) {
        super(t);
        this.myOp = op;
    }

    public AssignType getAssignOp() { return myOp; }
    public boolean isAssignOp() { return true; }

    @Override
    public String toString() { return names[myOp.ordinal()]; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsAssignOp(this); }
}
