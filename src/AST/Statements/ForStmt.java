package AST.Statements;

import AST.*;
import AST.Expressions.*;
import Token.*;
import Utilities.PokeVisitor;

public class ForStmt extends Statement {

    private Expression cond;
    private Expression nextExpr;
    private BlockStmt body;

    public ForStmt(Token t, Expression c, BlockStmt b) { this(t,c,null,b); }

    public ForStmt(Token t, Expression c, Expression ne, BlockStmt b) {
        super(t);
        this.cond = c;
        this.nextExpr = ne;
        this.body = b;

        addChild(this.cond);
        addChild(this.nextExpr);
        addChild(this.body);
        setParent();
    }

    public Expression getCondition() { return cond; }
    public Expression getNextExpr() { return nextExpr; }
    public BlockStmt getBody() { return body; }

    public boolean isForStmt() { return true; }
    
    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsForStmt(this); }
}
