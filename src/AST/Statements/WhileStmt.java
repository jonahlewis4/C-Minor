package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class WhileStmt extends Statement {

    Expression cond;
    Expression nextExpr;
    BlockStmt whileBlock;

    public WhileStmt(Expression cond, BlockStmt whileBlock) { this(cond,null,whileBlock); }

    public WhileStmt(Expression cond, Expression nextExpr, BlockStmt whileBlock) {
        this.cond = cond;
        this.nextExpr = nextExpr;
        this.whileBlock = whileBlock;

        addChild(cond);
        addChild(nextExpr);
        addChild(whileBlock);
        setParent();
    }

    public Expression getCondition() { return cond; }
    public Expression getNextExpr() { return nextExpr; }
    public BlockStmt getWhileBlock() { return whileBlock; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsWhileStmt(this); }
}
