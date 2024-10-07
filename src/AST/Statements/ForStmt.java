package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Utilities.PokeVisitor;

public class ForStmt extends Statement {

    Expression cond;
    Expression nextExpr;
    BlockStmt body;

    public ForStmt(Expression cond, BlockStmt body) { this(cond,null,body); }

    public ForStmt(Expression cond, Expression nextExpr, BlockStmt body) {
        this.cond = cond;
        this.nextExpr = nextExpr;
        this.body = body;

        addChild(this.cond);
        addChild(this.nextExpr);
        addChild(this.body);
    }

    public Expression getCondition() { return cond; }
    public Expression getNextExpr() { return nextExpr; }
    public BlockStmt getBody() { return body; }
    
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsForStmt(this); }
}
