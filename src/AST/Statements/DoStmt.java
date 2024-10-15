package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Token.*;
import Utilities.PokeVisitor;

public class DoStmt extends Statement {

    private BlockStmt doBlock;
    private Expression nextExpr;
    private Expression cond;

    public DoStmt(Token t, BlockStmt db, Expression c) { this(t,db,null,c); }

    public DoStmt(Token t, BlockStmt db, Expression ne, Expression c) {
        super(t);
        this.doBlock = db;
        this.nextExpr = ne;
        this.cond = c;

        addChild(this.doBlock);
        addChild(this.nextExpr);
        addChild(this.cond);
        setParent();
    }

    public BlockStmt getDoBlock() { return doBlock; }
    public Expression getNextExpr() { return nextExpr; }
    public Expression getCondition() { return cond; }

    public boolean isDoStmt() { return true; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsDoStmt(this); }
}
