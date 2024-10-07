package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class IfStmt extends Statement {

    Expression cond;
    BlockStmt ifBlock;
    BlockStmt elseBlock;

    public IfStmt(Expression cond, BlockStmt ifBlock) { this(cond,ifBlock,null); }

    public IfStmt(Expression cond, BlockStmt ifBlock, BlockStmt elseBlock) {
        this.cond = cond;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;

        addChild(this.cond);
        addChild(this.ifBlock);
        addChild(this.elseBlock);
        setParent();
    }

    public Expression getCondition() { return cond; }
    public BlockStmt getIfBlock() { return ifBlock; }
    public BlockStmt getElseBlock() { return elseBlock; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsIfStmt(this); }
}
