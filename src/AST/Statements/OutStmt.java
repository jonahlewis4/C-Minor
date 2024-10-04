package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

public class OutStmt extends Statement {

    Sequence outExprs;

    public OutStmt(Sequence outExprs) {
        this.outExprs = outExprs;
        addChild(this.outExprs);
        setParent();
    }

    public Sequence getOutExprs() { return outExprs; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsOutStmt(this); }
}
