package AST.Statements;

import AST.AST;
import AST.Expressions.Expression;
import AST.Token;
import AST.Type;
import Utilities.PokeVisitor;

/*
----------------------------------------------------------------------
                                RETURNSTMT
----------------------------------------------------------------------
Fields:
    1. returnExpr: Expression representing what we are returning
*/
public class ReturnStmt extends Statement {

    Expression returnExpr;

    public ReturnStmt(Expression returnExpr) {
        this.returnExpr = returnExpr;
        children.add(this.returnExpr);
        setParent();
    }

    public Expression getReturnExpr() { return returnExpr; }

    @Override
    public AST whosThatNode(PokeVisitor v) { return v.itsReturnStmt(this); }
}
