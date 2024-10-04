package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

public class AssignOp extends AST {

    public enum AssignType { EQ, ADDEQ, SUBEQ, MULTEQ, DIVEQ, MODEQ, EXPEQ }

    AssignType op;

    public AssignOp(AssignType op) {
        this.op = op;
        setParent();
    }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsAssignOp(this); }
}
