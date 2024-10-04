package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

public class InStmt extends Statement {

    Sequence inExprs;

    public InStmt(Sequence inExprs) {
        this.inExprs = inExprs;
        addChild(this.inExprs);
        setParent();
    }

    public Sequence getInExprs() { return inExprs; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsInStmt(this); }
}
