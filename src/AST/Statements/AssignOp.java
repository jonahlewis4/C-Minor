package AST.Statements;

import AST.*;
import Token.*;
import Utilities.PokeVisitor;

// Leaf node
public class AssignOp extends AST {

    public enum AssignType { EQ, ADDEQ, SUBEQ, MULTEQ, DIVEQ, MODEQ, EXPEQ }

    AssignType op;

    public AssignOp(Token t, AssignType op) {
        super(t);
        this.op = op;
    }

    public String getAssignOp() { return myToken.getText(); }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsAssignOp(this); }
}
