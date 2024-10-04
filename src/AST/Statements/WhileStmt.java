package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                WHILESTMT
----------------------------------------------------------------------
Fields:
    1. whileExpr: Expression representing the while condition
    2. nextExpr: Expression representing the next condition
    3. whileBlock: Block statement representing while block
*/
public class WhileStmt extends Statement {

    Expression whileExpr;
    Expression nextExpr;
    BlockStmt whileBlock;

    public WhileStmt(Expression whileExpr, BlockStmt whileBlock) { this(whileExpr,null,whileBlock); }

    public WhileStmt(Expression whileExpr, Expression nextExpr, BlockStmt whileBlock) {
        this.whileExpr = whileExpr;
        this.nextExpr = nextExpr;
        this.whileBlock = whileBlock;

        addChild(whileExpr);
        addChild(nextExpr);
        addChild(whileBlock);
        setParent();
    }

    public Expression getWhileExpr() { return whileExpr; }
    public Expression getNextExpr() { return nextExpr; }
    public BlockStmt getWhileBlock() { return whileBlock; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsWhileStmt(this); }
}
