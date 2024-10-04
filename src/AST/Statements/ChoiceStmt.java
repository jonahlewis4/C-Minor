package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                CHOICESTMT
----------------------------------------------------------------------
Fields:
    1. choiceExpr: Expression representing the while condition

*/

public class ChoiceStmt extends Statement {

    Expression choiceExpr;
    Sequence caseStmts;
    BlockStmt block;

    public ChoiceStmt(Expression choiceExpr, Sequence caseStmts, BlockStmt block) {
        this.choiceExpr = choiceExpr;
        this.caseStmts = caseStmts;
        this.block = block;

        addChild(this.choiceExpr);
        addChild(this.caseStmts);
        addChild(this.block);
        setParent();
    }

    public Expression getChoiceExpr() { return choiceExpr; }
    public Sequence getCaseStmts() { return caseStmts; }
    public BlockStmt getBlock() { return block; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsChoiceStmt(this); }
}
