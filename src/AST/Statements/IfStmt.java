package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                IFSTMT
----------------------------------------------------------------------
Fields:
    1. condExpr: Expression representing the condition
    2. ifBlock: Block statement representing if block
    3. elseBlock: Block statement representing else block
*/
public class IfStmt extends Statement {

    Expression condExpr;
    BlockStmt ifBlock;
    BlockStmt elseBlock;

    public IfStmt(Expression condExpr, BlockStmt ifBlock) { this(condExpr,ifBlock,null); }

    public IfStmt(Expression condExpr, BlockStmt ifBlock, BlockStmt elseBlock) {
        this.condExpr = condExpr;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;

        addChild(this.condExpr);
        addChild(this.ifBlock);
        addChild(this.elseBlock);
        setParent();
    }

    public Expression getCondExpr() { return this.condExpr; }
    public BlockStmt getIfBlock() { return ifBlock; }
    public BlockStmt getElseBlock() { return elseBlock; }

    /*
    ----------------------------------------------------------------------
                                Visitor Method
    ----------------------------------------------------------------------
    */
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsIfStmt(this); }
}
