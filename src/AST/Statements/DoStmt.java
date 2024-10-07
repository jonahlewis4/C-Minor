package AST.Statements;

import AST.AST;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class DoStmt extends Statement {

    BlockStmt doBlock;
    Expression nextExpr;
    Expression cond;

    public DoStmt(BlockStmt doBlock, Expression cond) { this(doBlock,null,cond); }

    public DoStmt(BlockStmt doBlock, Expression nextExpr, Expression cond) {
        this.doBlock = doBlock;
        this.nextExpr = nextExpr;
        this.cond = cond;

        addChild(this.doBlock);
        addChild(this.nextExpr);
        addChild(this.cond);
        setParent();
    }

    public BlockStmt getDoBlock() { return doBlock; }
    public Expression getNextExpr() { return nextExpr; }
    public Expression getCondition() { return cond; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsDoStmt(this); }
}
