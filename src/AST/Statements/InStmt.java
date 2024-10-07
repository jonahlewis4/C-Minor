package AST.Statements;

import AST.*;
import AST.Expressions.Expression;
import Utilities.PokeVisitor;

public class InStmt extends Statement {

    Sequence exprs;

    public InStmt(Sequence exprs) {
        this.exprs = exprs;

        addChild(this.exprs);
        setParent();
    }

    public Sequence getInExprs() { return exprs; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsInStmt(this); }
}
