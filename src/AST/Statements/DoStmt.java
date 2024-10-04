package AST.Statements;

import AST.AST;
import AST.Expressions.*;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                DOSTMT
----------------------------------------------------------------------
Fields:
    1. blockStmt: Block statement representing do block
    2. nextExpr: Expression representing next condition
    3. whileExpr: Expression representing while condition
*/
public class DoStmt extends AST {

    BlockStmt doBlock;
    Expression nextExpr;
    Expression whileExpr;

    public DoStmt(BlockStmt doBlock, Expression whileExpr) { this(doBlock,null,whileExpr); }

    public DoStmt(BlockStmt doBlock, Expression nextExpr, Expression whileExpr) {
        this.doBlock = doBlock;
        this.nextExpr = nextExpr;
        this.whileExpr = whileExpr;

        addChild(this.doBlock);
        addChild(this.nextExpr);
        addChild(this.whileExpr);
        setParent();
    }

    public BlockStmt getDoBlock() { return doBlock; }
    public Expression getNextExpr() { return nextExpr; }
    public Expression getWhileExpr() { return whileExpr; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsDoStmt(this); }
}
