package AST.Statements;

import AST.*;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                BLOCKSTMT
----------------------------------------------------------------------
Fields:
    1. varSeq: Sequence of variables inside block
    2. stmtSeq: Sequence of statements inside block
*/
public class BlockStmt extends Statement {

    Sequence varSeq;
    Sequence stmtSeq;

    public BlockStmt(Sequence varSeq, Sequence stmtSeq) {
        this.varSeq = varSeq;
        this.stmtSeq = stmtSeq;

        addChild(this.varSeq);
        addChild(this.stmtSeq);
        setParent();
    }

    public Sequence getVarSeq() { return this.varSeq; }
    public Sequence getStmtSeq() { return this.stmtSeq; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsBlockStmt(this); }
}
